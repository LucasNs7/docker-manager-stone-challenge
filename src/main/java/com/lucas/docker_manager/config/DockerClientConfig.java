package com.lucas.docker_manager.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.time.Duration;

@Configuration
public class DockerClientConfig {

    @Value("${docker.host.path}")
    private String dockerHostPath;

    @Bean
    @Lazy(false)
    public DockerClient buildDockerClient() {
        DefaultDockerClientConfig.Builder dockerClientConfigBuilder = DefaultDockerClientConfig
                .createDefaultConfigBuilder();

        if (this.dockerHostPath != null && !this.dockerHostPath.isBlank()) {
            dockerClientConfigBuilder
                    .withDockerHost(this.dockerHostPath)
                    .withDockerTlsVerify(false);
        }

        DefaultDockerClientConfig dockerClientConfig = dockerClientConfigBuilder.build();

        ApacheDockerHttpClient dockerHttpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(dockerClientConfig.getDockerHost())
                .maxConnections(5)
                .connectionTimeout(Duration.ofMillis(300))
                .responseTimeout(Duration.ofSeconds(3))
                .build();

        DockerClient client = DockerClientBuilder.getInstance(dockerClientConfig)
                .withDockerHttpClient(dockerHttpClient)
                .build();

        client.pingCmd().exec();

        return client;
    }

}
