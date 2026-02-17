package org.vlad.vladportfoliobackend.Resume.presentationlayer;

import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Resume.servicelayer.CvService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class CvController {

    private final CvService cvService;

    public CvController(CvService cvService) {
        this.cvService = cvService;
    }

    @GetMapping(value = "/public/cv", produces = MediaType.APPLICATION_PDF_VALUE)
    public Mono<ResponseEntity<Resource>> getCv(@RequestParam(defaultValue = "en") String lang) {
        return cvService.getCv(lang)
                .map(resource -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"cv-" + lang + ".pdf\"")
                        .cacheControl(CacheControl.noCache())
                        .body(resource));
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/cv/upload")
    public Mono<ResponseEntity<Void>> uploadCv(
            @RequestParam String lang,
            @RequestPart("file") FilePart file) {
        return cvService.uploadCv(lang, file)
                .then(Mono.just(ResponseEntity.ok().<Void>build()));
    }
}
