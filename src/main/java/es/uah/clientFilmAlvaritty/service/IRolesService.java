package es.uah.clientFilmAlvaritty.service;

import es.uah.clientFilmAlvaritty.model.Rol;

import java.util.List;

public interface IRolesService {

    List<Rol> findAll();

    Rol findRolById(Integer idRol);

    void saveRol(Rol rol);

    void deleteRol(Integer idRol);

}
