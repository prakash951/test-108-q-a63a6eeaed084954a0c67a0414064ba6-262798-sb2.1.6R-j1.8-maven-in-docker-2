package org.codejudge.sb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.codejudge.sb.dto.FriendRequestResponse;
import org.codejudge.sb.dto.FriendRequestsResponse;
import org.codejudge.sb.dto.FriendResponse;
import org.codejudge.sb.dto.IUserResponse;
import org.codejudge.sb.repository.FriendRepository;
import org.codejudge.sb.repository.FriendRequestRepository;
import org.codejudge.sb.repository.UserRepository;
import org.codejudge.sb.entity.Friend;
import org.codejudge.sb.entity.FriendRequest;
import org.codejudge.sb.entity.User;
import org.codejudge.sb.exception.NoFriendRequestsPendingException;
import org.codejudge.sb.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FriendRequestService {

    @Autowired
    UserRepository myUserRepository;

    @Autowired
    FriendRepository myFriendRepository;

    @Autowired
    FriendRequestRepository myFriendRequestRepository;

    @Transactional
    public Optional<IUserResponse> createFriendRequest(String uid1, String uid2) {
        log.info("Inside Create Friend Request");
        List<String> myUsers = new ArrayList<>();
        myUsers.add(uid1);
        myUsers.add(uid2);
        List<User> users = myUserRepository.findAllByUsername(myUsers);
        if (users == null || users.size() < 2) {
            return Optional.empty();
        }
        Long myId1 = users.get(0).getUsername().equals(uid1) ? users.get(0).getId() : users.get(1).getId();
        Long myId2 = users.get(0).getUsername().equals(uid2) ? users.get(0).getId() : users.get(1).getId();
        List<FriendRequest> requests = myFriendRequestRepository.findByRequestorAndReceiver(myId1, myId2);
        if (requests != null && !requests.isEmpty()) {
            return Optional.empty();
        }
        FriendRequest request = FriendRequest.builder().requestor(myId1).receiver(myId2).completed(false).build();
        myFriendRequestRepository.save(request);
        List<FriendRequest> requests1 = myFriendRequestRepository.findByRequestorAndReceiver(myId2, myId1);
        if (requests1 != null && !requests1.isEmpty()) {
            requests1.get(0).setCompleted(true);
            myFriendRequestRepository.save(requests1.get(0));
            request.setCompleted(true);
            myFriendRequestRepository.save(request);
            Friend myFriend1 = Friend.builder().userid(myId1).firendid(myId2).build();
            myFriendRepository.save(myFriend1);
            Friend myFriend2 = Friend.builder().userid(myId2).firendid(myId1).build();
            myFriendRepository.save(myFriend2);
        }

        return Optional.of(FriendRequestResponse.builder().status("success").build());
    }

    public Optional<FriendResponse> getFriendsForUser(String user) {
        List<String> friends = getFriends(user);
        return buildResponse(friends);
    }

    private List<String> getFriends(String user) {
        List<User> users = myUserRepository.findByUsername(user);
        if (users == null || users.isEmpty()) {
            throw new UserNotFoundException("User doesn't exists");
        }
        Long uid = users.get(0).getId();
        List<Friend> requests = myFriendRepository.findByUserid(uid);
        if (requests == null || requests.isEmpty()) {
            throw new NoFriendRequestsPendingException("No pending friend requests");
        }
        List<Long> ids = requests.stream().map(request -> request.getFirendid()).collect(Collectors.toList());
        List<String> friends = new ArrayList<>();
        myUserRepository.findAllById(ids).forEach(record -> friends.add(record.getUsername()));
        return friends;
    }

    private Optional<FriendResponse> buildResponse(List<String> friends) {
        return Optional.of(FriendResponse.builder().friends(friends).build());
    }

    public Optional<FriendRequestsResponse> getFriendRequestForUser(String user) {
        List<User> users = myUserRepository.findByUsername(user);
        if (users == null || users.isEmpty()) {
            throw new UserNotFoundException("User doesn't exists");
        }
        Long uid = users.get(0).getId();
        List<FriendRequest> requests = myFriendRequestRepository.findByReceiverAndCompleted(uid, false);
        if (requests == null || requests.isEmpty()) {
            throw new NoFriendRequestsPendingException("No pending friend requests");
        }
        List<Long> ids = requests.stream().map(request -> request.getRequestor()).collect(Collectors.toList());
        List<String> pendingRequests = new ArrayList<>();
        myUserRepository.findAllById(ids).forEach(record -> pendingRequests.add(record.getUsername()));
        return Optional.of(FriendRequestsResponse.builder().friend_requests(pendingRequests).build());
    }
}
