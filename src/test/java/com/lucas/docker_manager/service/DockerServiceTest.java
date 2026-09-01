package com.lucas.docker_manager.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Container;
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
    ListContainersCmd listContainersCmd;

    @Mock
    StartContainerCmd startContainerCmd;

    @InjectMocks
    DockerService dockerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve consultar os containers com showAll=true")
    public void testListContainersTrue() {
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
    public void testListContainersFalse() {
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
    public void testStartContainer() {
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.startContainerCmd(eq(containerId))).thenReturn(startContainerCmd);

        dockerService.startContainer(containerId);

        verify(dockerClient).startContainerCmd(containerId);
        verify(startContainerCmd).exec();
    }

    @Test
    @DisplayName("Deve testar se o startContainer está lançando a exeção ContainerNotFound")
    public void testStartContainer2() {
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.startContainerCmd(eq(containerId))).thenReturn(startContainerCmd);
        when(startContainerCmd.exec()).thenThrow(new NotFoundException("Container Not Found"));

        assertThrows(NotFoundException.class, () -> dockerService.startContainer(containerId));

        verify(dockerClient).startContainerCmd(containerId);
        verify(startContainerCmd).exec();
    }
}