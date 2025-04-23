package com.example.demo.service.impl;

import com.example.demo.dto.request.MembershipRequest;
import com.example.demo.dto.response.MembershipResponse;
import com.example.demo.model.entity.Membership;
import com.example.demo.repository.MembershipRepository;
import com.example.demo.service.MembershipService;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository repository;

    public MembershipServiceImpl(MembershipRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MembershipResponse> getAll() {
        return repository.findAll().stream()
                .map(m -> new MembershipResponse(
                        m.getId(),
                        m.getType(),
                        m.getPrice(),
                        m.getDuration()
                ))
                .toList();
    }

    @Override
    public MembershipResponse getById(Long id) {
        Membership m = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found"));
        return new MembershipResponse(m.getId(), m.getType(), m.getPrice(), m.getDuration());
    }

    @Override
    public MembershipResponse create(MembershipRequest request) {
        Membership m = new Membership();
        m.setType(request.type());
        m.setPrice(request.price());
        m.setDuration(request.duration());
        repository.save(m);
        return new MembershipResponse(m.getId(), m.getType(), m.getPrice(), m.getDuration());
    }

    @Override
    public MembershipResponse update(Long id, MembershipRequest request) {
        Membership m = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found"));
        m.setType(request.type());
        m.setPrice(request.price());
        m.setDuration(request.duration());
        repository.save(m);
        return new MembershipResponse(m.getId(), m.getType(), m.getPrice(), m.getDuration());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}