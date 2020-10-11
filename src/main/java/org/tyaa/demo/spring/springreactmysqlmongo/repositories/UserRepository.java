package org.tyaa.demo.spring.springreactmysqlmongo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tyaa.demo.spring.springreactmysqlmongo.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findUserByName(String name);
}
