package org.vlad.vladportfoliobackend.Hobbies.presentationlayer;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.HobbyRequestModel;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.HobbyResponseDTO;
import org.vlad.vladportfoliobackend.Hobbies.servicelayer.HobbyService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/hobbies")
public class HobbyController {

    private final HobbyService hobbyService;

    public HobbyController(HobbyService hobbyService) {
        this.hobbyService = hobbyService;
    }

    @GetMapping()
    public Flux<HobbyResponseDTO> getAllHobbies() {
        return hobbyService.getAllHobbies();
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping()
    public Mono<HobbyResponseDTO> addHobby(@RequestBody HobbyRequestModel hobby) {
        return hobbyService.addHobby(hobby);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/{id}")
    public Mono<HobbyResponseDTO> editHobby(@PathVariable Long id, @RequestBody HobbyRequestModel hobby) {
        return hobbyService.editHobby(id, hobby);
    }

    @PreAuthorize("hasRole('Admin')")
    @PatchMapping("/{id}/active")
    public Mono<Void> toggleActive(@PathVariable Long id, @RequestParam boolean active) {
        return hobbyService.toggleActive(id, active);
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteHobby(@PathVariable Long id) {
        return hobbyService.deleteHobby(id);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/reorder")
    public Mono<Void> reorder(@RequestBody List<Long> orderedIds) {
        return hobbyService.reorder(orderedIds);
    }
}
