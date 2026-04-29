package com.example.studentinfoapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadingExerciseActivity extends AppCompatActivity {
    private TextView tvTaskStatus;
    private Button btnStartTasks;

    // Tạo Thread Pool với 3 luồng
    private ExecutorService executorService;
    private Handler mainHandler;
    private int completedTasks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threading_exercise);

        tvTaskStatus = findViewById(R.id.tvTaskStatus);
        btnStartTasks = findViewById(R.id.btnStartTasks);

        executorService = Executors.newFixedThreadPool(3);
        mainHandler = new Handler(Looper.getMainLooper());

        btnStartTasks.setOnClickListener(v -> executeTasks());
    }

    private void executeTasks() {
        completedTasks = 0;
        tvTaskStatus.setText("Đang xử lý...");
        btnStartTasks.setEnabled(false);

        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executorService.execute(() -> {
                long startTime = System.currentTimeMillis();
                Log.d("ThreadingTask", "Task " + taskId + " bắt đầu.");

                try {
                    // Giả lập thời gian chạy ngẫu nhiên từ 1 đến 3 giây
                    int sleepTime = new Random().nextInt(2000) + 1000;
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                Log.d("ThreadingTask", "Task " + taskId + " hoàn thành sau " + duration + "ms.");

                // Đẩy kết quả về Main Thread
                mainHandler.post(() -> {
                    completedTasks++;
                    tvTaskStatus.append("\nTask " + taskId + " xong (" + duration + "ms)");

                    if (completedTasks == 5) {
                        tvTaskStatus.append("\nTất cả 5 tác vụ đã hoàn thành!");
                        btnStartTasks.setEnabled(true);
                    }
                });
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}