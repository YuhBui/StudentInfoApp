package com.example.studentinfoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList = new ArrayList<>();
    private OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    public TaskAdapter(OnTaskClickListener listener) {
        this.listener = listener;
    }

    public void setTasks(List<Task> tasks) {
        this.taskList = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(taskList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleView, dateView;
        ImageView priorityIcon;
        CheckBox completionCheckbox;

        public TaskViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.taskTitle);
            dateView = itemView.findViewById(R.id.taskDate);
            priorityIcon = itemView.findViewById(R.id.priorityIcon);
            completionCheckbox = itemView.findViewById(R.id.checkComplete);
        }

        public void bind(Task task, OnTaskClickListener listener) {
            titleView.setText(task.getTitle());
            dateView.setText(task.getDueDate());
            completionCheckbox.setChecked(task.isCompleted());

            if (task.getPriority() == 2) priorityIcon.setImageResource(android.R.drawable.ic_dialog_alert);
            else priorityIcon.setImageResource(android.R.drawable.ic_menu_info_details);

            itemView.setOnClickListener(v -> listener.onTaskClick(task));
        }
    }
}