//package com.example.studentinfoapp;
//
//import android.content.Context;
//import androidx.lifecycle.MutableLiveData;
//import com.example.studentinfoapp.helper.DatabaseHelper;
//import java.util.List;
//
//public class TaskRepository {
//    private DatabaseHelper dbHelper;
//    private MutableLiveData<List<Task>> tasksLiveData = new MutableLiveData<>();
//
//    public TaskRepository(Context context) {
//        dbHelper = new DatabaseHelper(context);
//        loadTasksFromDb(); // Tải dữ liệu ngay khi khởi tạo
//    }
//
//    // Đọc từ Database và cập nhật lên LiveData
//    public void loadTasksFromDb() {
//        List<Task> taskList = dbHelper.getAllTasks();
//        tasksLiveData.setValue(taskList);
//    }
//
//    public MutableLiveData<List<Task>> getTasks() {
//        return tasksLiveData;
//    }
//
//    public void insertTask(Task task) {
//        dbHelper.insertTask(task);
//        loadTasksFromDb(); // Load lại list sau khi thêm
//    }
//
//    public void deleteTask(int taskId) {
//        dbHelper.deleteTask(taskId);
//        loadTasksFromDb(); // Load lại list sau khi xóa
//    }
//}

package com.example.studentinfoapp;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    // Constructor nhận Application thay vì Context thông thường
    public TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        taskDao = db.taskDao();

        // Lấy danh sách Task dưới dạng LiveData ngay khi khởi tạo
        allTasks = taskDao.getAllTasks();
    }

    // Hàm cung cấp LiveData cho ViewModel quan sát
    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.insertTask(task);
        });
    }

    public void update(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.updateTask(task);
        });
    }

    public void delete(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.deleteTask(task);
        });
    }
}