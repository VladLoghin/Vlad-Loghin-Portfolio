package org.vlad.vladportfoliobackend.Hobbies.servicelayer;

import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.Hobby;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.HobbyRequestModel;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.HobbyResponseDTO;
import org.vlad.vladportfoliobackend.Hobbies.repositorylayer.HobbyRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class HobbyServiceImpl implements HobbyService {

    HobbyRepository hobbyRepository;

    public HobbyServiceImpl(HobbyRepository hobbyRepository) {
        this.hobbyRepository = hobbyRepository;
    }

    @Override
    public List<HobbyResponseDTO> getAllHobbies() {
        return hobbyRepository.findAll().stream()
                .map(HobbyResponseDTO::from)
                .toList();
    }

    @Override
    public HobbyResponseDTO addHobby(HobbyRequestModel hobby) {
        Hobby newHobby = new Hobby();
        newHobby.setHobbyName(hobby.getHobbyName());
        newHobby.setDescription(hobby.getDescription());

        // IMPORTANT: always initialize the collection
        newHobby.setTags(
                hobby.getTags() != null ? hobby.getTags() : new ArrayList<>()
        );

        return HobbyResponseDTO.from(hobbyRepository.save(newHobby));
    }


    @Override
    public HobbyResponseDTO editHobby(Long id, HobbyRequestModel hobby) {
        Hobby existingHobby = hobbyRepository.findById(id).orElseThrow(() -> new RuntimeException("Hobby not found"));
        existingHobby.setHobbyName(hobby.getHobbyName());
        existingHobby.setDescription(hobby.getDescription());
        existingHobby.setTags(hobby.getTags());
        return HobbyResponseDTO.from(hobbyRepository.save(existingHobby));
    }

    @Override
    public void deleteHobby(Long id) {
        hobbyRepository.deleteById(id);
    }
}
