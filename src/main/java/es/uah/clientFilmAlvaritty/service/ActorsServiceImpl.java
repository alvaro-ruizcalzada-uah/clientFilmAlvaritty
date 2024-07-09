package es.uah.clientFilmAlvaritty.service;

import es.uah.clientFilmAlvaritty.model.Actor;
import lombok.AllArgsConstructor;
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
public class ActorsServiceImpl implements IActorsService {

    private RestTemplate template;

    private static String URL = "http://localhost:8080/api/actors";

    @Override
    public Page<Actor> findAll(Pageable pageable) {
        Actor[] films = template.getForObject(URL, Actor[].class);
        List<Actor> filmsList = Arrays.asList(films);
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Actor> list;
        if (filmsList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, filmsList.size());
            list = filmsList.subList(startItem, toIndex);
        }
        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), filmsList.size());
    }

    @Override
    public Actor findActorById(Integer idActor) {
        return template.getForObject(URL + "/" + idActor, Actor.class);

    }

    @Override
    public Page<Actor> findActorsByName(String name, Pageable pageable) {
        Actor[] films = template.getForObject(URL + "/name/" + name, Actor[].class);
        List<Actor> list = Arrays.asList(films);
        return new PageImpl<>(list, pageable, list.size());
    }

    @Override
    public void saveActor(Actor film) {
        if (film.getIdActor() != null && film.getIdActor() > 0) {
            template.put(URL, film);
        } else {
            film.setIdActor(0);
            template.postForObject(URL, film, String.class);
        }
    }

    @Override
    public void deleteActor(Integer idActor) {
        template.delete(URL + "/" + idActor);
    }

}