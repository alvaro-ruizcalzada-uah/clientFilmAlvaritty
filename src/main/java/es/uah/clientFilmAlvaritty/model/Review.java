package es.uah.clientFilmAlvaritty.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Review {

    private Integer idReview;

    private Integer idFilm;

    private Integer rating;

    private String opinion;

    private LocalDate date;

    private Usuario user;

}
