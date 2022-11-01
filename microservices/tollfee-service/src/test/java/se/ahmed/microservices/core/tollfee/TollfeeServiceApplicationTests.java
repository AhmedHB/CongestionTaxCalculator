package se.ahmed.microservices.core.tollfee;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
public class TollfeeServiceApplicationTests {
	@Autowired
	private WebTestClient client;

	@Test
	public void getTollFeeByCity() {
		String city = "gothenburg";

		client.get()
				.uri("/tollfee/" + city)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType("application/json;charset=UTF-8")
				.expectBody()
				.jsonPath("$.city").isEqualTo(city);
	}

	@Test
	public void getTollFeeInvalidParameterString() {
		String city = "";

		client.get()
				.uri("/tollfee/"+ city)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isEqualTo(NOT_FOUND)
				.expectHeader().contentType("application/json")
				.expectBody();
	}


	@Test
	public void getTollFeeNotFound() {
		String cityNotFound = "malmö";

		client.get()
				.uri("/tollfee/" + cityNotFound)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isNotFound()
				.expectHeader().contentType("application/json")
				.expectBody();
	}

	@Test
	public void getTollFeeInvalidParameterNonStringValue() {
		int cityInvalid = -1;

		client.get()
				.uri("/tollfee/"+ cityInvalid)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isEqualTo(NOT_FOUND)
				.expectHeader().contentType("application/json")
				.expectBody();
	}
}
