package io.github.stackpan.examia.server;

import io.github.stackpan.examia.server.configuration.ExamiaConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Import(ExamiaConfiguration.class)
@Profile("test")
public class ExamiaTestConfiguration {

}
