package com.example.studentinfoapp;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Khai báo danh sách các Entity và version
@Database(entities = {Task.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDao taskDao(); // Cung cấp Dao để thao tác

    private static volatile AppDatabase INSTANCE;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class, "taskmanager.db")
                    .allowMainThreadQueries()
                    // 2. THÊM DÒNG NÀY: Cho phép Room tự động xóa bảng cũ và tạo bảng mới khi version tăng
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}