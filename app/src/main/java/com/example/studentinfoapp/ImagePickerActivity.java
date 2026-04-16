package com.example.studentinfoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.studentinfoapp.helper.ImageStorageHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImagePickerActivity extends AppCompatActivity {
    private ImageView ivPreview;
    private ImageStorageHelper storageHelper;
    private static final int PICK_IMAGE = 100;
    private String currentSavedFilename = "";
    private ListView lvSavedImages;
    private ArrayAdapter<String> listAdapter;
    private List<String> fileListData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        ivPreview = findViewById(R.id.ivPreview);
        storageHelper = new ImageStorageHelper(this);

        Button btnPickImage = findViewById(R.id.btnPickImage);
        Button btnDelete = findViewById(R.id.btnDelete);

        // Mở Gallery
        btnPickImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        });

        // Xóa ảnh hiện tại
        btnDelete.setOnClickListener(v -> {
            if (!currentSavedFilename.isEmpty()) {
                storageHelper.deleteImage(currentSavedFilename);
                ivPreview.setImageBitmap(null); // Xóa trên màn hình
                currentSavedFilename = "";
            } else {
                Toast.makeText(this, "Chưa có ảnh nào để xóa", Toast.LENGTH_SHORT).show();
            }
        });

        // Đặt đoạn này trong onCreate(), sau khi setContentView
        lvSavedImages = findViewById(R.id.lvSavedImages);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileListData);
        lvSavedImages.setAdapter(listAdapter);

        // Tải danh sách file ngay khi vừa mở màn hình
        updateImageList();
    }

    // Nhận kết quả trả về từ Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                // Chuyển Uri thành Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                // Tạo tên file theo timestamp [cite: 1741]
                currentSavedFilename = "task_" + System.currentTimeMillis() + ".jpg";

                // Lưu vào Internal Storage
                storageHelper.saveImage(bitmap, currentSavedFilename);

                // Tải lên UI với chiều rộng tối đa 600px để test inSampleSize
                Bitmap optimizedBitmap = storageHelper.loadImage(currentSavedFilename, 600);
                ivPreview.setImageBitmap(optimizedBitmap);

                Toast.makeText(this, "Đã lưu: " + currentSavedFilename, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateImageList() {
        fileListData.clear(); // Xóa dữ liệu cũ

        File[] savedFiles = storageHelper.getAllImages(); // Lấy tất cả ảnh
        if (savedFiles != null) {
            for (File file : savedFiles) {
                // Lấy dung lượng file tính bằng Byte, chia 1024 để ra KB
                long fileSizeInKB = file.length() / 1024;

                // Format chuỗi hiển thị giống Hình 11.4: "task_001.jpg (45 KB)"
                String displayInfo = file.getName() + " (" + fileSizeInKB + " KB)";
                fileListData.add(displayInfo);
            }
        }

        // Báo cho giao diện biết dữ liệu đã thay đổi để vẽ lại danh sách
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }
}