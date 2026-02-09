package org.vlad.vladportfoliobackend.Hobbies.presentationlayer;

import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.HobbyRequestModel;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.HobbyResponseDTO;
import org.vlad.vladportfoliobackend.Hobbies.servicelayer.HobbyService;

import java.util.List;

@RestController
@RequestMapping("/api/hobbies")
public class HobbyController {

    private final HobbyService hobbyService;

    public HobbyController(HobbyService hobbyService) {
        this.hobbyService = hobbyService;
    }

    @GetMapping()
    public List<HobbyResponseDTO> getAllHobbies() {
        return hobbyService.getAllHobbies();
    }

    @PostMapping()
    public HobbyResponseDTO addHobby(@RequestBody HobbyRequestModel hobby) {
        return hobbyService.addHobby(hobby);
    }

    @PutMapping("/{id}")
    public HobbyResponseDTO editHobby(@PathVariable Long id, @RequestBody HobbyRequestModel hobby) {
        return hobbyService.editHobby(id, hobby);
    }

    @DeleteMapping("/{id}")
    public void deleteHobby(@PathVariable Long id) {
        hobbyService.deleteHobby(id);
    }
}