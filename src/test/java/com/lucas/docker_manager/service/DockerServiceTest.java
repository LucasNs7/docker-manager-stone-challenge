package com.lucas.docker_manager.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DockerServiceTest {

    @Mock
    private DockerClient dockerClient;

    @Mock
    private StopContainerCmd stopContainerCmd;

    @Mock
    private RemoveContainerCmd removeContainerCmd;

    @Mock
    private CreateContainerCmd createContainerCmd;

    @Mock
    private ListImagesCmd listImagesCmd;

    @Mock
    private ListContainersCmd listContainersCmd;

    @Mock
    private StartContainerCmd startContainerCmd;

    @InjectMocks
    private DockerService dockerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve consultar os containers com showAll=true")
    void testListContainersTrue() {
        // Mock test
        List<Container> mockContainersList = Collections.emptyList();

        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
        when(listContainersCmd.withShowAll(true)).thenReturn(listContainersCmd);
        when(listContainersCmd.exec()).thenReturn(mockContainersList);

        // Normal Test
        List<Container> containers = dockerService.listContainers(true);

        // Verificações
        assertEquals(mockContainersList, containers);
        verify(dockerClient).listContainersCmd();
        verify(listContainersCmd).withShowAll(true);
        verify(listContainersCmd).exec();
    }

    @Test
    @DisplayName("Deve consultar os containers com showAll=false")
    void testListContainersFalse() {
        // Mock test
        List<Container> mockContainersList = Collections.emptyList();

        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
        when(listContainersCmd.withShowAll(false)).thenReturn(listContainersCmd);
        when(listContainersCmd.exec()).thenReturn(mockContainersList);

        // Normal Test
        List<Container> containers = dockerService.listContainers(false);

        // Verificações
        assertEquals(mockContainersList, containers);
        verify(dockerClient).listContainersCmd();
        verify(listContainersCmd).withShowAll(false);
        verify(listContainersCmd).exec();
    }

    @Test
    @DisplayName("Deve testar se o id recebido está sendo passado")
    void testStartContainer() {
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.startContainerCmd(eq(containerId))).thenReturn(startContainerCmd);

        dockerService.startContainer(containerId);

        verify(dockerClient).startContainerCmd(containerId);
        verify(startContainerCmd).exec();
    }

    @Test
    @DisplayName("Deve testar se o startContainer está lançando a exeção ContainerNotFound")
    void testStartContainer2() {
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.startContainerCmd(eq(containerId))).thenReturn(startContainerCmd);
        when(startContainerCmd.exec()).thenThrow(new NotFoundException("Container Not Found"));

        assertThrows(NotFoundException.class, () -> dockerService.startContainer(containerId));

        verify(dockerClient).startContainerCmd(containerId);
        verify(startContainerCmd).exec();
    }

    @Test
    @DisplayName("Deve consultar todas as imagens disponíveis")
    void testListImages() {
        // Mock test
        List<Image> mockImagesList = Collections.emptyList();

        when(dockerClient.listImagesCmd()).thenReturn(listImagesCmd);
        when(listImagesCmd.exec()).thenReturn(mockImagesList);

        // Normal Test
        List<Image> images = dockerService.listImages();

        // Verificações
        assertEquals(mockImagesList, images);
        verify(dockerClient).listImagesCmd();
        verify(listImagesCmd).exec();
    }

    @Test
    @DisplayName("Deve filtrar imagens pelo nome com sucesso")
    void testFilteredImages() {
        // Mock test
        String imageName = "ubuntu:latest";
        List<Image> mockImagesList = Collections.emptyList();

        when(dockerClient.listImagesCmd()).thenReturn(listImagesCmd);
        when(listImagesCmd.withFilter("reference", Collections.singletonList(imageName))).thenReturn(listImagesCmd);
        when(listImagesCmd.exec()).thenReturn(mockImagesList);

        // Normal Test
        List<Image> images = dockerService.filteredImages(imageName);

        // Verificações
        assertEquals(mockImagesList, images);
        verify(dockerClient).listImagesCmd();
        verify(listImagesCmd).withFilter("reference", Collections.singletonList(imageName));
        verify(listImagesCmd).exec();
    }

    @Test
    @DisplayName("Deve testar se o id recebido está sendo passado para parar o container")
    void testStopContainer() {
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.stopContainerCmd(eq(containerId))).thenReturn(stopContainerCmd);

        dockerService.stopContainer(containerId);

        verify(dockerClient).stopContainerCmd(containerId);
        verify(stopContainerCmd).exec();
    }

    @Test
    @DisplayName("Deve testar se o stopContainer está lançando a exceção NotFoundException ao tentar parar container inexistente")
    void testStopContainerNotFound() {
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.stopContainerCmd(eq(containerId))).thenReturn(stopContainerCmd);
        when(stopContainerCmd.exec()).thenThrow(new NotFoundException("Container Not Found"));

        assertThrows(NotFoundException.class, () -> dockerService.stopContainer(containerId));

        verify(dockerClient).stopContainerCmd(containerId);
        verify(stopContainerCmd).exec();
    }

    @Test
    @DisplayName("Deve testar se o id recebido está sendo passado para deletar o container")
    void testDeleteContainer() {
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.removeContainerCmd(eq(containerId))).thenReturn(removeContainerCmd);

        dockerService.deleteContainer(containerId);

        verify(dockerClient).removeContainerCmd(containerId);
        verify(removeContainerCmd).exec();
    }

    @Test
    @DisplayName("Deve testar se o deleteContainer está lançando a exceção NotFoundException ao tentar deletar container inexistente")
    void testDeleteContainerNotFound() {
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.removeContainerCmd(eq(containerId))).thenReturn(removeContainerCmd);
        when(removeContainerCmd.exec()).thenThrow(new NotFoundException("Container Not Found"));

        assertThrows(NotFoundException.class, () -> dockerService.deleteContainer(containerId));

        verify(dockerClient).removeContainerCmd(containerId);
        verify(removeContainerCmd).exec();
    }

    @Test
    @DisplayName("Deve testar a criação de container a partir do nome da imagem")
    void testCreateContainer() {
        String imageName = "nginx:latest";
        when(dockerClient.createContainerCmd(eq(imageName))).thenReturn(createContainerCmd);
        when(createContainerCmd.exec()).thenReturn(null);

        dockerService.createContainer(imageName);

        verify(dockerClient).createContainerCmd(imageName);
        verify(createContainerCmd).exec();
    }

    @Test
    @DisplayName("Deve testar se o createContainer lança exceção ao tentar criar container com imagem inexistente")
    void testCreateContainerNotFound() {
        String imageName = "invalid-image:latest";
        when(dockerClient.createContainerCmd(eq(imageName))).thenReturn(createContainerCmd);
        when(createContainerCmd.exec()).thenThrow(new NotFoundException("Image Not Found"));

        assertThrows(NotFoundException.class, () -> dockerService.createContainer(imageName));

        verify(dockerClient).createContainerCmd(imageName);
        verify(createContainerCmd).exec();
    }
}