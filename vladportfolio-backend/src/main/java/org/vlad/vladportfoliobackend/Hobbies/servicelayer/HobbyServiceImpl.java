package org.vlad.vladportfoliobackend.Hobbies.servicelayer;

import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.Hobby;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.HobbyRequestModel;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.HobbyResponseDTO;
import org.vlad.vladportfoliobackend.Hobbies.repositorylayer.HobbyRepository;
import org.vlad.vladportfoliobackend.utils.JsonUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class HobbyServiceImpl implements HobbyService {

    private final HobbyRepository hobbyRepository;

    public HobbyServiceImpl(HobbyRepository hobbyRepository) {
        this.hobbyRepository = hobbyRepository;
    }

    @Override
    public Flux<HobbyResponseDTO> getAllHobbies() {
        return hobbyRepository.findAllByOrderByIdAsc()
                .map(HobbyResponseDTO::from);
    }

    @Override
    public Mono<HobbyResponseDTO> addHobby(HobbyRequestModel hobby) {
        Hobby newHobby = new Hobby();
        newHobby.setHobbyName(hobby.getHobbyName());
        newHobby.setDescription(hobby.getDescription());
        newHobby.setTags(JsonUtils.toJson(hobby.getTags()));
        return hobbyRepository.save(newHobby)
                .map(HobbyResponseDTO::from);
    }

    @Override
    public Mono<HobbyResponseDTO> editHobby(Long id, HobbyRequestModel hobby) {
        return hobbyRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Hobby not found")))
                .flatMap(existingHobby -> {
                    existingHobby.setHobbyName(hobby.getHobbyName());
                    existingHobby.setDescription(hobby.getDescription());
                    existingHobby.setTags(JsonUtils.toJson(hobby.getTags()));
                    return hobbyRepository.save(existingHobby);
                })
                .map(HobbyResponseDTO::from);
    }

    @Override
    public Mono<Void> deleteHobby(Long id) {
        return hobbyRepository.deleteById(id);
    }

    @Override
    public Mono<Void> toggleActive(Long id, boolean active) {
        return hobbyRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Hobby not found")))
                .flatMap(existing -> {
                    existing.setActive(active);
                    return hobbyRepository.save(existing);
                })
                .then();
    }
}
