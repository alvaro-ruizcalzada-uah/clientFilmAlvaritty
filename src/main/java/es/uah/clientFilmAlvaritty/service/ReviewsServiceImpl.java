package es.uah.clientFilmAlvaritty.service;

import es.uah.clientFilmAlvaritty.model.Film;
import es.uah.clientFilmAlvaritty.model.Review;
import es.uah.clientFilmAlvaritty.model.Usuario;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
public class ReviewsServiceImpl implements IReviewsService{

    private RestTemplate template;

    private IFilmsService filmsService;

    private final String url = "http://localhost:8090/api/usuarios/reviews";


    @Override
    public Page<Review> findAll(Pageable pageable) {
        Review[] reviews = template.getForObject(url, Review[].class);
        List<Review> reviewsList = Arrays.asList(reviews);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Review> list;

        if(reviewsList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, reviewsList.size());
            list = reviewsList.subList(startItem, toIndex);
        }
        Page<Review> page = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), reviewsList.size());
        return page;
    }

    @Override
    public List<Review> findReviewsByIdFilm(Integer idFilm) {
        Review[] reviews = template.getForObject(url+"/film/"+idFilm, Review[].class);
        return Arrays.asList(reviews);
    }

    @Override
    public Review findReviewsById(Integer idReview) {
        return template.getForObject(url+"/"+idReview, Review.class);
    }

    @Override
    public String saveReview(Review review) {
        if (review.getIdReview() != null && review.getIdReview() > 0) {
            return "No se puede modificar una crítica.";
        } else {
            Film film = filmsService.findFilmById(review.getIdFilm());
            if(film == null) {
                return "¡La película sobre la que se quiere opinar no existe!";
            }
            review.setIdReview(0);
            review.setDate(LocalDate.now());
            template.postForObject(url, review, String.class);
            return "¡Los datos de la reseña fueron guardados!";
        }
    }

    @Override
    public void deleteReview(Integer idReview) {
        template.delete(url+ "/" +  idReview);
    }

    @Override
    public Double calculateAverageRating(Integer idFilm) {
        Review[] reviews = template.getForObject(url+"/film/"+idFilm, Review[].class);
        List<Review> reviewsList = Arrays.asList(reviews);
        return reviewsList.stream().mapToInt(Review::getRating).average().orElse(0);
    }
}
