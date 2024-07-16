package com.studies.catalog.admin.infrastructure;

import com.studies.catalog.admin.infrastructure.configuration.WebServerConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VideoStreamCatalogAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebServerConfig.class, args);
    }

    @RabbitListener(queues = "video.encoded.queue")
    void dummy() {

    }

}