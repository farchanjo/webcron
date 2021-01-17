package br.eti.archanjo.webcron.repositories.mysql;

import br.eti.archanjo.webcron.entities.mysql.JobsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface JobsRepository extends CrudRepository<JobsEntity, Long> {
    Page<JobsEntity> findAllByOrderByIdDesc(Pageable page);
}