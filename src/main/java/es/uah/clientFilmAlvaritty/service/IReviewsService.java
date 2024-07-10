package es.uah.clientFilmAlvaritty.service;


import es.uah.clientFilmAlvaritty.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IReviewsService {

    Page<Review> findAll(Pageable pageable);

    Page<Review> findReviewsByIdFilm(Integer idFilm, Pageable pageable);

    Review findReviewsById(Integer idReview);

    String saveReview(Review matricula);

    void deleteReview(Integer idReview);

}
