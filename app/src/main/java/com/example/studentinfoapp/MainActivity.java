package com.example.studentinfoapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.studentinfoapp.api.RetrofitClient;
import com.example.studentinfoapp.api.TodoApi;
import com.example.studentinfoapp.helper.PreferenceHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.TimeUnit;

// Import thư viện Retrofit
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TaskViewModel viewModel;

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

                    Task newTask = new Task(title, desc, dueDate, category, priority, isCompleted);
                    viewModel.insert(newTask);

//                    Intent serviceIntent = new Intent(MainActivity.this, TaskService.class);
//                    serviceIntent.putExtra("title", title);
//
//                    TaskService.enqueueWork(MainActivity.this, serviceIntent);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        viewModel.getTaskList().observe(this, tasks -> {
            Log.d("Lab15_Observe", "Danh sách task đã cập nhật, số lượng: " + tasks.size());
        });

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

        ImageButton btnSettings = findViewById(R.id.btnOpenSettings);
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        ImageButton btnGallery = findViewById(R.id.btnOpenGallery);
        btnGallery.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ImagePickerActivity.class);
            startActivity(intent);
        });

        Button btnThreading = findViewById(R.id.btnThreading);
        Button btnApi = findViewById(R.id.btnApi);

        btnThreading.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ThreadingExerciseActivity.class);
            startActivity(intent);
        });

        btnApi.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ApiActivity.class);
            startActivity(intent);
        });

        loadTasksFromApi();

        PeriodicWorkRequest syncRequest = new PeriodicWorkRequest.Builder(
                SyncWorker.class,
                15, TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "sync_tasks",
                ExistingPeriodicWorkPolicy.KEEP,
                syncRequest);
    }

    private void loadTasksFromApi() {
        TodoApi api = RetrofitClient.getApi();
        Call<List<Task>> call = api.getAllTasks();

        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> tasksFromApi = response.body();
                    Log.d("API_SUCCESS", "Số lượng tasks tải về từ API: " + tasksFromApi.size());

                    for (Task t : tasksFromApi) {
                        if (t.getDueDate() == null) t.setDueDate("Chưa có hạn");
                        if (t.getCategory() == null) t.setCategory("Mặc định");
                        if (t.getPriority() == null) t.setPriority("Bình thường");
                        if (t.getDescription() == null) t.setDescription("Dữ liệu tải từ API");

                        viewModel.insert(t);
                    }
                    Toast.makeText(MainActivity.this, "Đồng bộ API thành công!", Toast.LENGTH_SHORT).show();

                } else {
                    int statusCode = response.code();
                    if (statusCode == 401) {
                        Log.e("API_ERROR", "Lỗi 401: Không có quyền truy cập (Unauthorized)");
                        Toast.makeText(MainActivity.this, "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
                    } else if (statusCode == 404) {
                        Log.e("API_ERROR", "Lỗi 404: Không tìm thấy đường dẫn (Not Found)");
                    } else if (statusCode >= 500) {
                        Log.e("API_ERROR", "Lỗi 5xx: Máy chủ đang bị lỗi (Server Error)");
                    } else {
                        Log.e("API_ERROR", "Lỗi khác: " + statusCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                if (t instanceof java.io.IOException) {
                    Log.e("API_FAILURE", "Lỗi kết nối mạng: " + t.getMessage());
                    Toast.makeText(MainActivity.this, "Mạng không ổn định hoặc mất kết nối!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("API_FAILURE", "Lỗi không xác định: " + t.getMessage());
                }
            }
        });
    }

    public void onTaskSelected(Task task) {
        TaskDetailFragment detailFragment = new TaskDetailFragment();
        Bundle args = new Bundle();
        args.putInt("TASK_ID", task.getId());
        args.putString("TASK_TITLE", task.getTitle());
        args.putString("TASK_DESC", task.getDescription());
        args.putString("TASK_CATEGORY", task.getCategory());
        args.putString("TASK_DEADLINE", task.getDueDate());
        detailFragment.setArguments(args);

        if (findViewById(R.id.taskDetailContainer) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.taskDetailContainer, detailFragment)
                    .commit();
        } else {
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
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "task_reminders";
            CharSequence name = "Task Updates";
            int importance = NotificationManager.IMPORTANCE_HIGH; // Độ ưu tiên cao để hiện pop-up

            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription("Notifications for task reminders");
            channel.setShowBadge(true);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 500, 250, 500});

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public void scheduleTaskReminder(Context context, int taskId, long triggerTime) {
        Intent intent = new Intent(context, TaskAlarmReceiver.class);
        intent.putExtra("task_id", taskId);

        // Dùng FLAG_IMMUTABLE theo chuẩn bảo mật mới
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                taskId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            try {
                // Lên lịch báo thức chạy ngay cả khi máy ngủ (Doze mode)
                alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                );
                Log.d("AlarmScheduler", "Đã lên lịch báo thức thành công!");
            } catch (SecurityException e) {
                Log.e("AlarmScheduler", "Thiếu quyền SCHEDULE_EXACT_ALARM");
            }
        }
    }
}