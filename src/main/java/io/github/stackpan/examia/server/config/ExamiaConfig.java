package io.github.stackpan.examia.server.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ConfigurationPropertiesScan(basePackages = "io.github.stackpan.examia.server.config.properties")
public class ExamiaConfig {
}
