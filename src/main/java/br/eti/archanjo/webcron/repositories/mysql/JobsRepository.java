package br.eti.archanjo.webcron.repositories.mysql;

import br.eti.archanjo.webcron.entities.mysql.JobsEntity;
import br.eti.archanjo.webcron.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface JobsRepository extends CrudRepository<JobsEntity, Long> {
    Page<JobsEntity> findAllByUserIdOrderByIdDesc(Long userId, Pageable page);

    JobsEntity findByIdAndStatus(Long id, Status status);
}