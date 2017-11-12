package by.bsu.rabbitmq.controller;

import by.bsu.rabbitmq.constant.ParameterConst;
import by.bsu.rabbitmq.model.CommentModel;
import by.bsu.rabbitmq.util.JsonTransformationsUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.TriggerBuilder.newTrigger;


@Controller
public class RabbitMQController {
    private SchedulerFactoryBean schedulerFactory;
    @Autowired
    public RabbitMQController(SchedulerFactoryBean schedulerFactory) {
        this.schedulerFactory = schedulerFactory;
    }

    private JobDetail jobDetail;
    @Autowired
    @Qualifier ("manuallyTriggeredJob")
    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

    private SimpleTrigger simpleTrigger;
    @Autowired
    @Qualifier ("simpleTrigger")
    public void setSimpleTrigger(SimpleTrigger simpleTrigger){
        this.simpleTrigger = simpleTrigger;
    }

    private CommentModel processedResult = new CommentModel();

    @RequestMapping("/")
    public ModelAndView  home(Model model) {
        return new ModelAndView("comment", "commentForm", new CommentModel());
    }

    @PostMapping (value = "/comment")
    public String postAComment(@ModelAttribute("commentForm") CommentModel comment, Model model) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        jobDetail.getJobDataMap().put(ParameterConst.PARAMETER_COMMENT, comment);
        scheduler.scheduleJob(jobDetail, simpleTrigger);
        return "result";
    }

    @PostMapping (value="/result")
    public void catchResultResponse(@RequestBody String request, Model model) throws IOException {
        CommentModel result = JsonTransformationsUtil.parseStringToCommentModel(request);
        processedResult.setCommentContent(result.getCommentContent());
    }

    @GetMapping (value = "/result")
    public String seeProcessedResult(Model model){
        model.addAttribute("result", processedResult);
        return "result";
    }
}