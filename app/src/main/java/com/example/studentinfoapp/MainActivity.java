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

    // 1. SỬA LỖI N/A: Nhận đầy đủ dữ liệu từ Intent thay vì gán cứng "N/A"
    private final ActivityResultLauncher<Intent> addTaskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();

                    String title = data.getStringExtra("NEW_TASK_TITLE");
                    String desc = data.getStringExtra("NEW_TASK_DESC");
                    String dueDate = data.getStringExtra("NEW_TASK_DUE_DATE");
                    String category = data.getStringExtra("NEW_TASK_CATEGORY");
                    String priority = data.getStringExtra("NEW_TASK_PRIORITY");
                    boolean isCompleted = data.getBooleanExtra("NEW_TASK_COMPLETED", false);

                    // Tạo đối tượng Task chuẩn và lưu vào Database thông qua ViewModel
                    Task newTask = new Task(title, desc, dueDate, category, priority, isCompleted);
                    viewModel.insert(newTask);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyTheme(); // Áp dụng giao diện sáng/tối trước khi load layout

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 2. KHỞI TẠO VIEWMODEL
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // 3. QUAN SÁT DỮ LIỆU (LiveData): Tự động cập nhật UI khi database thay đổi
        viewModel.getTaskList().observe(this, tasks -> {
            Log.d("Lab15_Observe", "Danh sách task đã cập nhật, số lượng: " + tasks.size());
            // Lưu ý: Nếu Adapter của bạn nằm trong Fragment,
            // Fragment đó cũng nên observe cùng một ViewModel này để cập nhật RecyclerView.
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new TaskListFragment())
                    .commit();
        }

        // Nút thêm Task mới
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            addTaskLauncher.launch(intent);
        });

        // Nút cài đặt
        ImageButton btnSettings = findViewById(R.id.btnOpenSettings);
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // Nút mở bộ sưu tập ảnh
        ImageButton btnGallery = findViewById(R.id.btnOpenGallery);
        btnGallery.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ImagePickerActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Xử lý khi chọn một Task từ danh sách (Dành cho Tablet hoặc chuyển Fragment trên Phone)
     */
    public void onTaskSelected(Task task) {
        TaskDetailFragment detailFragment = new TaskDetailFragment();
        Bundle args = new Bundle();
        // Truyền đầy đủ thông tin sang Fragment chi tiết
        args.putInt("TASK_ID", task.getId());
        args.putString("TASK_TITLE", task.getTitle());
        args.putString("TASK_DESC", task.getDescription());
        args.putString("TASK_CATEGORY", task.getCategory());
        args.putString("TASK_DEADLINE", task.getDueDate());
        detailFragment.setArguments(args);

        if (findViewById(R.id.taskDetailContainer) != null) {
            // Chế độ TABLET: Hiển thị ở vùng bên phải
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.taskDetailContainer, detailFragment)
                    .commit();
        } else {
            // Chế độ PHONE: Thay thế Fragment chính và thêm vào BackStack
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, detailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void applyTheme() {
        PreferenceHelper prefHelper = new PreferenceHelper(this);
        String theme = prefHelper.getTheme();

        if ("Dark".equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if ("Light".equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyTheme();
        // Không cần gọi loadTasksFromDb() nữa vì LiveData đã tự động quan sát
    }
}