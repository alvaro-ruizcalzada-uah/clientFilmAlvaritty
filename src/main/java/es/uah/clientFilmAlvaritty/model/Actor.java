package es.uah.clientFilmAlvaritty.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Actor {

    private Integer idActor;

    private String name;

    private LocalDate birthday;

    private String country;

    private List<Film> filmography;

}
