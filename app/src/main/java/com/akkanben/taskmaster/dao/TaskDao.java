package com.akkanben.taskmaster.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.akkanben.taskmaster.model.Task;

import java.util.List;

@Dao
public interface TaskDao {
   @Insert
   public void insertTask(Task task);

   @Query("DELETE FROM Task")
   public void clearTasks();

   @Query("SELECT * FROM Task")
   public List<Task> findAll();

   @Query("SELECT COUNT(*) FROM Task")
   public int getTaskCount();

}
