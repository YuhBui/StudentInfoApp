package com.example.studentinfoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TaskListFragment extends Fragment {
    private RecyclerView recyclerView;
    private TaskViewModel viewModel;
    private TaskAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TaskAdapter(task -> {
            // Kiểm tra xem Container Detail có tồn tại trên màn hình này không
            boolean isTablet = getActivity().findViewById(R.id.taskDetailContainer) != null;

            if (isTablet) {
                // Đang mở trên Tablet: Load vào TaskDetailContainer
                TaskDetailFragment detailFrag = TaskDetailFragment.newInstance(task);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.taskDetailContainer, detailFrag)
                        .commit();
            } else {
                // Đang mở trên Phone: Nhảy sang TaskDetailActivity như cũ
                Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                intent.putExtra("TASK_TITLE", task.getTitle());
                intent.putExtra("TASK_DESC", task.getDescription());
                intent.putExtra("TASK_CATEGORY", task.getCategory());
                intent.putExtra("TASK_DEADLINE", task.getDueDate());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        viewModel.getTaskList().observe(getViewLifecycleOwner(), tasks -> {
            adapter.setTasks(tasks);
        });
    }
}