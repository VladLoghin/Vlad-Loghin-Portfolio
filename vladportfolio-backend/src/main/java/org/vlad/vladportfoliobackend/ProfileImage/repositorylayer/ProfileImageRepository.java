package org.vlad.vladportfoliobackend.ProfileImage.repositorylayer;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vlad.vladportfoliobackend.ProfileImage.datalayer.ProfileImage;
import reactor.core.publisher.Mono;

public interface ProfileImageRepository extends ReactiveCrudRepository<ProfileImage, Long> {
    Mono<ProfileImage> findFirstByOrderByUploadedAtDesc();
}
