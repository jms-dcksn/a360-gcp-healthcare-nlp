package com.automationanywhere.botcommand.samples.Utils;
import com.automationanywhere.botcommand.exception.BotCommandException;
import okhttp3.*;
import org.apache.http.HttpEntity;
import java.io.FileOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HTTPRequest {

    public static String Request(String url, String method, String auth) throws IOException, ParseException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method(method, null)
                .addHeader("Authorization", auth)
                //.addHeader("Cookie", "BIGipServerpool_dev117003=427929098.59456.0000; JSESSIONID=7F99F03A11EAD961AAFC4987A02DFA05; glide_session_store=6FFDE7AB1B2230104B7A0F26624BCBD4; glide_user_activity=U0N2MzorNXZYeFJncVo4eUNZZjZVMUhQZG5tOEhBdjBFa1BWdjo3b09lNkRqT016d2VJbTNHeVZBSENBQ3dmTDVlOTJ3ZURrUklwSU50blM4PQ==; glide_user_route=glide.73d6d0a9ea8868a1a5a301c9c850c8cf")
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            throw new BotCommandException("Exception occurred making the request: " + e);
        }
        if(response.code() >= 400) {
            throw new BotCommandException("Could not complete the action. Code: " + response.code() + "Response Body: " + Objects.requireNonNull(response.body()).string());
        }
        return Objects.requireNonNull(response.body()).string();
    }

    public static String oAuthMethod(String url, String clientId, String clientSecret) throws IOException {
        String clientAuth = clientId + ":" + clientSecret;
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString(clientAuth.getBytes());
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials");
        Request request = new Request.Builder()
                .url("https://login." + url + "/oauth/token")
                .method("POST", body)
                .addHeader("Authorization", basicAuthPayload)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            throw new BotCommandException("Exception occurred making the request: " + e);
        }
        if(response.code() >= 400) {
            throw new BotCommandException("Genesys did not accept the request. Code: " + response.code() + "Response Body: " + Objects.requireNonNull(response.body()).string());
        }
        return Objects.requireNonNull(response.body()).string();
    }

    public static String SEND(String auth, String url, String method, String body) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, body);
        Request request = new Request.Builder()
                .url(url)
                .method(method, requestBody)
                .addHeader("Authorization", auth)
                //.addHeader("Content-Type", "multipart/form-data")
                //.addHeader("Cookie", "b=bqjwz6qwia1zsqok1ring9va3")
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            throw new BotCommandException("Exception occurred making the request: " + e);
        }
        if(response.code() >= 400) {
            throw new BotCommandException("The request was not accepted. Code: " + response.code() + "Response Body: " + Objects.requireNonNull(response.body()).string());
        }
        return Objects.requireNonNull(response.body()).string();
    }

    public static String POSTwFile(String url, String filepath) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", filepath,
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(filepath)))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Cookie", "b=bqjwz6qwia1zsqok1ring9va3")
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
}
