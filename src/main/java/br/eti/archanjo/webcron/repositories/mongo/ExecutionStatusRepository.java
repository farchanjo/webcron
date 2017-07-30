package br.eti.archanjo.webcron.repositories.mongo;

import br.eti.archanjo.webcron.entities.mongo.ExecutionStatusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExecutionStatusRepository extends MongoRepository<ExecutionStatusEntity, String> {
    Page<ExecutionStatusEntity> findAllByJobUserIdOrderByCreatedDesc(Long id, Pageable page);
    Page<ExecutionStatusEntity> findAllByOrderByCreatedDesc(Pageable page);
    Page<ExecutionStatusEntity> findBy(TextCriteria criteria, Pageable page);
}
