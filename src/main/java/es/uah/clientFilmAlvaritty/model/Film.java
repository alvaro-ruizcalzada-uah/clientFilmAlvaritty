package es.uah.clientFilmAlvaritty.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Film {
    private Integer idFilm;
    private String title;
    private Integer year;
    private Integer runningTime;
    private String country;
    private String director;
    private String genre;
    private String synopsis;
    private byte[] poster;
}
