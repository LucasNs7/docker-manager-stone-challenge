package com.lucas.docker_manager.controller;

import com.github.dockerjava.api.model.Image;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DockerImagesControllerTest {

    @Mock
    private DockerService dockerService;

    @InjectMocks
    private DockerImagesController dockerImagesController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(dockerImagesController).build();
    }

    @Test
    @DisplayName("Deve retornar a lista de imagens com sucesso")
    void listImages() throws Exception {
        List<Image> mockImagesList = Collections.emptyList();
        when(dockerService.listImages()).thenReturn(mockImagesList);

        mockMvc.perform(get("/api/images"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(dockerService).listImages();
    }

    @Test
    @DisplayName("Deve retornar a lista de imagens filtradas pelo parâmetro imageName")
    void listImagesFiltered() throws Exception {
        String imageName = "ubuntu:latest";
        List<Image> mockImagesList = Collections.emptyList();
        when(dockerService.filteredImages(imageName)).thenReturn(mockImagesList);

        mockMvc.perform(get("/api/images/filter").param("imageName", imageName))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(dockerService).filteredImages(imageName);
    }
}