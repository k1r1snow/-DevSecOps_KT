package com.example.analytics;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/analytics")
public class AnalyticsApplication {

	private long count;

	public static void main(String[] args) {
		SpringApplication.run(AnalyticsApplication.class, args);
	}

	@PostMapping("/increment")
	public Map<String, Long> increment() {
		count++;
		return Map.of("count", count);
	}

	@GetMapping
	public Map<String, Long> getCount() {
		return Map.of("count", count);
	}
}