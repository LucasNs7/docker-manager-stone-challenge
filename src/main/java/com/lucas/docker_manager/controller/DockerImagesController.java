package com.lucas.docker_manager.controller;

import com.github.dockerjava.api.model.Image;
import com.lucas.docker_manager.service.DockerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/images")
public class DockerImagesController {

    private final DockerService dockerService;

    @GetMapping
    public List<Image> listImages() {
        return dockerService.listImages();
    }

    @GetMapping("/filter")
    public List<Image> listImagesFiltered(@RequestParam String imageName) {
        return dockerService.filteredImages(imageName);
    }
}
