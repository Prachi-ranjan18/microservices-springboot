package com.microservices.product_service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;
//import org.testcontainers.shaded.org.hamcrest.Matchers;
import org.hamcrest.Matchers;


import java.util.regex.Matcher;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {
	@ServiceConnection
	static MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:7.0.5");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}
	static {
		mongoDbContainer.start();
		//System.setProperty("spring.data.mongodb.uri", mongoDbContainer.getReplicaSetUrl());
	}
	@Test
	void shouldCreateProduct() {
		String RequestBody = """
				{
				    "id": "4d5e6f",
				    "name": "Smartphone Case",
				    "description": "Durable and stylish smartphone case with shock absorption.",
				    "price": 19.99
				  }
				""";
		RestAssured.given()
				.contentType("application/json")
				.body(RequestBody)
				.when()
				.post("/api/product")
				.then()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name",Matchers.equalTo("Smartphone Case"));
	}

}

