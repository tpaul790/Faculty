package com.example.scheletseminar9.util.paging;

import com.example.scheletseminar9.domain.Entity;
import com.example.scheletseminar9.domain.Page;
import com.example.scheletseminar9.domain.Pageable;
import com.example.scheletseminar9.repository.Repository;

import java.util.Map;

public interface PagingRepository<ID,E extends Entity<ID>> extends Repository<ID, E> {
    Page<E> findAllOnPage(Pageable pageable);
}
