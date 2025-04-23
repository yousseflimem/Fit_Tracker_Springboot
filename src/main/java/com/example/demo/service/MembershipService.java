package com.example.demo.service;

import com.example.demo.dto.request.MembershipRequest;
import com.example.demo.dto.response.MembershipResponse;
import java.util.List;

public interface MembershipService {
    List<MembershipResponse> getAll();
    MembershipResponse getById(Long id);
    MembershipResponse create(MembershipRequest request);
    MembershipResponse update(Long id, MembershipRequest request);
    void delete(Long id);
}