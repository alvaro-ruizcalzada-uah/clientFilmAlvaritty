package es.uah.clientFilmAlvaritty.service;


import es.uah.clientFilmAlvaritty.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IReviewsService {

    Page<Review> findAll(Pageable pageable);

    List<Review> findReviewsByIdFilm(Integer idFilm);

    Review findReviewsById(Integer idReview);

    String saveReview(Review matricula);

    void deleteReview(Integer idReview);

    Double calculateAverageRating(Integer idFilm);

}
