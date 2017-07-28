package br.eti.archanjo.webcron.repositories.mongo;

import br.eti.archanjo.webcron.entities.mongo.ExecutionStatusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExecutionStatusRepository extends MongoRepository<ExecutionStatusEntity, String> {
    Page<ExecutionStatusEntity> findAllByJobUserId(Long id, Pageable page);
}
