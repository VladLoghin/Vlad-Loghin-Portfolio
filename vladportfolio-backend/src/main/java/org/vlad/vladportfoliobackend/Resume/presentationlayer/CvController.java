package org.vlad.vladportfoliobackend.Resume.presentationlayer;

import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Resume.servicelayer.CvService;

@RestController
@RequestMapping("/api/public/cv")
public class CvController {
/*
    private final CvService cvService;

    public CvController(CvService cvService) {
        this.cvService = cvService;
    }

    @GetMapping(produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> getCv(@RequestParam(defaultValue = "en") String lang) {
        Resource pdf = cvService.getCv(lang);

        String filename = lang.toLowerCase().startsWith("fr") ? "cv-fr.pdf" : "cv-en.pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .cacheControl(CacheControl.noCache())
                .body(pdf);
    }

 */
}
