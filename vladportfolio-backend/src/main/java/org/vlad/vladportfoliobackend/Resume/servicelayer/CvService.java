package org.vlad.vladportfoliobackend.Resume.servicelayer;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.vlad.vladportfoliobackend.Resume.datalayer.CvDocument;
import org.vlad.vladportfoliobackend.Resume.repositorylayer.CvDocumentRepository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Service
public class CvService {

    private final CvDocumentRepository cvDocumentRepository;
    private final ResourceLoader resourceLoader;

    public CvService(CvDocumentRepository cvDocumentRepository, ResourceLoader resourceLoader) {
        this.cvDocumentRepository = cvDocumentRepository;
        this.resourceLoader = resourceLoader;
    }

    public Mono<Resource> getCv(String lang) {
        String normalized = (lang == null) ? "en" : lang.toLowerCase();
        String key = normalized.startsWith("fr") ? "fr" : "en";

        return cvDocumentRepository.findByLanguage(key)
                .map(doc -> (Resource) new ByteArrayResource(doc.getData()))
                .switchIfEmpty(
                        Mono.fromCallable(() -> {
                            String path = key.equals("fr")
                                    ? "classpath:cv/CV French.pdf"
                                    : "classpath:cv/CV English.pdf";

                            Resource resource = resourceLoader.getResource(path);

                            if (!resource.exists()) {
                                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "CV file not found for language: " + key);
                            }

                            return resource;
                        }).subscribeOn(Schedulers.boundedElastic())
                );
    }

    public Mono<Void> uploadCv(String lang, FilePart filePart) {
        String key = lang.toLowerCase().startsWith("fr") ? "fr" : "en";

        return DataBufferUtils.join(filePart.content())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                })
                .flatMap(bytes -> cvDocumentRepository.findByLanguage(key)
                        .defaultIfEmpty(new CvDocument())
                        .flatMap(doc -> {
                            doc.setLanguage(key);
                            doc.setFilename(filePart.filename());
                            doc.setData(bytes);
                            doc.setContentType("application/pdf");
                            doc.setUploadedAt(LocalDateTime.now());
                            return cvDocumentRepository.save(doc);
                        })
                )
                .then();
    }
}
