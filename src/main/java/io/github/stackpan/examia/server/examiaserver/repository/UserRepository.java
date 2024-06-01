package io.github.stackpan.examia.server.examiaserver.repository;

import io.github.stackpan.examia.server.examiaserver.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findByUsername(String username);

}
