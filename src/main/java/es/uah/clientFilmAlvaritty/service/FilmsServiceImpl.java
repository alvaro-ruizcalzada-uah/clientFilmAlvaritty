package es.uah.clientFilmAlvaritty.service;

import es.uah.clientFilmAlvaritty.model.Film;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
public class FilmsServiceImpl implements IFilmsService{

    private RestTemplate template;

    private static String URL="http://localhost:8080/api/films";

    @Override
    public Page<Film> findAll(Pageable pageable) {
        Film[] films = template.getForObject(URL, Film[].class);
        List<Film> filmsList = Arrays.asList(films);
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Film> list;
        if (filmsList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, filmsList.size());
            list = filmsList.subList(startItem, toIndex);
        }
        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), filmsList.size());
    }

    @Override
    public Film findFilmById(Integer idFilm) {
        return template.getForObject(URL + "/" + idFilm, Film.class);

    }

    @Override
    public Page<Film> findFilmsByTitle(String title, Pageable pageable) {
        Film[] films = template.getForObject(URL + "/title/" + title, Film[].class);
        List<Film> list = Arrays.asList(films);
        return new PageImpl<>(list, pageable, list.size());
    }

    @Override
    public Page<Film> findFilmsByGenre(String genre, Pageable pageable) {
        Film[] films = template.getForObject(URL + "/genre/" + genre, Film[].class);
        List<Film> list = Arrays.asList(films);
        return new PageImpl<>(list, pageable, list.size());
    }

    @Override
    public void saveFilm(Film film) {
        if (film.getIdFilm() != null && film.getIdFilm() > 0) {
            template.put(URL, film);
        } else {
            film.setIdFilm(0);
            template.postForObject(URL, film, String.class);
        }
    }

    @Override
    public void deleteFilm(Integer idFilm) {
        template.delete(URL + "/" + idFilm);
    }

}
