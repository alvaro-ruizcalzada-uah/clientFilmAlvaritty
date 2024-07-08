package es.uah.clientFilmAlvaritty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ClientFilmAlvarittyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientFilmAlvarittyApplication.class, args);
	}

	@Bean
	public RestTemplate template() { return new RestTemplate(); }

}
