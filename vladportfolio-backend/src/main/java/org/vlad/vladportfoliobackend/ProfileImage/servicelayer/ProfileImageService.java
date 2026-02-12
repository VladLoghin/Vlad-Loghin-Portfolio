package org.vlad.vladportfoliobackend.ProfileImage.servicelayer;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.vlad.vladportfoliobackend.ProfileImage.datalayer.ProfileImage;
import org.vlad.vladportfoliobackend.ProfileImage.repositorylayer.ProfileImageRepository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Service
public class ProfileImageService {

    private final ProfileImageRepository profileImageRepository;
    private final ResourceLoader resourceLoader;

    public ProfileImageService(ProfileImageRepository profileImageRepository,
                               ResourceLoader resourceLoader) {
        this.profileImageRepository = profileImageRepository;
        this.resourceLoader = resourceLoader;
    }

    public Mono<ProfileImage> getProfileImage() {
        return profileImageRepository.findFirstByOrderByUploadedAtDesc()
                .switchIfEmpty(
                        Mono.fromCallable(() -> {
                            Resource resource = resourceLoader.getResource("classpath:profile/profile.jpg");
                            if (!resource.exists()) {
                                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "Profile image not found");
                            }
                            byte[] bytes = resource.getInputStream().readAllBytes();
                            ProfileImage fallback = new ProfileImage();
                            fallback.setFilename("profile.jpg");
                            fallback.setData(bytes);
                            fallback.setContentType("image/jpeg");
                            fallback.setUploadedAt(LocalDateTime.now());
                            return fallback;
                        }).subscribeOn(Schedulers.boundedElastic())
                );
    }

    public Mono<Void> uploadProfileImage(FilePart filePart) {
        return DataBufferUtils.join(filePart.content())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                })
                .flatMap(bytes -> profileImageRepository.findFirstByOrderByUploadedAtDesc()
                        .defaultIfEmpty(new ProfileImage())
                        .flatMap(img -> {
                            img.setFilename(filePart.filename());
                            img.setData(bytes);
                            img.setContentType(resolveContentType(filePart.filename()));
                            img.setUploadedAt(LocalDateTime.now());
                            return profileImageRepository.save(img);
                        })
                )
                .then();
    }

    private String resolveContentType(String filename) {
        if (filename == null) return "image/jpeg";
        String lower = filename.toLowerCase();
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".webp")) return "image/webp";
        if (lower.endsWith(".gif")) return "image/gif";
        return "image/jpeg";
    }
}
