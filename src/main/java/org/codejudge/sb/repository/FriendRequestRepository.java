package org.codejudge.sb.repository;

import org.codejudge.sb.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest,Long> {

    List<FriendRequest> findByRequestorAndReceiver(Long requestor,Long receiver);

    List<FriendRequest> findByReceiverAndCompleted(Long receiver,boolean completed);
}
