package org.vlad.vladportfoliobackend.Emailing.presentationlayer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Emailing.datalayer.EmailRequestModel;
import org.vlad.vladportfoliobackend.Emailing.servicelayer.ContactEmailService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/public/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactEmailService contactEmailService;

    @PostMapping
    public Mono<ResponseEntity<Void>> sendContactEmail(@Valid @RequestBody EmailRequestModel request) {
        return contactEmailService.sendContactEmail(request)
                .then(Mono.just(ResponseEntity.ok().<Void>build()));
    }
}
