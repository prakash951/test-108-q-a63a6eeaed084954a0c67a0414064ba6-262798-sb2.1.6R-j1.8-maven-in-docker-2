package org.codejudge.sb.service;

import java.util.List;
import java.util.Optional;

import org.codejudge.sb.dto.IUserResponse;
import org.codejudge.sb.dto.UserRequest;
import org.codejudge.sb.dto.UserResponse;
import org.codejudge.sb.entity.User;
import org.codejudge.sb.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository mRepository;

    public Optional<IUserResponse> createUser(UserRequest myRequest)
    {
        log.info("Inside create user");
        List<User> users = mRepository.findByUserName(myRequest.getUsername());
        if(users!=null && users.size()>0)
        {
            log.info("User Exists");
            return Optional.empty();
        }
        User user = new User();
        BeanUtils.copyProperties(myRequest, user);
        mRepository.save(user);
        log.info("User Created");
        return Optional.of(UserResponse.builder().username(myRequest.getUsername()).build());
    }
    
}
