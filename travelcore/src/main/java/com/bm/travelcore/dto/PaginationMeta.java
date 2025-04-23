package com.bm.travelcore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationMeta {
    private int currentPage;
    private int pageSize;
    private long totalItems;
    private int totalPages;

    public PaginationMeta(int currentPage, int pageSize, long totalItems, int totalPages) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }
}