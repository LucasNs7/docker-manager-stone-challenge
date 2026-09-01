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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
        when(dockerService.listContainers(false)).thenReturn(mockContainersList);

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
        when(dockerService.listContainers(true)).thenReturn(mockContainersList);

        // Com o param showAll
        mockMvc.perform(get("/api/containers").param("showAll", "false"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(dockerService).listContainers(false);
    }
}