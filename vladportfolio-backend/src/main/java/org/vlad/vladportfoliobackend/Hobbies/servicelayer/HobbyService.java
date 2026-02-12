package org.vlad.vladportfoliobackend.Hobbies.servicelayer;

import org.vlad.vladportfoliobackend.Hobbies.datalayer.HobbyRequestModel;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.HobbyResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HobbyService {

    Flux<HobbyResponseDTO> getAllHobbies();

    Mono<HobbyResponseDTO> addHobby(HobbyRequestModel hobby);

    Mono<HobbyResponseDTO> editHobby(Long id, HobbyRequestModel hobby);

    Mono<Void> deleteHobby(Long id);

    Mono<Void> toggleActive(Long id, boolean active);
}
