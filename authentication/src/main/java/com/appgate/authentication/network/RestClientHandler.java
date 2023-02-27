package com.appgate.authentication.network;

import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RestClientHandler {
    private static final int TIME_OUT = 5000;
    private static final int INITIAL_CAPACITY = 1024;

    public void execute(String endPoint, OnRequestCompletedListener<String> callable) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> futureResult = executor.submit(() -> createHttpURLConnection(endPoint));
        try {
            String result = futureResult.get();
            callable.onSuccess(result);
        } catch (InterruptedException | ExecutionException e) {
            callable.onError(e);
        } finally {
            executor.shutdown();
        }
    }

    private String createHttpURLConnection(String method) throws IOException {
        HttpURLConnection urlConnection = null;
        String data;
        try {
            URL url = new URL(method);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(TIME_OUT);
            urlConnection.setReadTimeout(TIME_OUT);
            try (InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder stringBuilder = new StringBuilder(INITIAL_CAPACITY);
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                data = stringBuilder.toString();
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return data;
    }
}
