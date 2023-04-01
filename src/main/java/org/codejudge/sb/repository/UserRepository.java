package org.codejudge.sb.repository;

import java.util.List;

import org.codejudge.sb.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByUsername(String userName);

    @Query("select u from User u where u.username in :users")
    List<User> findAllByUsername(@Param("users") List<String> users);
}
