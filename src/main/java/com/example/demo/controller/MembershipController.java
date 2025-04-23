package com.example.demo.controller;

import com.example.demo.dto.request.MembershipRequest;
import com.example.demo.dto.response.MembershipResponse;
import com.example.demo.service.MembershipService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    private final MembershipService service;

    public MembershipController(MembershipService service) {
        this.service = service;
    }

    @GetMapping
    public List<MembershipResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public MembershipResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public MembershipResponse create(@RequestBody MembershipRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public MembershipResponse update(@PathVariable Long id, @RequestBody MembershipRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}