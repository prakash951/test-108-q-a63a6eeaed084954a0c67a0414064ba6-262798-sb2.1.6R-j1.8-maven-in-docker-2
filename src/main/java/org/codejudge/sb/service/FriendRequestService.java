package org.codejudge.sb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.codejudge.sb.dto.FriendRequestResponse;
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

@Service
public class FriendRequestService {

    @Autowired
    UserRepository myUserRepository;

    @Autowired
    FriendRepository myFriendRepository;

    @Autowired
    FriendRequestRepository myFriendRequestRepository;

    @Transactional
    public Optional<IUserResponse> createFriendRequest(String uid1, String uid2) {
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
        if (requests != null && requests.size() > 0) {
            return Optional.empty();
        }
        FriendRequest request = FriendRequest.builder().requestor(myId1).receiver(myId2).completed(false).build();
        myFriendRequestRepository.save(request);
        List<FriendRequest> requests1 = myFriendRequestRepository.findByRequestorAndReceiver(myId2, myId1);
        if (requests1 != null && requests1.size() > 0) {
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

    public List<String> getFriendRequestForUser(String user) {
        List<User> users = myUserRepository.findByUsername(user);
        if (users == null || users.size() == 0) {
            throw new UserNotFoundException("User doesn't exists");
        }
        Long uid = users.get(0).getId();
        List<FriendRequest> requests = myFriendRequestRepository.findByReceiverAndcompleted(uid, false);
        if (requests == null || requests.size() == 0) {
            throw new NoFriendRequestsPendingException("No pending friend requests");
        }
        List<Long> ids = requests.stream().map(request -> request.getRequestor()).collect(Collectors.toList());
        List<String> pendingRequests = new ArrayList<>();
        myUserRepository.findAllById(ids).forEach(record -> pendingRequests.add(record.getUsername()));
        return pendingRequests;
    }

}
