package es.uah.clientFilmAlvaritty.controller;

import es.uah.clientFilmAlvaritty.model.Film;
import es.uah.clientFilmAlvaritty.paginator.PageRender;
import es.uah.clientFilmAlvaritty.service.IFilmsService;
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
public class FilmsController {

    private IFilmsService filmsService;

    @GetMapping(value = {"/", "/home", ""})
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("mainTitle", "Nueva película");
        Film film = new Film();
        model.addAttribute("film", film);
        return "films/formFilm";
    }

    @GetMapping("/search")
    public String search(Model model) {
        return "films/searchFilm";
    }

    @GetMapping("/list")
    public String listFilms(Model model, @RequestParam(name="page", defaultValue="0") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Film> list = filmsService.findAll(pageable);
        PageRender<Film> pageRender = new PageRender<Film>("/filmAlvaritty/list", list);
        model.addAttribute("mainTitle", "Listado de todas las películas");
        model.addAttribute("listFilms", list);
        model.addAttribute("page", pageRender);
        return "films/listFilms";
    }

    @GetMapping("/idfilms/{id}")
    public String searchFilmById(Model model, @PathVariable("id") Integer idFilm) {
        Film film = filmsService.findFilmById(idFilm);
        model.addAttribute("film", film);
        return "films/formFilm";
    }

    @GetMapping("/title")
    public String searchFilmsByTitle(Model model, @RequestParam(name="page",
            defaultValue="0") int page, @RequestParam("title") String title) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Film> list;
        if (title.isEmpty()) {
            list = filmsService.findAll(pageable);
        } else {
            list = filmsService.findFilmsByTitle(title, pageable);
        }
        PageRender<Film> pageRender = new PageRender<Film>("/list", list);
        model.addAttribute("mainTitle", "Listado de películas por título");
        model.addAttribute("listFilms", list);
        model.addAttribute("page", pageRender);
        return "films/listFilms";
    }

    @GetMapping("/genre")
    public String searchFilmsByGenre(Model model, @RequestParam(name="page",
            defaultValue="0") int page, @RequestParam("genre") String genre) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Film> list;
        if (genre.isEmpty() || genre.equals("enum")) {
            list = filmsService.findAll(pageable);
        } else {
            list = filmsService.findFilmsByGenre(genre, pageable);
        }
        PageRender<Film> pageRender = new PageRender<Film>("/list", list);
        model.addAttribute("mainTitle", "Listado de películas por género");
        model.addAttribute("listFilms", list);
        model.addAttribute("page", pageRender);
        return "films/listFilms";
    }

    @PostMapping("/save/")
    public String saveFilm(Model model, Film film, RedirectAttributes attributes) {
        filmsService.saveFilm(film);
        model.addAttribute("mainTitle", "Nueva película");
        attributes.addFlashAttribute("msg", "La película se ha creado correctamente.");
        return "redirect:/filmAlvaritty/list";
    }

    @GetMapping("/edit/{id}")
    public String editFilm(Model model, @PathVariable("id") Integer idFilm) {
        Film film = filmsService.findFilmById(idFilm);
        model.addAttribute("mainTitle", "Editar película");
        model.addAttribute("film", film);
        return "films/formFilm";
    }

    @GetMapping("/delete/{id}")
    public String deleteFilm(Model model, @PathVariable("id") Integer idFilm, RedirectAttributes attributes) {
        filmsService.deleteFilm(idFilm);
        attributes.addFlashAttribute("msg", "La película se ha borrado correctamente");
        return "redirect:/filmAlvaritty/list";
    }
}
