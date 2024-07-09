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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@AllArgsConstructor
@Controller
@RequestMapping("/filmAlvaritty")
public class FilmsController {

    private IFilmsService filmsService;

    @GetMapping(value = {"/", "/home", ""})
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/films/new")
    public String create(Model model) {
        model.addAttribute("mainTitle", "Nueva película");
        Film film = new Film();
        model.addAttribute("film", film);
        return "films/formFilm";
    }

    @GetMapping("/films/search")
    public String search(Model model) {
        return "films/searchFilm";
    }

    @GetMapping("/films/list")
    public String listFilms(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Film> list = filmsService.findAll(pageable);
        PageRender<Film> pageRender = new PageRender<Film>("/filmAlvaritty/films/list", list);
        model.addAttribute("mainTitle", "Listado de todas las películas");
        model.addAttribute("listFilms", list);
        model.addAttribute("page", pageRender);
        return "films/listFilms";
    }

    @GetMapping("/films/idfilms/{id}")
    public String searchFilmById(Model model, @PathVariable("id") Integer idFilm) {
        Film film = filmsService.findFilmById(idFilm);
        model.addAttribute("film", film);
        return "films/formFilm";
    }

    @GetMapping("/films/title")
    public String searchFilmsByTitle(Model model, @RequestParam(name = "page",
            defaultValue = "0") int page, @RequestParam("title") String title) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Film> list;
        if (title.isEmpty()) {
            list = filmsService.findAll(pageable);
        } else {
            list = filmsService.findFilmsByTitle(title, pageable);
        }
        PageRender<Film> pageRender = new PageRender<Film>("/filmAlvaritty/films/list", list);
        model.addAttribute("mainTitle", "Listado de películas por título");
        model.addAttribute("listFilms", list);
        model.addAttribute("page", pageRender);
        return "films/listFilms";
    }

    @GetMapping("/films/genre")
    public String searchFilmsByGenre(Model model, @RequestParam(name = "page",
            defaultValue = "0") int page, @RequestParam("genre") String genre) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Film> list;
        if (genre.isEmpty() || genre.equals("enum")) {
            list = filmsService.findAll(pageable);
        } else {
            list = filmsService.findFilmsByGenre(genre, pageable);
        }
        PageRender<Film> pageRender = new PageRender<Film>("/filmAlvaritty/films/list", list);
        model.addAttribute("mainTitle", "Listado de películas por género");
        model.addAttribute("listFilms", list);
        model.addAttribute("page", pageRender);
        return "films/listFilms";
    }

    @GetMapping("/films/actor")
    public String searchFilmsByCast(Model model, @RequestParam(name = "page",
            defaultValue = "0") int page, @RequestParam("actor") String actor) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Film> list;
        if (actor.isEmpty()) {
            list = filmsService.findAll(pageable);
        } else {
            list = filmsService.findFilmsByCast(actor, pageable);
        }
        PageRender<Film> pageRender = new PageRender<Film>("/filmAlvaritty/films/list", list);
        model.addAttribute("mainTitle", "Listado de películas por reparto");
        model.addAttribute("listFilms", list);
        model.addAttribute("page", pageRender);
        return "films/listFilms";
    }

    @PostMapping(value="/films/save/", consumes = MULTIPART_FORM_DATA_VALUE)
    public String saveFilm(Model model, Film film, @RequestParam(value="posterInput",required = false) MultipartFile
            posterFile, RedirectAttributes attributes) throws IOException {
        if (posterFile != null && !posterFile.isEmpty()) {
            film.setPoster(posterFile.getBytes());
        }
        filmsService.saveFilm(film);
        model.addAttribute("mainTitle", "Nueva película");
        attributes.addFlashAttribute("msg", "La película se ha creado correctamente.");
        return "redirect:/filmAlvaritty/films/list";
    }

    @GetMapping("/films/edit/{id}")
    public String editFilm(Model model, @PathVariable("id") Integer idFilm) {
        Film film = filmsService.findFilmById(idFilm);
        model.addAttribute("mainTitle", "Editar película");
        model.addAttribute("film", film);
        return "films/formFilm";
    }

    @GetMapping("/films/delete/{id}")
    public String deleteFilm(Model model, @PathVariable("id") Integer idFilm, RedirectAttributes attributes) {
        filmsService.deleteFilm(idFilm);
        attributes.addFlashAttribute("msg", "La película se ha borrado correctamente");
        return "redirect:/filmAlvaritty/films/list";
    }
}
