package io.github.stackpan.examia.server.examiaserver.repository;

import io.github.stackpan.examia.server.examiaserver.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

}
