package es.uah.clientFilmAlvaritty.controller;

import es.uah.clientFilmAlvaritty.model.Rol;
import es.uah.clientFilmAlvaritty.model.Usuario;
import es.uah.clientFilmAlvaritty.paginator.PageRender;
import es.uah.clientFilmAlvaritty.service.IRolesService;
import es.uah.clientFilmAlvaritty.service.IUsuariosService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/cusuarios")
@AllArgsConstructor
public class UsuariosController {

    private IUsuariosService usuariosService;

    private IRolesService rolesService;

    @GetMapping(value = "/ver/{id}")
    public String ver(Model model, @PathVariable("id") Integer id, RedirectAttributes attributes) {
        Usuario usuario = usuariosService.findUserById(id);
        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Detalle del usuario: " + usuario.getNombre());
        return "usuarios/verUsuario";
    }

    @GetMapping("/listado")
    public String listadoUsuarios(Model model, @RequestParam(name="page", defaultValue="0") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Usuario> listado = usuariosService.findAll(pageable);
        PageRender<Usuario> pageRender = new PageRender<Usuario>("/cusuarios/listado", listado);
        model.addAttribute("titulo", "Listado de todos los usuarios");
        model.addAttribute("listadoUsuarios", listado);
        model.addAttribute("page", pageRender);
        return "usuarios/listUsuario";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        List<Rol> roles = rolesService.findAll();
        model.addAttribute("titulo", "Nuevo usuario");
        model.addAttribute("allRoles", roles);
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);
        return "usuarios/formUsuario";
    }

    @PostMapping("/guardar/")
    public String guardarUsuario(Model model, Usuario usuario, RedirectAttributes attributes) {
        //si existe un usuario con el mismo correo no lo guardamos
        if (usuariosService.findUserByEmail(usuario.getCorreo())!=null) {
            attributes.addFlashAttribute("msga", "Error al guardar, ya existe el correo!");
            return "redirect:/cusuarios/listado";
        }
        List<Rol> roles = rolesService.findAll();
        model.addAttribute("allRoles", roles);
        usuariosService.saveUser(usuario);
        model.addAttribute("titulo", "Nuevo usuario");
        attributes.addFlashAttribute("msg", "Los datos del usuario fueron guardados!");
        return "redirect:/cusuarios/listado";
    }

    @PostMapping("/registrar")
    public String registro(Model model, Usuario usuario, RedirectAttributes attributes) {
        //si existe un usuario con el mismo correo no lo guardamos
        if (usuariosService.findUserByEmail(usuario.getCorreo())!=null) {
            attributes.addFlashAttribute("msga", "Error al guardar, ya existe el correo!");
            return "redirect:/login";
        }
        usuario.setEnable(true);
        Rol rol = rolesService.findRolById(2);
        usuario.setRoles(Arrays.asList(rol));
        usuariosService.saveUser(usuario);
        attributes.addFlashAttribute("msg", "Los datos del registro fueron guardados!");
        return "redirect:/login";
    }

    @GetMapping("/registrar")
    public String nuevoRegistro(Model model) {
        model.addAttribute("titulo", "Nuevo registro");
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);
        return "/registro";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(Model model, @PathVariable("id") Integer id, Authentication authentication) {
        Usuario user = getCurrentUser(authentication);
        if (!isAdmin(user)) {
            return "redirect:/cusuarios/editar/currentUser";
        }
        Usuario usuario = usuariosService.findUserById(id);
        model.addAttribute("titulo", "Editar usuario");
        model.addAttribute("usuario", usuario);
        List<Rol> roles = rolesService.findAll();
        model.addAttribute("allRoles", roles);
        return "usuarios/formUsuario";
    }

    @GetMapping("/editar/currentUser")
    public String editarPropioUsuario(Model model, Authentication authentication) {
        Usuario usuario = getCurrentUser(authentication);
        model.addAttribute("titulo", "Editar usuario");
        model.addAttribute("usuario", usuario);
        return "usuarios/formUsuario";
    }

    @GetMapping("/borrar/{id}")
    public String eliminarUsuario(Model model, @PathVariable("id") Integer id, RedirectAttributes attributes) {
        Usuario usuario = usuariosService.findUserById(id);
        if (usuario != null) {
            usuariosService.deleteUser(id);
            attributes.addFlashAttribute("msg", "Los datos del usuario fueron borrados!");
        } else {
            attributes.addFlashAttribute("msg", "Usuario no encontrado!");
        }

        return "redirect:/cusuarios/listado";
    }

    private Usuario getCurrentUser (Authentication authentication) {
        String userEmail;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            userEmail = ((UserDetails) principal).getUsername();
            return usuariosService.findUserByEmail(userEmail);
        }
        throw new RuntimeException("¡¡Usuario sin credenciales!!");
    }

    private boolean isAdmin (Usuario user) {
        return user.getRoles().stream()
                .anyMatch(rol -> rol.getAuthority().equals("ROLE_ADMIN"));
    }

}
