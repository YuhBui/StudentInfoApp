package com.example.studentinfoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    // Phương thức này được gọi khi nhận được tín hiệu broadcast
    @Override
    public void onReceive(Context context, Intent intent) {
        // Kiểm tra xem tín hiệu nhận được có đúng là tín hiệu khởi động máy không
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // In ra logcat để kiểm tra
            Log.d("BootReceiver", "Thiết bị vừa khởi động xong! Hệ thống sẵn sàng.");

            // Nếu muốn, bạn có thể khởi động TaskService ở đây để chạy ngầm
            // Intent serviceIntent = new Intent(context, TaskService.class);
            // TaskService.enqueueWork(context, serviceIntent);
        }
    }
}