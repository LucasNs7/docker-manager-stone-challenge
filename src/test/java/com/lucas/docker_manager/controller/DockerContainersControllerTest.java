package com.lucas.docker_manager.controller;

import com.github.dockerjava.api.model.Container;
import com.lucas.docker_manager.service.DockerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DockerContainersControllerTest {

    @Mock
    private DockerService dockerService;

    @InjectMocks
    private DockerContainersController dockerContainersController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(dockerContainersController).build();
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia usando o showAll=true do defaultValue")
    void listContainers() throws Exception {
        List<Container> mockContainersList = Collections.emptyList();
        when(dockerService.listContainers(true)).thenReturn(mockContainersList);

        // Sem o param showAll
        mockMvc.perform(get("/api/containers"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(dockerService).listContainers(true);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia usando o showAll=false")
    void listContainers2() throws Exception {
        List<Container> mockContainersList = Collections.emptyList();
        when(dockerService.listContainers(false)).thenReturn(mockContainersList);

        // Com o param showAll
        mockMvc.perform(get("/api/containers").param("showAll", "false"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(dockerService).listContainers(false);
    }

    @Test
    @DisplayName("Deve iniciar um container através do id informado na URL")
    void startContainer() throws Exception {
        String containerId = UUID.randomUUID().toString();
        doNothing().when(dockerService).startContainer(containerId);

        mockMvc.perform(post("/api/containers/{id}/start", containerId))
                .andExpect(status().isOk());

        verify(dockerService).startContainer(containerId);
    }

    @Test
    @DisplayName("Deve parar um container através do id informado na URL")
    void stopContainer() throws Exception {
        String containerId = UUID.randomUUID().toString();
        doNothing().when(dockerService).stopContainer(containerId);

        mockMvc.perform(post("/api/containers/{id}/stop", containerId))
                .andExpect(status().isOk());

        verify(dockerService).stopContainer(containerId);
    }

    @Test
    @DisplayName("Deve deletar um container através do id informado na URL")
    void deleteContainer() throws Exception {
        String containerId = UUID.randomUUID().toString();
        doNothing().when(dockerService).deleteContainer(containerId);

        mockMvc.perform(delete("/api/containers/{id}/delete", containerId))
                .andExpect(status().isOk());

        verify(dockerService).deleteContainer(containerId);
    }

    @Test
    @DisplayName("Deve criar um container passando o parâmetro imageName")
    void createContainer() throws Exception {
        String imageName = "nginx:latest";
        doNothing().when(dockerService).createContainer(imageName);

        mockMvc.perform(post("/api/containers").param("imageName", imageName))
                .andExpect(status().isOk());

        verify(dockerService).createContainer(imageName);
    }
}