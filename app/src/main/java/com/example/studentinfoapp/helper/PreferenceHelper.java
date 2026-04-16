package com.example.studentinfoapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private static final String PREF_NAME = "app_settings";
    private static final String KEY_THEME = "theme";
    private static final String KEY_NOTIFY = "notifications_enabled";
    private static final String KEY_SORT = "sort_by";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public PreferenceHelper(Context context) {
        sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setTheme(String theme) {
        editor.putString(KEY_THEME, theme);
        editor.apply(); // Lưu bất đồng bộ [cite: 3608]
    }

    public String getTheme() {
        return sp.getString(KEY_THEME, "System");
    }

    public void setNotificationsEnabled(boolean enabled) {
        editor.putBoolean(KEY_NOTIFY, enabled);
        editor.apply();
    }

    public boolean isNotificationsEnabled() {
        return sp.getBoolean(KEY_NOTIFY, true);
    }

    public void setSortBy(String sortBy) {
        editor.putString(KEY_SORT, sortBy);
        editor.apply();
    }

    public String getSortBy() {
        return sp.getString(KEY_SORT, "Date");
    }

    // Hàm dùng để đặt lại toàn bộ cài đặt (Bài 10.2)
    public void resetToDefault() {
        editor.clear();
        editor.apply();
    }
}