package br.eti.archanjo.webcron.repositories.mysql;

import br.eti.archanjo.webcron.entities.mysql.UserEntity;
import br.eti.archanjo.webcron.enums.Status;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByUsernameAndPasswordAndStatus(String username, String password, Status status);

    UserEntity findByUsernameOrEmailAndPasswordAndStatus(String username, String email, String password, Status status);
}