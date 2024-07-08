package es.uah.clientFilmAlvaritty.service;

import es.uah.clientFilmAlvaritty.model.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IFilmsService {

    Page<Film> findAll(Pageable pageable);
    Film findFilmById(Integer idFilm);
    Page<Film> findFilmsByTitle(String title, Pageable pageable);
    Page<Film> findFilmsByGenre(String genre, Pageable pageable);
    void saveFilm(Film film);
    void deleteFilm(Integer idFilm);

}
