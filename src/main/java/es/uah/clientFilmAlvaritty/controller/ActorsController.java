package es.uah.clientFilmAlvaritty.controller;

import es.uah.clientFilmAlvaritty.model.Actor;
import es.uah.clientFilmAlvaritty.paginator.PageRender;
import es.uah.clientFilmAlvaritty.service.IActorsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@AllArgsConstructor
@Controller
@RequestMapping("/filmAlvaritty")
public class ActorsController {

    private IActorsService actorsService;

    @GetMapping("/actors/new")
    public String create(Model model) {
        model.addAttribute("mainTitle", "Nuevo actor");
        Actor actor = new Actor();
        model.addAttribute("actor", actor);
        return "actors/formActor";
    }

    @GetMapping("/actors/search")
    public String search(Model model) {
        return "actors/searchActor";
    }

    @GetMapping("/actors/list")
    public String listActors(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Actor> list = actorsService.findAll(pageable);
        PageRender<Actor> pageRender = new PageRender<Actor>("/filmAlvaritty/actors/list", list);
        model.addAttribute("mainTitle", "Listado de todos los actores");
        model.addAttribute("listActors", list);
        model.addAttribute("page", pageRender);
        return "actors/listActors";
    }

    @GetMapping("/actors/id/{id}")
    public String searchActorById(Model model, @PathVariable("id") Integer idActor) {
        Actor actor = actorsService.findActorById(idActor);
        model.addAttribute("actor", actor);
        return "actors/formActor";
    }

    @GetMapping("/actors/name")
    public String searchActorsByName(Model model, @RequestParam(name = "page",
            defaultValue = "0") int page, @RequestParam("name") String name) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Actor> list;
        if (name.isEmpty()) {
            list = actorsService.findAll(pageable);
        } else {
            list = actorsService.findActorsByName(name, pageable);
        }
        PageRender<Actor> pageRender = new PageRender<Actor>("/filmAlvaritty/actors/list", list);
        model.addAttribute("mainTitle", "Listado de actores por nombre");
        model.addAttribute("listActors", list);
        model.addAttribute("page", pageRender);
        return "actors/listActors";
    }

    @PostMapping("/actors/save/")
    public String saveActor(Model model, Actor actor, RedirectAttributes attributes) {
        actorsService.saveActor(actor);
        model.addAttribute("mainTitle", "Nuevo actor");
        attributes.addFlashAttribute("msg", "El actor se ha creado correctamente.");
        return "redirect:/filmAlvaritty/actors/list";
    }

    @GetMapping("/actors/edit/{id}")
    public String editActor(Model model, @PathVariable("id") Integer idActor) {
        Actor actor = actorsService.findActorById(idActor);
        model.addAttribute("mainTitle", "Editar actor");
        model.addAttribute("actor", actor);
        return "actors/formActor";
    }

    @GetMapping("/actors/delete/{id}")
    public String deleteActor(Model model, @PathVariable("id") Integer idActor, RedirectAttributes attributes) {
        actorsService.deleteActor(idActor);
        attributes.addFlashAttribute("msg", "El actor se ha borrado correctamente");
        return "redirect:/filmAlvaritty/actors/list";
    }
}
