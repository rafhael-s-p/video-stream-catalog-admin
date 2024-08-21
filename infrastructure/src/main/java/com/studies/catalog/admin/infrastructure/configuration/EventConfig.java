package com.studies.catalog.admin.infrastructure.configuration;

import com.studies.catalog.admin.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.studies.catalog.admin.infrastructure.configuration.properties.amqp.QueueProperties;
import com.studies.catalog.admin.infrastructure.services.EventService;
import com.studies.catalog.admin.infrastructure.services.impl.RabbitEventService;
import com.studies.catalog.admin.infrastructure.services.local.InMemoryEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class EventConfig {

    @Bean
    @VideoCreatedQueue
    @Profile({"dev"})
    EventService localVideoCreatedEventService() {
        return new InMemoryEventService();
    }

    @Bean
    @VideoCreatedQueue
    @ConditionalOnMissingBean
    EventService videoCreatedEventService(
            @VideoCreatedQueue final QueueProperties props,
            final RabbitOperations ops
    ) {
        return new RabbitEventService(props.getExchange(), props.getRoutingKey(), ops);
    }

}