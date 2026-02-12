package org.vlad.vladportfoliobackend.Hobbies.presentationlayer;

import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.HobbyRequestModel;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.HobbyResponseDTO;
import org.vlad.vladportfoliobackend.Hobbies.servicelayer.HobbyService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @PostMapping()
    public Mono<HobbyResponseDTO> addHobby(@RequestBody HobbyRequestModel hobby) {
        return hobbyService.addHobby(hobby);
    }

    @PutMapping("/{id}")
    public Mono<HobbyResponseDTO> editHobby(@PathVariable Long id, @RequestBody HobbyRequestModel hobby) {
        return hobbyService.editHobby(id, hobby);
    }

    @PatchMapping("/{id}/active")
    public Mono<Void> toggleActive(@PathVariable Long id, @RequestParam boolean active) {
        return hobbyService.toggleActive(id, active);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteHobby(@PathVariable Long id) {
        return hobbyService.deleteHobby(id);
    }
}
