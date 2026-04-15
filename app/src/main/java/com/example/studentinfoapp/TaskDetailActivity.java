package com.example.studentinfoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentinfoapp.AddTaskActivity;
import com.example.studentinfoapp.R;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);
        TextView tvCategory = findViewById(R.id.tvDetailCategory);
        TextView tvDeadline = findViewById(R.id.tvDetailDeadline);
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);

        Intent intent = getIntent();
        String title = intent.getStringExtra("TASK_TITLE");
        String desc = intent.getStringExtra("TASK_DESC");
        String category = intent.getStringExtra("TASK_CATEGORY");
        String deadline = intent.getStringExtra("TASK_DEADLINE");

        tvTitle.setText(title);
        tvDesc.setText(desc);
        tvCategory.setText("Category: " + (category != null ? category : "N/A"));
        tvDeadline.setText("Deadline: " + (deadline != null ? deadline : "N/A"));

        btnEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(TaskDetailActivity.this, AddTaskActivity.class);
            startActivity(editIntent);
        });

        btnDelete.setOnClickListener(v -> {
            finish();
        });
    }
}