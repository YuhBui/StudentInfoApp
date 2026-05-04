package com.example.studentinfoapp.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    // Đổi link này thành URL API thực tế của bạn (ví dụ: https://jsonplaceholder.typicode.com/)
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            // 1. Tạo Interceptor để xem toàn bộ Log Request/Response trong tab Logcat
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY); // Level.BODY in ra cả nội dung JSON

            // 2. Cấu hình OkHttpClient với Timeout (30 giây) và tự động thử lại nếu rớt mạng
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(logging) // Gắn bộ ghi log vào Client
                    .build();

            // 3. Khởi tạo Retrofit và truyền custom client vào
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client) // <-- Bước quan trọng của Lab 20
                    .build();
        }
        return retrofit;
    }

    public static TodoApi getApi() {
        return getInstance().create(TodoApi.class);
    }
}