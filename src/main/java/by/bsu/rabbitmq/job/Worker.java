package by.bsu.rabbitmq.job;

import by.bsu.rabbitmq.constant.ParameterConst;
import by.bsu.rabbitmq.util.JsonTransformationsUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.quartz.SchedulerException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class Worker {
    private Logger logger = Logger.getLogger(Worker.class);

    private Properties azureProperties;
    @Autowired
    public void setAzureProperties(Properties azureProperties) {
        this.azureProperties = azureProperties;
    }

    @RabbitListener (queues = {"messages"})
    public void worker(String message) throws IOException, SchedulerException {
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put(ParameterConst.PARAMETER_COMMENT, message);

        CloseableHttpResponse response = callAzureFunctions(jsonRequest);
        String stringResponse = EntityUtils.toString(response.getEntity());

        logger.info("Processed comment: " + stringResponse);

        callResponseProcessing(stringResponse);
    }

    private CloseableHttpResponse callAzureFunctions(JSONObject jsonObject) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(azureProperties.getProperty("url.request.path"));

        StringEntity entity = new StringEntity(jsonObject.toString(), azureProperties.getProperty("settings.charset"));
        entity.setContentType(azureProperties.getProperty("settings.contentType"));
        httpPost.setEntity(entity);

        CloseableHttpResponse response = client.execute(httpPost);

        return response;
    }

    private void callResponseProcessing(String stringResponse) throws IOException {
        String jsonLikeResponse = JsonTransformationsUtil.setUpJsonLikeStringContent(stringResponse);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(azureProperties.getProperty("url.response.path"));

        StringEntity entity = new StringEntity(jsonLikeResponse, azureProperties.getProperty("settings.charset"));
        entity.setContentType(azureProperties.getProperty("settings.contentType"));
        httpPost.setEntity(entity);

        client.execute(httpPost);
    }

}
