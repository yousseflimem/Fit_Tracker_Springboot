package com.example.demo.controller;

import com.example.demo.entity.Membership;
import com.example.demo.repository.MembershipRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    private final MembershipRepository membershipRepository;

    public MembershipController(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    @GetMapping
    public List<Membership> getAllMemberships() {
        return membershipRepository.findAll();
    }

    @PostMapping
    public Membership createMembership(@RequestBody Membership membership) {
        return membershipRepository.save(membership);
    }

    @PutMapping("/{id}")
    public Membership updateMembership(@PathVariable Long id, @RequestBody Membership membership) {
        membership.setId(id);
        return membershipRepository.save(membership);
    }

    @DeleteMapping("/{id}")
    public void deleteMembership(@PathVariable Long id) {
        membershipRepository.deleteById(id);
    }
}
