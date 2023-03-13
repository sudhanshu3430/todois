package com.bawp.todoister.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.bawp.todoister.Util.TaskroomDatabase;
import com.bawp.todoister.model.Task;

import java.util.List;

public class DoisterRepository {
    private final AbstractDao abstractDao;
    private final LiveData<List<Task>> allTasks;

    public DoisterRepository(Application application) {
        TaskroomDatabase database = TaskroomDatabase.getDatabase(application);
        this.abstractDao = database.abstractDao();
        this.allTasks = abstractDao.getTask();
    }

    public LiveData<List<Task>> getAllTasks(){
        return allTasks;

    }
    public void insert(Task task){
        TaskroomDatabase.databaseWriteExeutor.execute(()-> abstractDao.insertTask(task));
    }
    public LiveData<Task> get(long taskId){return abstractDao.get(taskId);}

    public void delete(Task task){
        TaskroomDatabase.databaseWriteExeutor.execute(()-> abstractDao.delete(task));
    }

    public void update(Task task){
        TaskroomDatabase.databaseWriteExeutor.execute(()-> abstractDao.update(task));
    }

}
