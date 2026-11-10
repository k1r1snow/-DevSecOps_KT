package com.example.task_api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TaskController {

	private final TaskRepository taskRepository;
	private final RestTemplate restTemplate = new RestTemplate();
	private final String notificationServiceUrl;
	private final String analyticsServiceUrl;

	public TaskController(TaskRepository taskRepository,
			@Value("${services.notification.url}") String notificationServiceUrl,
			@Value("${services.analytics.url}") String analyticsServiceUrl) {
		this.taskRepository = taskRepository;
		this.notificationServiceUrl = notificationServiceUrl;
		this.analyticsServiceUrl = analyticsServiceUrl;
	}

	@GetMapping("/health")
	public Map<String, String> health() {
		return Map.of("status", "UP");
	}

	@GetMapping("/tasks")
	public List<Task> getTasks() {
		return taskRepository.findAll();
	}

	@PostMapping("/tasks")
	public ResponseEntity<Task> createTask(@RequestBody Task task) {
		task.setId(null);
		Task savedTask = taskRepository.save(task);
		notifyServices("Task created: " + savedTask.getTitle());
		return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
	}

	@PutMapping("/tasks/{id}")
	public Task updateTask(@PathVariable Long id, @RequestBody Task request) {
		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
		task.setTitle(request.getTitle());
		Task savedTask = taskRepository.save(task);
		notifyServices("Task updated: " + savedTask.getTitle());
		return savedTask;
	}

	@DeleteMapping("/tasks/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		if (!taskRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
		}
		taskRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private void notifyServices(String message) {
		restTemplate.postForEntity(notificationServiceUrl + "/notifications", message, Void.class);
		restTemplate.postForEntity(analyticsServiceUrl + "/analytics/increment", null, Void.class);
	}
}
