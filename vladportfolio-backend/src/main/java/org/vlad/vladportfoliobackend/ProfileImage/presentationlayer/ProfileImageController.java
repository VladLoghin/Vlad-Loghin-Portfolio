package org.vlad.vladportfoliobackend.ProfileImage.presentationlayer;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.ProfileImage.servicelayer.ProfileImageService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    public ProfileImageController(ProfileImageService profileImageService) {
        this.profileImageService = profileImageService;
    }

    @GetMapping("/public/profile-image")
    public Mono<ResponseEntity<Resource>> getProfileImage() {
        return profileImageService.getProfileImage()
                .map(img -> ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(img.getContentType()))
                        .cacheControl(CacheControl.noCache())
                        .body((Resource) new ByteArrayResource(img.getData())));
    }

    @PostMapping("/profile-image/upload")
    public Mono<ResponseEntity<Void>> uploadProfileImage(
            @RequestPart("file") FilePart file) {
        return profileImageService.uploadProfileImage(file)
                .then(Mono.just(ResponseEntity.ok().<Void>build()));
    }
}
