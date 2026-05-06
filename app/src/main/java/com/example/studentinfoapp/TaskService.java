package com.example.studentinfoapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class TaskService extends JobIntentService {
    static final int JOB_ID = 1000;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, TaskService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String taskTitle = intent.getStringExtra("title");

        Log.d("TaskService", "Bắt đầu lưu task: " + taskTitle);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("TaskService", "Đã xử lý xong (lưu DB thành công): " + taskTitle);
    }
}