package es.uah.clientFilmAlvaritty.service;

import es.uah.clientFilmAlvaritty.model.Usuario;
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
public class UsuariosServiceImpl implements IUsuariosService {

    RestTemplate template;

    private final String url = "http://localhost:8090/api/usuarios/usuarios";

    @Override
    public Page<Usuario> findAll(Pageable pageable) {
        Usuario[] users = template.getForObject(url, Usuario[].class);
        List<Usuario> usersList = Arrays.asList(users);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Usuario> list;

        if (usersList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, usersList.size());
            list = usersList.subList(startItem, toIndex);
        }

        Page<Usuario> page = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), usersList.size());
        return page;
    }

    @Override
    public Usuario findUserById(Integer idUsuario) {
        return template.getForObject(url+"/"+idUsuario, Usuario.class);

    }

    @Override
    public Usuario findUserByName(String name) {
        return template.getForObject(url+"/name/"+name, Usuario.class);

    }

    @Override
    public Usuario findUserByEmail(String email) {
        return template.getForObject(url+"/email/"+email, Usuario.class);
    }

    @Override
    public Usuario login(String email, String password) {
        return template.getForObject(url+"/login/"+email+"/"+password, Usuario.class);

    }

    @Override
    public void saveUser(Usuario user) {
        if (user.getIdUsuario() != null && user.getIdUsuario() > 0) {
            template.put(url, user);
        } else {
            user.setIdUsuario(0);
            template.postForObject(url, user, String.class);
        }
    }

    @Override
    public void deleteUser(Integer idUsuario) {
        template.delete(url+"/"+idUsuario);
    }

 }
