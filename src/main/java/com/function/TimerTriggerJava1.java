package com.function;

import java.io.IOException;
import java.time.*;
import com.microsoft.azure.functions.annotation.*;

import okhttp3.Call;
import okhttp3.MediaType;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Timer trigger.
 */
public class TimerTriggerJava1 {
    /**
     * This function will be invoked periodically according to the specified schedule.
     * @throws IOException 
     */
    @FunctionName("TimerTriggerJava1")
    public void run(
        @TimerTrigger(name = "timerInfo", schedule = "0 */5 * * * *") String timerInfo,
        final ExecutionContext context
    ) throws IOException {

        //get weather data from rest provider
        OkHttpClient weatherClient = new OkHttpClient();
        Request request = new  Request.Builder()
        .url("https://api.weatherapi.com/v1/current.json?q=lalibela")
        .header("key", "**************************")
        .get()
        .build();

        Call call = weatherClient.newCall(request);

        Response response = call.execute();

        String responseString = response.body().string();

        //Make a post request to the Laravel end point
        OkHttpClient laravClient = new OkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        @SuppressWarnings("deprecation")
        RequestBody requestBody =  RequestBody.create(
           JSON,  responseString 
        );

        Request laravelPostRequest = new Request.Builder()
        .url("")
        .post(requestBody)
        .build();

        laravClient.newCall(laravelPostRequest);
        
         context.getLogger().info("Java Timer trigger function executed at: " + LocalDateTime.now());
         context.getLogger().info("system call happened: " + responseString);
    }
}
