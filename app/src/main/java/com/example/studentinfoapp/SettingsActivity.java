package com.example.studentinfoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.studentinfoapp.helper.PreferenceHelper;

public class SettingsActivity extends AppCompatActivity {
    private PreferenceHelper prefHelper;
    private Spinner spinnerTheme;
    private Switch switchNotifications;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefHelper = new PreferenceHelper(this);
        spinnerTheme = findViewById(R.id.spinnerTheme);
        switchNotifications = findViewById(R.id.switchNotifications);
        btnReset = findViewById(R.id.btnReset);

        loadSettings();
        setupListeners();
    }

    private void loadSettings() {
        // Load trạng thái Switch
        switchNotifications.setChecked(prefHelper.isNotificationsEnabled());

        // Load trạng thái Spinner
        String theme = prefHelper.getTheme();
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerTheme.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(theme);
            spinnerTheme.setSelection(position);
        }
    }

    private void setupListeners() {
        switchNotifications.setOnCheckedChangeListener((view, isChecked) -> {
            prefHelper.setNotificationsEnabled(isChecked);
        });

        spinnerTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTheme = parent.getItemAtPosition(position).toString();
                prefHelper.setTheme(selectedTheme);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnReset.setOnClickListener(v -> {
            prefHelper.resetToDefault();
            loadSettings(); // Tải lại UI sau khi reset
            Toast.makeText(this, "Đã đặt lại về mặc định", Toast.LENGTH_SHORT).show();
        });
    }
}