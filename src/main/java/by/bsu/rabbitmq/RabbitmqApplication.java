package by.bsu.rabbitmq;

import by.bsu.rabbitmq.config.AppConfiguration;
import by.bsu.rabbitmq.config.QuartzConfiguration;
import by.bsu.rabbitmq.job.JobScheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@EnableAutoConfiguration
@ComponentScan (basePackages = "by.bsu.rabbitmq")
@Import({AppConfiguration.class, QuartzConfiguration.class})
public class RabbitmqApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(RabbitmqApplication.class, args);
    }
}
