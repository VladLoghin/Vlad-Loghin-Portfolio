package org.vlad.vladportfoliobackend.Emailing.presentationlayer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Emailing.datalayer.EmailRequestModel;
import org.vlad.vladportfoliobackend.Emailing.servicelayer.ContactEmailService;
import org.vlad.vladportfoliobackend.Emailing.servicelayer.InvalidEmailDomainException;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/public/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactEmailService contactEmailService;

    @PostMapping
    public Mono<ResponseEntity<Object>> sendContactEmail(@Valid @RequestBody EmailRequestModel request) {
        return contactEmailService.sendContactEmail(request)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(InvalidEmailDomainException.class, ex ->
                        Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Map.of("error", ex.getMessage()))));
    }
}
