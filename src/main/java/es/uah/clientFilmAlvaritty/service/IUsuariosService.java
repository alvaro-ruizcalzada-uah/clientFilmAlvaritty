package es.uah.clientFilmAlvaritty.service;

import es.uah.clientFilmAlvaritty.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUsuariosService {

    Page<Usuario> findAll(Pageable pageable);

    Usuario findUserById(Integer idUsuario);

    Usuario findUserByName(String name);

    Usuario findUserByEmail(String email);

    Usuario login(String email, String password);

    void saveUser(Usuario user);

    void deleteUser(Integer idUsuario);

}
