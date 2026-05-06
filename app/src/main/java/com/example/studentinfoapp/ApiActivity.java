package com.example.studentinfoapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiActivity extends AppCompatActivity {

    private EditText editPostTitle, editPostBody;
    private Button btnCreatePost;
    private ProgressBar progressBar;
    private ListView listViewPosts;

    private ExecutorService executorService;
    private Handler mainHandler;
    private List<String> displayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        editPostTitle = findViewById(R.id.editPostTitle);
        editPostBody = findViewById(R.id.editPostBody);
        btnCreatePost = findViewById(R.id.btnCreatePost);
        progressBar = findViewById(R.id.progressBar);
        listViewPosts = findViewById(R.id.listViewPosts);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        listViewPosts.setAdapter(adapter);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        btnCreatePost.setOnClickListener(v -> submitPost());

        loadDataFromApi();
    }

    private void loadDataFromApi() {
        progressBar.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            try {
                List<Post> posts = ApiClient.fetchPosts();

                mainHandler.post(() -> {
                    displayList.clear();
                    for (Post p : posts) {
                        displayList.add(p.getId() + " - " + p.getTitle());
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Lỗi mạng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void submitPost() {
        String title = editPostTitle.getText().toString().trim();
        String body = editPostBody.getText().toString().trim();

        if (title.isEmpty() || body.isEmpty()) {
            Toast.makeText(this, "Bạn cần nhập đầy đủ Tiêu đề và Nội dung", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnCreatePost.setEnabled(false);

        executorService.execute(() -> {
            try {
                boolean success = ApiClient.createPost(title, body);

                mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnCreatePost.setEnabled(true);

                    if (success) {
                        Toast.makeText(this, "Tạo bài viết thành công (Status 201)!", Toast.LENGTH_SHORT).show();
                        editPostTitle.setText("");
                        editPostBody.setText("");
                        loadDataFromApi();
                    } else {
                        Toast.makeText(this, "Có lỗi xảy ra khi tạo bài viết.", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnCreatePost.setEnabled(true);
                    Toast.makeText(this, "Lỗi mạng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}