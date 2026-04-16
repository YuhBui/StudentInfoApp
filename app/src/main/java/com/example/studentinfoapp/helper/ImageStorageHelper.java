package com.example.studentinfoapp.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageStorageHelper {
    private Context context;

    public ImageStorageHelper(Context context) {
        this.context = context;
    }

    public String saveImage(Bitmap bitmap, String filename) throws IOException {
        File internalStorageDir = context.getFilesDir();
        File imageFile = new File(internalStorageDir, filename);
        FileOutputStream fos = new FileOutputStream(imageFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
        fos.close();
        return imageFile.getAbsolutePath();
    }

    public Bitmap loadImage(String filename, int targetWidth) {
        File internalStorageDir = context.getFilesDir();
        File imageFile = new File(internalStorageDir, filename);

        if (!imageFile.exists()) return null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        int imageWidth = options.outWidth;
        int inSampleSize = 1;

        if (imageWidth > targetWidth) {
            inSampleSize = imageWidth / targetWidth;
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
    }

    public File[] getAllImages() {
        return context.getFilesDir().listFiles((dir, name) ->
                name.endsWith(".jpg") || name.endsWith(".png")
        );
    }

    public void deleteImage(String filename) {
        File imageFile = new File(context.getFilesDir(), filename);
        if (imageFile.exists()) {
            boolean deleted = imageFile.delete();
            if (deleted) {
                Toast.makeText(context, "Đã xóa ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }
}