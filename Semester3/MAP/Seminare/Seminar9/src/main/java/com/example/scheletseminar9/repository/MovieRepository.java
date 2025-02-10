package com.example.scheletseminar9.repository;

import com.example.scheletseminar9.domain.Movie;
import com.example.scheletseminar9.domain.Page;
import com.example.scheletseminar9.domain.Pageable;
import com.example.scheletseminar9.dto.MovieFilterDTO;
import com.example.scheletseminar9.util.paging.PagingRepository;

import java.util.List;

public interface MovieRepository extends PagingRepository<Long, Movie> {
    List<Integer> getYears();

    public Page<Movie> findAllOnPage(Pageable pageable, MovieFilterDTO movieFilterDTO);
}
