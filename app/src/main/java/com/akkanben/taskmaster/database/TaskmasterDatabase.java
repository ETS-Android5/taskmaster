package com.akkanben.taskmaster.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.akkanben.taskmaster.dao.TaskDao;
import com.akkanben.taskmaster.model.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class TaskmasterDatabase extends RoomDatabase {
    public TaskDao taskDao;
}
