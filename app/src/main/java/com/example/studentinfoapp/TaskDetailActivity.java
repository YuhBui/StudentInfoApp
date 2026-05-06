package com.example.studentinfoapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskDetailActivity extends AppCompatActivity {

    private TaskViewModel viewModel;
    private int taskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);
        TextView tvCategory = findViewById(R.id.tvDetailCategory);
        TextView tvDeadline = findViewById(R.id.tvDetailDeadline);
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);

        Intent intent = getIntent();
        taskId = intent.getIntExtra("TASK_ID", -1);
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
            editIntent.putExtra("EDIT_TASK_ID", taskId); // Gửi ID int đi
            editIntent.putExtra("EDIT_TASK_TITLE", title);
            editIntent.putExtra("EDIT_TASK_DESC", desc);
            editIntent.putExtra("EDIT_TASK_CATEGORY", category);
            editIntent.putExtra("EDIT_TASK_DEADLINE", deadline);
            startActivity(editIntent);
            finish();
        });

        btnDelete.setOnClickListener(v -> {
            if (taskId != -1) {
                Task taskToDelete = new Task("", "", "", "", "", false);
                taskToDelete.setId(taskId);

                viewModel.delete(taskToDelete);
            }
            finish();
        });

        TextView tvDetailDeadline = findViewById(R.id.tvDetailDeadline);
        Button btnSetReminder = findViewById(R.id.btnSetReminder);

        btnSetReminder.setOnClickListener(v -> {
            String deadlineStr = tvDetailDeadline.getText().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            try {
                Date deadlineDate = sdf.parse(deadlineStr);
                if (deadlineDate != null) {
                    long deadlineMillis = deadlineDate.getTime();

                    // 3. Tính toán: Thời gian báo thức = Deadline - 1 giờ (3.600.000 ms)
                    long oneHourInMillis = 60 * 60 * 1000;
                    long triggerTime = deadlineMillis - oneHourInMillis;

                    // 4. Kiểm tra xem thời gian báo thức đã trôi qua chưa
                    if (triggerTime > System.currentTimeMillis()) {

                        // Lấy taskId hiện tại (Giả sử bạn đã nhận từ Intent trước đó)
                        int taskId = getIntent().getIntExtra("task_id", -1);

                        // Gọi hàm đặt báo thức
                        scheduleTaskReminder(this, taskId, triggerTime);
                        Toast.makeText(this, "Đã hẹn giờ nhắc nhở trước 1 tiếng!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(this, "Không thể hẹn giờ cho thời gian trong quá khứ!", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi đọc định dạng ngày tháng!", Toast.LENGTH_SHORT).show();
            }
        });
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