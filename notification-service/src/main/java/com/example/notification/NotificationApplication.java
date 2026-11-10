package com.example.notification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/notifications")
public class NotificationApplication {

	private final List<String> notifications = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);
	}

	@GetMapping
	public List<String> getNotifications() {
		return notifications;
	}

	@PostMapping
	public List<String> addNotification(@RequestBody String message) {
		notifications.add(message);
		return notifications;
	}
}