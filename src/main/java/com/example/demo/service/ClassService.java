package com.example.demo.service;

import com.example.demo.dto.request.ClassRequest;
import com.example.demo.dto.response.ClassResponse;
import org.springframework.data.domain.Page;

public interface ClassService {
    Page<ClassResponse> searchClasses(String keyword, int page, int size);
    ClassResponse getClass(Long id);
    ClassResponse createClass(ClassRequest classRequest);
    ClassResponse updateClass(Long id, ClassRequest classRequest);
    void deleteClass(Long id);
}
