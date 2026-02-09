package org.vlad.vladportfoliobackend.Resume.servicelayer;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CvService {

    /*
    private final ResourceLoader resourceLoader;

    public CvService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Resource getCv(String lang) {
        String normalized = (lang == null) ? "en" : lang.toLowerCase();

        // If you renamed files:
        // String path = normalized.startsWith("fr") ? "classpath:cv/cv-fr.pdf" : "classpath:cv/cv-en.pdf";

        // If you KEEP your current filenames (with spaces):
        String path = normalized.startsWith("fr")
                ? "classpath:cv/CV French.pdf"
                : "classpath:cv/CV English.pdf";

        Resource resource = resourceLoader.getResource(path);

        if (!resource.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CV file not found: " + path);
        }

        return resource;
    }

     */
}
