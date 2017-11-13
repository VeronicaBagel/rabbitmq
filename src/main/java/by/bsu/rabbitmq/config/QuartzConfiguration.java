package by.bsu.rabbitmq.config;

import by.bsu.rabbitmq.constant.ParameterConst;
import by.bsu.rabbitmq.job.CustomJob;
import by.bsu.rabbitmq.model.CommentModel;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;


@Configuration
public class QuartzConfiguration {
    private ApplicationContext applicationContext;
    @Autowired
    public QuartzConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SchedulerFactoryBean quartzScheduler() throws SchedulerException {
        SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();

        quartzScheduler.setQuartzProperties(quartzPropertiesAsAWhole());
        quartzScheduler.setOverwriteExistingJobs(true);

        AutowiredSpringBeanJobFactory jobFactory = new AutowiredSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        quartzScheduler.setJobFactory(jobFactory);

        Trigger[] triggers = {
                processMyJobTrigger().getObject()
        };

        quartzScheduler.setTriggers(triggers);

        return quartzScheduler;
    }

    @Bean
    public JobDetailFactoryBean scheduledJob() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(CustomJob.class);
        jobDetailFactory.setName(quartzPropertiesAsAWhole().getProperty("quartz.scheduledJobName"));
        jobDetailFactory.setGroup(quartzPropertiesAsAWhole().getProperty("quartz.scheduledJobGroupName"));

        JobDataMap data = new JobDataMap();
        data.put(ParameterConst.PARAMETER_COMMENT, new CommentModel(quartzPropertiesAsAWhole()
                .getProperty("quartz.testComment")));
        jobDetailFactory.setJobDataMap(data);

        jobDetailFactory.setDurability(true);

        return jobDetailFactory;
    }

    @Bean (name = "manuallyTriggeredJob")
    public JobDetail getManuallyTriggeredJob() {
        JobDetail job = newJob(CustomJob.class)
                .withIdentity(quartzPropertiesAsAWhole().getProperty("quartz.manuallyTriggeredJobName"),
                        quartzPropertiesAsAWhole().getProperty("quartz.manuallyTriggeredJobGroupName"))
                .storeDurably()
                .build();
        return job;
    }

    @Bean (name = "simpleTrigger")
    public SimpleTrigger getSimpleTrigger() {
        SimpleTrigger simpleTrigger = (SimpleTrigger) newTrigger()
                .startAt(futureDate(0, DateBuilder.IntervalUnit.SECOND))
                .forJob(JobKey
                        .jobKey(quartzPropertiesAsAWhole().getProperty("quartz.manuallyTriggeredJobName"),
                                quartzPropertiesAsAWhole().getProperty("quartz.manuallyTriggeredJobGroupName")))
                .build();
        return simpleTrigger;
    }

    @Bean
    public CronTriggerFactoryBean processMyJobTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(scheduledJob().getObject());
        cronTriggerFactoryBean.setCronExpression(quartzPropertiesAsAWhole().getProperty("quartz.cronTrigger"));
        return cronTriggerFactoryBean;
    }

    @Bean (name = "quartzProperties")
    public Properties quartzPropertiesAsAWhole() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
        Properties properties;

        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to load quartz.properties", e);
        }

        return properties;
    }


}
