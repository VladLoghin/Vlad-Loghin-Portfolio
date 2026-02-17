package org.vlad.vladportfoliobackend.Hobbies.servicelayer;

import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.Hobby;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.HobbyRequestModel;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.HobbyResponseDTO;
import org.vlad.vladportfoliobackend.Hobbies.repositorylayer.HobbyRepository;
import org.vlad.vladportfoliobackend.utils.JsonUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class HobbyServiceImpl implements HobbyService {

    private final HobbyRepository hobbyRepository;

    public HobbyServiceImpl(HobbyRepository hobbyRepository) {
        this.hobbyRepository = hobbyRepository;
    }

    @Override
    public Flux<HobbyResponseDTO> getAllHobbies() {
        return hobbyRepository.findAllByOrderByDisplayOrderAscIdAsc()
                .map(HobbyResponseDTO::from);
    }

    @Override
    public Mono<HobbyResponseDTO> addHobby(HobbyRequestModel hobby) {
        return hobbyRepository.findAllByOrderByDisplayOrderAscIdAsc()
                .map(Hobby::getDisplayOrder)
                .defaultIfEmpty(0)
                .reduce(Math::max)
                .flatMap(maxOrder -> {
                    Hobby newHobby = new Hobby();
                    newHobby.setHobbyName(hobby.getHobbyName());
                    newHobby.setDescription(hobby.getDescription());
                    newHobby.setTags(JsonUtils.toJson(hobby.getTags()));
                    newHobby.setDisplayOrder(maxOrder + 1);
                    return hobbyRepository.save(newHobby);
                })
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

    @Override
    public Mono<Void> reorder(List<Long> orderedIds) {
        return Flux.fromIterable(orderedIds)
                .index()
                .flatMap(tuple -> hobbyRepository.findById(tuple.getT2())
                        .flatMap(h -> {
                            h.setDisplayOrder(tuple.getT1().intValue());
                            return hobbyRepository.save(h);
                        }))
                .then();
    }
}
