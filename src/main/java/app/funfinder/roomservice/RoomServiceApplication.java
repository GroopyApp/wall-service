package app.funfinder.roomservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories
public class RoomServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomServiceApplication.class, args);
	}

}
