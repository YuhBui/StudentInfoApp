package com.example.studentinfoapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SyncWorker extends Worker {

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("SyncWorker", "Bắt đầu tiến trình đồng bộ ngầm...");

        // Trong thực tế, bạn sẽ lấy danh sách Task có syncStatus = "PENDING" từ Database (thông qua TaskDao)
        // Sau đó dùng vòng lặp gọi API POST (TodoApi.createTask) để đẩy lên server.
        // Khi API báo thành công, bạn cập nhật syncStatus thành "SYNCED" và lưu lại vào Database.

        try {
            // Giả lập việc đồng bộ dữ liệu mất 2 giây
            Thread.sleep(2000);
            Log.d("SyncWorker", "Đồng bộ thành công!");
            return Result.success();
        } catch (Exception e) {
            Log.e("SyncWorker", "Đồng bộ thất bại, sẽ thử lại sau.");
            return Result.retry();
        }
    }
}