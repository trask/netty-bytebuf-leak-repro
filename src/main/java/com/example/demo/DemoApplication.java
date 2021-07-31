package com.example.demo;

import com.azure.core.http.*;
import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class DemoApplication {

    private static final boolean READ_RESPONSE_BODY = false;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        sendLotsOfRequests();
        System.exit(0);
    }

    private static void sendLotsOfRequests() {
        HttpClient httpClient = new NettyAsyncHttpClientBuilder().build();
        HttpPipeline pipeline = new HttpPipelineBuilder().httpClient(httpClient).build();
        for (int i = 0; i < 100000; i++) {
            if (i % 1000 == 0) {
                System.out.println(i);
            }
            sendOneRequest(pipeline);
        }
    }

    private static void sendOneRequest(HttpPipeline pipeline) {
        HttpRequest request = new HttpRequest(HttpMethod.GET, "http://localhost:8080");
        HttpResponse response = pipeline.send(request).block();
        // only interested in status code
        if (response.getStatusCode() != 200) {
            throw new IllegalStateException("Unexpected status code: " + response.getStatusCode());
        }
        if (READ_RESPONSE_BODY) {
            response.getBodyAsString().block();
        }
    }
}
