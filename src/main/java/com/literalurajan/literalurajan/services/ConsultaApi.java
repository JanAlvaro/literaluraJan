package com.literalurajan.literalurajan.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ConsultaApi {

    public String requestData(String url){
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request, BodyHandlers.ofString());

        try {
            HttpResponse<String> response = responseFuture.get();
            return response.body();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}