package com.example.demo.repository;

import com.example.demo.model.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
//    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.membership.id = :membershipId")
//    boolean isMembershipAssigned(Long membershipId);
}