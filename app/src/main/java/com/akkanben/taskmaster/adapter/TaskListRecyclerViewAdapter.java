package com.akkanben.taskmaster.adapter;

import static com.akkanben.taskmaster.R.color.purple_200;
import static com.akkanben.taskmaster.R.color.purple_500;
import static com.akkanben.taskmaster.R.color.purple_700;
import static com.akkanben.taskmaster.R.color.teal_200;
import static com.akkanben.taskmaster.activity.MainActivity.TASK_ATTACHMENT_EXTRA_TAG;
import static com.akkanben.taskmaster.activity.MainActivity.TASK_DESCRIPTION_EXTRA_TAG;
import static com.akkanben.taskmaster.activity.MainActivity.TASK_LOCATION_EXTRA_TAG;
import static com.akkanben.taskmaster.activity.MainActivity.TASK_NAME_EXTRA_TAG;
import static com.akkanben.taskmaster.activity.MainActivity.TASK_STATUS_EXTRA_TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akkanben.taskmaster.R;
import com.akkanben.taskmaster.activity.TaskDetailActivity;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStatus;

import java.util.List;

public class TaskListRecyclerViewAdapter extends RecyclerView.Adapter<TaskListRecyclerViewAdapter.TaskListViewHolder> {
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
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Button taskFragmentButton = holder.itemView.findViewById(R.id.button_task_list_fragment_task_list_item);
        String taskName = taskList.get(position).getTitle();
        taskFragmentButton.setText(taskName);
        @ColorInt int color;
        color = setStatusColor(taskList.get(position).getStatus());
        taskFragmentButton.setBackgroundColor(color);
        taskFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTaskDetails = new Intent(callingActivity, TaskDetailActivity.class);
                goToTaskDetails.putExtra(TASK_NAME_EXTRA_TAG, taskName);
                goToTaskDetails.putExtra(TASK_DESCRIPTION_EXTRA_TAG, taskList.get(position).getBody());
                goToTaskDetails.putExtra(TASK_STATUS_EXTRA_TAG, taskList.get(position).getStatus().toString());
                goToTaskDetails.putExtra(TASK_ATTACHMENT_EXTRA_TAG, taskList.get(position).getAttachment());
                goToTaskDetails.putExtra(TASK_LOCATION_EXTRA_TAG, taskList.get(position).getLocation());
                callingActivity.startActivity(goToTaskDetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void updateListData(List<Task> updatedList) {
        int taskListSize = taskList.size();
        taskList = updatedList;
        while(taskListSize < updatedList.size())
            this.notifyItemInserted(taskListSize++);
        //TODO add for removals
    }

    public static class TaskListViewHolder extends RecyclerView.ViewHolder {
        public TaskListViewHolder(View fragmentItemView) {
            super(fragmentItemView);
        }
    }

    private @ColorInt int setStatusColor(TaskStatus status) {
        switch(status) {
            case COMPLETE:
                return callingActivity.getColor(purple_200);
            case ASSIGNED:
                return callingActivity.getColor(purple_500);
            case IN_PROGRESS:
                return callingActivity.getColor(purple_700);
            default:
                return callingActivity.getColor(teal_200);
        }
    }

}
