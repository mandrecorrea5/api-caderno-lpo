package br.com._tech.api_caderno_lpo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "br.com._tech.api_caderno_lpo.repository")
public class CadernoLpoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CadernoLpoApplication.class, args);
	}

}
