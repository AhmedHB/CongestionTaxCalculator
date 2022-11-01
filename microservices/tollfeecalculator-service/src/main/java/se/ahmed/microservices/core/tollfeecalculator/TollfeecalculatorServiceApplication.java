package se.ahmed.microservices.core.tollfeecalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("se.ahmed")
public class TollfeecalculatorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TollfeecalculatorServiceApplication.class, args);
	}

}
