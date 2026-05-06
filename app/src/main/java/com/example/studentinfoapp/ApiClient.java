package com.example.studentinfoapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ApiClient {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/posts";

    // BÀI TẬP 2: GET Request
    public static List<Post> fetchPosts() throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            List<Post> posts = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(response.toString());
            for (int i = 0; i < Math.min(10, jsonArray.length()); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Post post = new Post();
                post.setId(obj.getInt("id"));
                post.setTitle(obj.getString("title"));
                post.setBody(obj.getString("body"));
                posts.add(post);
            }
            return posts;
        } else {
            throw new Exception("Lỗi HTTP: " + conn.getResponseCode());
        }
    }

    public static boolean createPost(String title, String body) throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("title", title);
        jsonBody.put("body", body);
        jsonBody.put("userId", 1);

        OutputStream out = conn.getOutputStream();
        out.write(jsonBody.toString().getBytes(StandardCharsets.UTF_8));
        out.close();

        return conn.getResponseCode() == 201;
    }
}