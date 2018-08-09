package ru.dz.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.dz.model.User;

import java.util.Optional;

public interface UserRepositories extends JpaRepository<User, Long> {
    Optional<User> findOneByUserIp(String userIp);
}
