// src/main/java/com/example/demo/repository/MembershipRepository.java
package com.example.demo.repository;

import com.example.demo.model.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    // you might add findByType if needed
    // Optional<Membership> findByType(MembershipType type);
}
