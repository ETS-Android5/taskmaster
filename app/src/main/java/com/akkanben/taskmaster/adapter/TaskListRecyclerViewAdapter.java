package com.akkanben.taskmaster.adapter;

import static com.akkanben.taskmaster.activity.MainActivity.TASK_NAME_EXTRA_TAG;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akkanben.taskmaster.R;
import com.akkanben.taskmaster.activity.MainActivity;
import com.akkanben.taskmaster.activity.TaskDetailActivity;
import com.akkanben.taskmaster.model.Task;

import java.util.List;

public class TaskListRecyclerViewAdapter extends RecyclerView.Adapter {

    List<Task> taskList;
    Context callingActivity;


    public TaskListRecyclerViewAdapter(List<Task> taskList, Context callingActivity) {
        this.taskList = taskList;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task_list, parent, false);
        return new TaskListViewHolder(taskFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView taskFragmentTextView = holder.itemView.findViewById(R.id.text_view_task_list_fragment_task_name);
        String taskName = taskList.get(position).getTitle();
        taskFragmentTextView.setText(taskName);

        View taskViewHolder = holder.itemView;
        taskViewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTaskDetails = new Intent(callingActivity, TaskDetailActivity.class);
                goToTaskDetails.putExtra(TASK_NAME_EXTRA_TAG, taskName);
                callingActivity.startActivity(goToTaskDetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskListViewHolder extends RecyclerView.ViewHolder {
        public TaskListViewHolder(View fragmentItemView) {
            super(fragmentItemView);
        }
    }

}
