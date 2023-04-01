package org.codejudge.sb.repository;

import java.util.List;

import org.codejudge.sb.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    
    List<User> findByUsername(String userName);
}
