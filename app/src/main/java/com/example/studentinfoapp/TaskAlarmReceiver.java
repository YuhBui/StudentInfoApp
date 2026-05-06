package com.example.studentinfoapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;

public class TaskAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Lấy ID của task được truyền qua từ báo thức
        int taskId = intent.getIntExtra("task_id", -1);
        Log.d("TaskAlarmReceiver", "Đã đến giờ báo thức cho task: " + taskId);

        showTaskReminderNotification(context, taskId);
    }

    private void showTaskReminderNotification(Context ctx, int taskId) {
        String channelId = "task_reminders";

        // Tạo Intent để mở TaskDetailActivity khi bấm vào thông báo
        Intent intent = new Intent(ctx, TaskDetailActivity.class);
        intent.putExtra("task_id", taskId);

        // Bắt buộc dùng FLAG_IMMUTABLE cho API cao
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, taskId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Xây dựng thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Nhớ đổi icon cho phù hợp
                .setContentTitle("Task Reminder")
                .setContentText("Bạn có một công việc sắp đến hạn! Nhấn để xem chi tiết.")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManager manager = ctx.getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.notify(taskId, builder.build());
        }
    }
}