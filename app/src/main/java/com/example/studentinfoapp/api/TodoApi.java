package com.example.studentinfoapp.api;

import com.example.studentinfoapp.Task;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TodoApi {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @GET("todos")
    Call<List<Task>> getAllTasks();

    @GET("api/tasks/{id}")
    Call<Task> getTaskById(@Path("id") int id);

    @POST("api/tasks")
    Call<Task> createTask(@Body Task task);

    @PUT("api/tasks/{id}")
    Call<Task> updateTask(@Path("id") int id, @Body Task task);

    @DELETE("api/tasks/{id}")
    Call<Void> deleteTask(@Path("id") int id);
}