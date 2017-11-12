package by.bsu.rabbitmq;

import by.bsu.rabbitmq.config.RabbitConfiguration;
import by.bsu.rabbitmq.config.QuartzConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@EnableAutoConfiguration
@ComponentScan (basePackages = "by.bsu.rabbitmq")
@Import({RabbitConfiguration.class, QuartzConfiguration.class})
public class RabbitmqApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(RabbitmqApplication.class, args);
    }
}
