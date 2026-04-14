package com.example.studentinfoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentinfoapp.R;

public class AddTaskActivity extends AppCompatActivity {
    private static final String TAG = "AddTaskActivity_Log";

    private EditText editTitle, editDesc, editDueDate;
    private Spinner spinnerCategory;
    private RadioGroup radioGroupPriority;
    private CheckBox checkCompleted;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Log.d(TAG, "onCreate called");

        editTitle = findViewById(R.id.editTaskTitle);
        editDesc = findViewById(R.id.editTaskDesc);
        editDueDate = findViewById(R.id.editDueDate);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        radioGroupPriority = findViewById(R.id.radioGroupPriority);
        checkCompleted = findViewById(R.id.checkCompleted);
        btnSave = findViewById(R.id.btnSave);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        btnSave.setOnClickListener(v -> saveTask());
    }

    private void saveTask() {
        String title = editTitle.getText().toString().trim();
        String dueDate = editDueDate.getText().toString().trim();
        String desc = editDesc.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "The task title must not be left blank!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dueDate.isEmpty()) {
            Toast.makeText(this, "The due date must not be left blank!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isCompleted = checkCompleted.isChecked();
        String category = spinnerCategory.getSelectedItem().toString();

        String priority = "Low";
        int selectedId = radioGroupPriority.getCheckedRadioButtonId();
        if (selectedId == R.id.radioMed) {
            priority = "Medium";
        } else if (selectedId == R.id.radioHigh) {
            priority = "High";
        }

        Log.d(TAG, "Task saved: " + title + " | Priority: " + priority + " | Done: " + isCompleted);
        Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("NEW_TASK_TITLE", title);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("SAVED_TITLE", editTitle.getText().toString());
        Log.d(TAG, "onSaveInstanceState: Instance state saved");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String savedTitle = savedInstanceState.getString("SAVED_TITLE", "");
        editTitle.setText(savedTitle);
        Log.d(TAG, "onRestoreInstanceState: Instance state restored");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }
}