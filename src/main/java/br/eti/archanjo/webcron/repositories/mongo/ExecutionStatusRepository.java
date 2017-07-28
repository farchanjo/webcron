package br.eti.archanjo.webcron.repositories.mongo;

import br.eti.archanjo.webcron.entities.mongo.ExecutionStatusEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExecutionStatusRepository extends MongoRepository<ExecutionStatusEntity, String> {
}
