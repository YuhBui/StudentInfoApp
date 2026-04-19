package com.example.studentinfoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class TaskDetailActivity extends AppCompatActivity {

    private TaskViewModel viewModel;
    private int taskId = -1; // Đổi sang kiểu int

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Khởi tạo ViewModel theo chuẩn Lab 15
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);
        TextView tvCategory = findViewById(R.id.tvDetailCategory);
        TextView tvDeadline = findViewById(R.id.tvDetailDeadline);
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);

        Intent intent = getIntent();
        // Lấy ID kiểu int (mặc định là -1 nếu không có)
        taskId = intent.getIntExtra("TASK_ID", -1);
        String title = intent.getStringExtra("TASK_TITLE");
        String desc = intent.getStringExtra("TASK_DESC");
        String category = intent.getStringExtra("TASK_CATEGORY");
        String deadline = intent.getStringExtra("TASK_DEADLINE");

        tvTitle.setText(title);
        tvDesc.setText(desc);
        tvCategory.setText("Category: " + (category != null ? category : "N/A"));
        tvDeadline.setText("Deadline: " + (deadline != null ? deadline : "N/A"));

        // ===== NÚT SỬA =====
        btnEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(TaskDetailActivity.this, AddTaskActivity.class);
            editIntent.putExtra("EDIT_TASK_ID", taskId); // Gửi ID int đi
            editIntent.putExtra("EDIT_TASK_TITLE", title);
            editIntent.putExtra("EDIT_TASK_DESC", desc);
            editIntent.putExtra("EDIT_TASK_CATEGORY", category);
            editIntent.putExtra("EDIT_TASK_DEADLINE", deadline);
            startActivity(editIntent);
            finish();
        });

        // ===== NÚT XÓA =====
        btnDelete.setOnClickListener(v -> {
            if (taskId != -1) {
                // Tạo một đối tượng Task tạm để gọi lệnh Delete trong Room
                Task taskToDelete = new Task("", "", "", "", "", false);
                taskToDelete.setId(taskId);

                // Xóa bằng ViewModel
                viewModel.delete(taskToDelete);
            }
            // Đóng màn hình, quay về MainActivity (danh sách sẽ tự động update)
            finish();
        });
    }
}