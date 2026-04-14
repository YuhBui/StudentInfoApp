package com.example.studentinfoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity_Log";

    private final ActivityResultLauncher<Intent> addTaskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String newTitle = result.getData().getStringExtra("NEW_TASK_TITLE");
                    Log.d(TAG, "Nhận được task mới: " + newTitle);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate called");

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        androidx.cardview.widget.CardView cardTask1 = findViewById(R.id.cardTask1);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            addTaskLauncher.launch(intent);
        });

        cardTask1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
            intent.putExtra("TASK_TITLE", "Bài tập Toán cao cấp");
            intent.putExtra("TASK_DESC", "Làm bài tập chương 3 trên hệ thống học tập trực tuyến CMS.");
            intent.putExtra("TASK_CATEGORY", "Homework");
            intent.putExtra("TASK_DEADLINE", "10/04/2026");
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart called");
    }
}