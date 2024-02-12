package com.presidential.elections.Controllers;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.presidential.elections.Entities.User;
import com.presidential.elections.Repository.UserRepository;
import com.presidential.elections.Service.UserService;
import com.presidential.elections.storage.StorageFileNotFoundException;
import com.presidential.elections.storage.StorageService;


@Controller
public class FileUploadController {
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	private final StorageService storageService;

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/DisplayImages")
	public String listUploadedFiles(Model model) throws IOException {
		Optional<User> optionalUser = userRepository.findByusername(userService.getUsernameAuthenticated());
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			if (user.getLink_photo() != null) {
				URI reconstructedUri = URI.create(user.getLink_photo());
				model.addAttribute("link_photo", reconstructedUri.toString());
			}
		}
		return "ProfileSettings";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = storageService.loadAsResource(filename);
		if (file == null) {
			return ResponseEntity.notFound().build();
		}	
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
	
	@PostMapping("/UploadImage")
	public String handleFileUpload(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()){		
			storageService.store(file);
		}
		Optional<User> optionalUser = userRepository.findByusername(userService.getUsernameAuthenticated());
			if (optionalUser.isPresent()) {
				User user = optionalUser.get();
				user.setLink_photo("http://localhost:8080/files/" + file.getOriginalFilename());
				userRepository.save(user);
			}
		return "redirect:/DisplayImages";
	}	

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
