package com.example.demo.service.impl;

import com.example.demo.dto.request.MembershipRequest;
import com.example.demo.dto.response.MembershipResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.Membership;
import com.example.demo.repository.MembershipRepository;
import com.example.demo.service.MembershipService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository repository;

    public MembershipServiceImpl(MembershipRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public MembershipResponse getById(Long id) {
        Membership m = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found with id: " + id));
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
    @Transactional
    public MembershipResponse create(MembershipRequest request) {
        Membership m = Membership.builder()
                .name(request.name())
                .description(request.description())
                .type(request.type())
                .price(request.price())
                .duration(request.duration())
                .active(request.active() != null ? request.active() : true)
                .build();

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
    @Transactional
    public MembershipResponse update(Long id, MembershipRequest request) {
        Membership m = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found with id: " + id));

        m.setName(request.name());
        m.setDescription(request.description());
        m.setType(request.type());
        m.setPrice(request.price());
        m.setDuration(request.duration());
        m.setActive(request.active() != null ? request.active() : true);

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
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Membership not found with id: " + id);
        }
        if (repository.isMembershipAssigned(id)) {
            throw new IllegalStateException("Cannot delete membership assigned to a user");
        }
        repository.deleteById(id);
    }
}