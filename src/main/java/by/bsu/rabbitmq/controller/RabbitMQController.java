package by.bsu.rabbitmq.controller;

import by.bsu.rabbitmq.job.CustomJob;
import by.bsu.rabbitmq.job.JobScheduler;
import by.bsu.rabbitmq.model.CommentModel;
import org.quartz.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class RabbitMQController {

    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("comment", new CommentModel());
        return "comment";
    }

    @PostMapping (value = "/comment")
    public String postAComment(@ModelAttribute("comment") CommentModel comment, Model model) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        JobKey jobKey = JobKey.jobKey("customJob", "customJobGroup");
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        jobDetail.getJobDataMap().put("comment", comment.getCommentContent());
        scheduler.addJob(jobDetail, true);

        scheduler.triggerJob(jobKey);

        model.addAttribute("result", comment.getCommentContent());
        return "comment";
    }

}