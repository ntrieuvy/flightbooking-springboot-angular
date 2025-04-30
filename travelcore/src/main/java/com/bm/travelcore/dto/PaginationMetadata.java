package com.bm.travelcore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationMetadata {
    private int currentPage;
    private int pageSize;
    private int totalItems;
    private int totalPages;

    public PaginationMetadata(int currentPage, int pageSize, int totalItems) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
    }
}
