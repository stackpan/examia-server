package io.github.stackpan.examia.server.examiaserver;

import io.github.stackpan.examia.server.examiaserver.configuration.ExamiaConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Import(ExamiaConfiguration.class)
@Profile("test")
public class ExamiaTestConfiguration {

}
