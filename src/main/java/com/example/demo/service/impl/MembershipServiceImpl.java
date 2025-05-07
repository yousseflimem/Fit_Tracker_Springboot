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
                        m.getName(),
                        m.getDescription(),
                        m.getType(),
                        m.getPrice(),
                        m.getDuration(),
                        m.isActive(),
                        m.getCreatedAt(),
                        m.getUpdatedAt()
                ))
                .toList();
    }

    @Override
    public MembershipResponse getById(Long id) {
        Membership m = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found"));
        return new MembershipResponse(
                m.getId(),
                m.getName(),
                m.getDescription(),
                m.getType(),
                m.getPrice(),
                m.getDuration(),
                m.isActive(),
                m.getCreatedAt(),
                m.getUpdatedAt()
        );
    }

    @Override
    public MembershipResponse create(MembershipRequest request) {
        Membership m = new Membership();
        m.setName(request.name());
        m.setDescription(request.description());
        m.setType(request.type());
        m.setPrice(request.price());
        m.setDuration(request.duration());
        if (request.active() != null) {
            m.setActive(request.active());
        }
        repository.save(m);
        return new MembershipResponse(
                m.getId(),
                m.getName(),
                m.getDescription(),
                m.getType(),
                m.getPrice(),
                m.getDuration(),
                m.isActive(),
                m.getCreatedAt(),
                m.getUpdatedAt()
        );
    }

    @Override
    public MembershipResponse update(Long id, MembershipRequest request) {
        Membership m = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found"));

        if (request.name() != null) {
            m.setName(request.name());
        }
        if (request.description() != null) {
            m.setDescription(request.description());
        }
        if (request.type() != null) {
            m.setType(request.type());
        }
        if (request.price() != null) {
            m.setPrice(request.price());
        }
        if (request.duration() != null) {
            m.setDuration(request.duration());
        }
        if (request.active() != null) {
            m.setActive(request.active());
        }

        repository.save(m);
        return new MembershipResponse(
                m.getId(),
                m.getName(),
                m.getDescription(),
                m.getType(),
                m.getPrice(),
                m.getDuration(),
                m.isActive(),
                m.getCreatedAt(),
                m.getUpdatedAt()
        );
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
