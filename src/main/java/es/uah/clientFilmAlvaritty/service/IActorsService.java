package es.uah.clientFilmAlvaritty.service;

import es.uah.clientFilmAlvaritty.model.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IActorsService {

    Page<Actor> findAll(Pageable pageable);
    Actor findActorById(Integer idActor);
    Page<Actor> findActorsByName(String name, Pageable pageable);
    void saveActor(Actor actor);
    void deleteActor(Integer idActor);

}
