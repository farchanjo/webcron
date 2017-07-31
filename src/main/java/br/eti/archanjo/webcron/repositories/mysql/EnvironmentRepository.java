package br.eti.archanjo.webcron.repositories.mysql;

import br.eti.archanjo.webcron.entities.mysql.EnvironmentEntity;
import org.springframework.data.repository.CrudRepository;

public interface EnvironmentRepository extends CrudRepository<EnvironmentEntity, Long> {
}