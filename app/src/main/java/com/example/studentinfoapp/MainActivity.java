package com.example.studentinfoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.example.studentinfoapp.helper.PreferenceHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private TaskViewModel viewModel;

    private final ActivityResultLauncher<Intent> addTaskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String title = result.getData().getStringExtra("NEW_TASK_TITLE");
                    Task newTask = new Task(title, "N/A", "N/A", "N/A", 1);
                    viewModel.addTask(newTask);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new TaskListFragment())
                    .commit();
        }

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            addTaskLauncher.launch(intent);
        });

        // Trong hàm onCreate() của MainActivity.java
        ImageButton btnSettings = findViewById(R.id.btnOpenSettings);
        btnSettings.setOnClickListener(v -> {
            // Tạo Intent để chuyển từ MainActivity sang SettingsActivity [cite: 241, 258]
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // Khai báo và xử lý nút mở màn hình Quản lý ảnh
        ImageButton btnGallery = findViewById(R.id.btnOpenGallery);
        btnGallery.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ImagePickerActivity.class);
            startActivity(intent);
        });

        PreferenceHelper prefHelper = new PreferenceHelper(this);
        prefHelper.setTheme("Dark");
        Log.d("Lab10_Test", "Current Theme: " + prefHelper.getTheme());
    }

    private void applyTheme() {
        PreferenceHelper prefHelper = new PreferenceHelper(this);
        String theme = prefHelper.getTheme();

        if (theme.equals("Dark")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (theme.equals("Light")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyTheme();
    }
}