package org.vlad.vladportfoliobackend.Resume.repositorylayer;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vlad.vladportfoliobackend.Resume.datalayer.CvDocument;
import reactor.core.publisher.Mono;

public interface CvDocumentRepository extends ReactiveCrudRepository<CvDocument, Long> {
    Mono<CvDocument> findByLanguage(String language);
}
