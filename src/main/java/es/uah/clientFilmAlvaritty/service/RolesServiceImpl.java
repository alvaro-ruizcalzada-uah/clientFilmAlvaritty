package es.uah.clientFilmAlvaritty.service;

import es.uah.clientFilmAlvaritty.model.Rol;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Service
public class RolesServiceImpl implements IRolesService {

    private RestTemplate template;

    private final String url = "http://localhost:8090/api/usuarios/rols";

    @Override
    public List<Rol> findAll() {
        Rol[] roles = template.getForObject(url, Rol[].class);
        return Arrays.asList(roles);
    }

    @Override
    public Rol findRolById(Integer idRol) {
        return template.getForObject(url+"/"+idRol, Rol.class);
    }

    @Override
    public void saveRol(Rol rol) {
        if (rol.getIdRol() != null && rol.getIdRol() > 0) {
            template.put(url, rol);
        } else {
            rol.setIdRol(0);
            template.postForObject(url, rol, String.class);
        }
    }

    @Override
    public void deleteRol(Integer idRol) {
        template.delete(url + "/" + idRol);
    }
}
