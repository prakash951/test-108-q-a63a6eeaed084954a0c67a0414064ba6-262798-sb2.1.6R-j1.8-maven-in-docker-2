package org.codejudge.sb.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.codejudge.sb.dto.SuggestionResponse;
import org.codejudge.sb.entity.Friend;
import org.codejudge.sb.entity.User;
import org.codejudge.sb.exception.NoFriendRequestsPendingException;
import org.codejudge.sb.exception.UserNotFoundException;
import org.codejudge.sb.repository.FriendRepository;
import org.codejudge.sb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SuggestionService {

    @Autowired
    UserRepository myUserRepository;

    @Autowired
    FriendRepository myFriendRepository;


    public Optional<SuggestionResponse> getFriendsSuggestionsForUser(String user) {
        List<User> users = myUserRepository.findByUsername(user);
        if (users == null || users.size() == 0) {
            throw new UserNotFoundException("User doesn't exists");
        }
        Long uid = users.get(0).getId();
        List<Friend> requests = myFriendRepository.findByUserid(uid);
        log.info("Response for user: " + user + "  friends: " + requests);
        if (requests == null || requests.size() == 0) {
            throw new NoFriendRequestsPendingException("No pending friend requests");
        }
        List<Long> suggestions = new ArrayList<>();
        requests.stream().map(request -> suggestions.addAll(getSuggestions(request.getFirendid(), 1)))
                .collect(Collectors.toList());
        if (suggestions.size() == 0) {
            throw new NoFriendRequestsPendingException("No pending friend requests");
        }
        log.info("suggestions:" + suggestions);
        List<String> response = new ArrayList<>();
        myUserRepository.findAllById(suggestions).forEach(record -> response.add(record.getUsername()));
        return Optional.of(SuggestionResponse.builder().suggestions(response).build());
    }

    private Set<Long> getSuggestions(long uid, int level) {
        if (level == 4)
            return new HashSet<>();
        List<Friend> requests = myFriendRepository.findByUserid(uid);
        log.info("Response for user: " + uid + " at level:" + level + " friends: " + requests);
        if (requests == null || requests.size() == 0) {
            return new HashSet<>();
        }
        List<Long> friends = new ArrayList<>();
        List<Long> ids = requests.stream().map(request -> {
            friends.add(request.getFirendid());
            return request.getFirendid();
        }).collect(Collectors.toList());
        ids.stream().map(x -> friends.addAll(getSuggestions(x,level+1)));
        
        log.info("Friends for user: " + uid + " at level:" + level + " : " + friends);
        return new HashSet<>(friends);
    }
    
}
