package org.codejudge.sb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.codejudge.sb.dto.FriendRequestResponse;
import org.codejudge.sb.dto.IUserResponse;
import org.codejudge.sb.repository.FriendRepository;
import org.codejudge.sb.repository.FriendRequestRepository;
import org.codejudge.sb.repository.UserRepository;
import org.codejudge.sb.entity.Friend;
import org.codejudge.sb.entity.FriendRequest;
import org.codejudge.sb.entity.User;
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
        Long myId1 = users.get(0).getId();
        Long myId2 = users.get(1).getId();
        List<FriendRequest> requests = myFriendRequestRepository.findByRequestorAndReceiver(myId1, myId2);
        if (requests != null && requests.size() > 1) {
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

}
