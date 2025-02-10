package com.example.scheletseminar9.service;

import com.example.scheletseminar9.domain.Movie;
import com.example.scheletseminar9.repository.MovieRepository;
import com.example.scheletseminar9.util.event.EntityChangeEvent;
import com.example.scheletseminar9.util.event.EntityChangeEventType;
import com.example.scheletseminar9.util.observer.Observable;
import com.example.scheletseminar9.util.observer.Observer;
import com.example.scheletseminar9.validator.ValidationException;
import com.example.scheletseminar9.validator.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieService implements Observable<EntityChangeEvent> {

    private MovieRepository movieRepository;
    private Validator<Movie> movieValidator;
    private List<Observer<EntityChangeEvent>> observers = new ArrayList<>();

    public MovieService(MovieRepository movieRepository, Validator<Movie> movieValidator) {
        this.movieRepository = movieRepository;
        this.movieValidator = movieValidator;
    }

    public Optional<Movie> update(Movie movie) throws ValidationException {
        movieValidator.validate(movie);
        Optional<Movie> oldMovie = movieRepository.findOne(movie.getId());
        return oldMovie.flatMap(old -> {
            Optional<Movie> updatedMovie = movieRepository.update(movie);
            updatedMovie.ifPresent(m ->
                    notifyObservers(new EntityChangeEvent<>(EntityChangeEventType.UPDATE, m, old)));
            return updatedMovie;
        });
    }

    public Optional<Movie> save(Movie movie) throws ValidationException {
        movieValidator.validate(movie);
        Optional<Movie> savedMovie = movieRepository.save(movie);
        savedMovie.ifPresent(m ->
                notifyObservers(new EntityChangeEvent<>(EntityChangeEventType.ADD, m)));
        return savedMovie;
    }

    public Optional<Movie> delete(Long id) {
        if (id == null) {
            throw new ValidationException("Empty ID");
        }
        Optional<Movie> deletedMovie = movieRepository.delete(id);
        deletedMovie.ifPresent(m ->
            notifyObservers(new EntityChangeEvent<>(EntityChangeEventType.DELETE, null, m)));
        return deletedMovie;
    }

    public Iterable<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public void addObserver(Observer<EntityChangeEvent> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<EntityChangeEvent> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(EntityChangeEvent e) {
        observers.stream().forEach(o -> o.update(e));
    }

    public List<Integer> getYears() {
        return movieRepository.getYears();
    }
}
