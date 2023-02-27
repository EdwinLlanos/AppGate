package com.appgate.authentication.network;

import android.util.Log;
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
    private static final String TAG = RestClientHandler.class.getCanonicalName();

    public <T> void execute(String endPoint, OnRequestCompletedListener<T> callable) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> futureResult = executor.submit(() -> createHttpURLConnection(endPoint));

        try {
            String result = futureResult.get();
            callable.onSuccess((T) result);
            Log.d(TAG, result);
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
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            data = stringBuilder.toString();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return data;
    }
}
