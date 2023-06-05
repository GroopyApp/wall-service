package app.groopy.wallservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class WallServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(WallServiceApplication.class, args);
	}
}
