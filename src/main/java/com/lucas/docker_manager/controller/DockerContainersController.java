package com.lucas.docker_manager.controller;

import com.github.dockerjava.api.model.Container;
import com.lucas.docker_manager.service.DockerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/containers")
public class DockerContainersController {

    private final DockerService dockerService;

    @GetMapping
    public List<Container> listContainers(@RequestParam boolean showAll) {
        return dockerService.listContainers(showAll);
    }

    @PostMapping("/{id}/start")
    public void startContainer(@PathVariable String id) {
        dockerService.startContainer(id);
    }

    @PostMapping("/{id}/stop")
    public void stopContainer(@PathVariable String id){
        dockerService.stopContainer(id);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteContainer(@PathVariable String id) {
        dockerService.deleteContainer(id);
    }

    @PostMapping
    public void createContainer(@RequestParam String imageName){
        dockerService.createContainer(imageName);
    }
}
