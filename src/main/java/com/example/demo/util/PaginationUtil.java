package com.example.demo.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PaginationUtil {

    /** Simple no-sort page request */
    public static PageRequest createPageRequest(int page, int size) {
        return PageRequest.of(page, size);
    }

    /** Page request sorted by the given property (ascending) */
    public static PageRequest createPageRequest(int page, int size, String sortBy) {
        return PageRequest.of(page, size, Sort.by(Sort.Order.asc(sortBy)));
    }
}
