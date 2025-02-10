package com.example.socialnetworkgui.utils.paging;

import com.example.socialnetworkgui.domain.Entity;
import com.example.socialnetworkgui.domain.Page;
import com.example.socialnetworkgui.domain.Pageable;
import com.example.socialnetworkgui.domain.Tuple;
import com.example.socialnetworkgui.repository.Repository2;

public interface PagingRepository<ID,E extends Entity<ID>> extends Repository2<ID,E> {
    Page<E> findallOnPage(int userId, Pageable pageable);
}
