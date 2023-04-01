package org.codejudge.sb.repository;

import org.codejudge.sb.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FriendRepository extends JpaRepository<Friend,Long>{
    
    List<Friend> findByUserid(Long userid);
}
