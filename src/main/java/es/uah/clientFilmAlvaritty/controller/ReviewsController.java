package es.uah.clientFilmAlvaritty.controller;


import es.uah.clientFilmAlvaritty.model.Review;
import es.uah.clientFilmAlvaritty.model.Usuario;
import es.uah.clientFilmAlvaritty.paginator.PageRender;
import es.uah.clientFilmAlvaritty.service.IReviewsService;
import es.uah.clientFilmAlvaritty.service.IUsuariosService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/creviews")
public class ReviewsController {

    IReviewsService reviewsService;

    IUsuariosService usuariosService;

    @GetMapping("/list")
    public String listReviews(Model model, @RequestParam(name = "page", defaultValue = "0") int page, Authentication authentication) {
        Usuario user = getCurrentUser(authentication);
        if (!isAdmin(user)) {
            return "redirect:/creviews/list/currentUser";
        }
        Pageable pageable = PageRequest.of(page, 5);
        Page<Review> list = reviewsService.findAll(pageable);
        PageRender<Review> pageRender = new PageRender<Review>("/creviews/listReviews", list);
        model.addAttribute("mainTitle", "Listado de todas las críticas");
        model.addAttribute("listReviews", list);
        model.addAttribute("page", pageRender);
        return "reviews/listReviews";
    }

    @GetMapping("/list/currentUser")
    public String listReviewsByCurrentUser(Model model, @RequestParam(name = "page", defaultValue = "0") int page, Authentication authentication) {
        Pageable pageable = PageRequest.of(page, 5);
        Usuario user = getCurrentUser(authentication);
        List<Review> reviews = user != null ? user.getReviews() : Collections.emptyList();
        Page<Review> list = generatePage(reviews, pageable);
        PageRender<Review> pageRender = new PageRender<Review>("/creviews/listReviews", list);
        model.addAttribute("mainTitle", "Listado de todas tus críticas");
        model.addAttribute("listReviews", list);
        model.addAttribute("page", pageRender);
        return "reviews/listReviews";
    }

    @GetMapping("/new")
    public String createReview(Model model) {
        Review review = new Review();
        model.addAttribute("mainTitle", "Nueva crítica");
        model.addAttribute("review", review);
        return "reviews/formReview";
    }

    @PostMapping("/save/")
    public String saveReview(Model model, Review review, RedirectAttributes attributes, Authentication authentication) {
        Usuario user = getCurrentUser(authentication);
        if (user != null) {
            review.setUser(user);
        }
        String resultado = reviewsService.saveReview(review);
        model.addAttribute("mainTitle", "Nueva crítica");
        attributes.addFlashAttribute("msg", resultado);
        if (isAdmin(user)) {
            return "redirect:/creviews/list";
        } else {
            return "redirect:/creviews/list/currentUser";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteReview(Model model, @PathVariable("id") Integer id, RedirectAttributes attributes) {
        Review review = reviewsService.findReviewsById(id);
        if (review != null) {
            reviewsService.deleteReview(id);
            attributes.addFlashAttribute("msg", "¡Los datos de la crítica fueron borrados!");
        } else {
            attributes.addFlashAttribute("msg", "¡Crítica no encontrada!");
        }

        return "redirect:/creviews/list";
    }

    private Page<Review> generatePage(List<Review> reviewsList, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Review>list;

        if(reviewsList.size() <startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, reviewsList.size());
            list = reviewsList.subList(startItem, toIndex);
        }
        Page<Review> page = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), reviewsList.size());
        return page;
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
