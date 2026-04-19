package com.example.studentinfoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class AddTaskActivity extends AppCompatActivity {
    private EditText editTitle, editDesc, editDueDate;
    private Spinner spinnerCategory;
    private RadioGroup radioGroupPriority;
    private CheckBox checkCompleted;
    private Button btnSave;

    private TaskViewModel viewModel;
    private int editTaskId = -1; // Chuyển sang int

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Khởi tạo ViewModel theo chuẩn Lab 15
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        editTitle = findViewById(R.id.editTaskTitle);
        editDesc = findViewById(R.id.editTaskDesc);
        editDueDate = findViewById(R.id.editDueDate);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        radioGroupPriority = findViewById(R.id.radioGroupPriority);
        checkCompleted = findViewById(R.id.checkCompleted);
        btnSave = findViewById(R.id.btnSave);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // ===== KIỂM TRA CHẾ ĐỘ SỬA =====
        Intent intent = getIntent();
        editTaskId = intent.getIntExtra("EDIT_TASK_ID", -1);

        if (editTaskId != -1) {
            editTitle.setText(intent.getStringExtra("EDIT_TASK_TITLE"));
            editDesc.setText(intent.getStringExtra("EDIT_TASK_DESC"));
            editDueDate.setText(intent.getStringExtra("EDIT_TASK_DEADLINE"));

            String category = intent.getStringExtra("EDIT_TASK_CATEGORY");
            if (category != null) {
                int spinnerPosition = adapter.getPosition(category);
                spinnerCategory.setSelection(spinnerPosition);
            }
            btnSave.setText("Edit Task");
        }

        btnSave.setOnClickListener(v -> saveTask());
    }

    private void saveTask() {
        String title = editTitle.getText().toString().trim();
        String dueDate = editDueDate.getText().toString().trim();
        String desc = editDesc.getText().toString().trim();

        if (title.isEmpty() || dueDate.isEmpty()) {
            Toast.makeText(this, "Title and Due Date cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isCompleted = checkCompleted.isChecked();
        String category = spinnerCategory.getSelectedItem().toString();

        String priority = "Low";
        int selectedId = radioGroupPriority.getCheckedRadioButtonId();
        if (selectedId == R.id.radioMed) priority = "Medium";
        else if (selectedId == R.id.radioHigh) priority = "High";

        if (editTaskId != -1) {
            // ----- TRƯỜNG HỢP SỬA (UPDATE) -----
            Task updatedTask = new Task(title, desc, dueDate, category, priority, isCompleted);
            updatedTask.setId(editTaskId); // Gán lại ID cũ để Room biết đường lưu đè

            viewModel.update(updatedTask); // Cập nhật qua ViewModel

            Toast.makeText(this, "Task updated!", Toast.LENGTH_SHORT).show();

            // Xóa hết Activity ở trên và quay về Trang chủ
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            finish();

        } else {
            // ----- TRƯỜNG HỢP THÊM MỚI (INSERT) -----
            // Giữ nguyên logic trả dữ liệu về MainActivity để MainActivity tự Insert
            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_TASK_TITLE", title);
            resultIntent.putExtra("NEW_TASK_DESC", desc);
            resultIntent.putExtra("NEW_TASK_DUE_DATE", dueDate);
            resultIntent.putExtra("NEW_TASK_CATEGORY", category);
            resultIntent.putExtra("NEW_TASK_PRIORITY", priority);
            resultIntent.putExtra("NEW_TASK_COMPLETED", isCompleted);

            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}