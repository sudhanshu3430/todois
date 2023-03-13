package com.bawp.todoister.Util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bawp.todoister.data.AbstractDao;
import com.bawp.todoister.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Task.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class TaskroomDatabase extends RoomDatabase {

    public static final int NUMBER_OF_THREADS = 4;
    public static final String DATABASE_NAME = "todoister_database";
    private static volatile TaskroomDatabase INSTANCE;
    public static final ExecutorService databaseWriteExeutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static final RoomDatabase.Callback sRoomDatabseCallkback =

            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriteExeutor.execute(() -> {
                        AbstractDao abstractDao = INSTANCE.abstractDao();
                        abstractDao.deleteAll();


                    });
                }
            };

    public static final TaskroomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskroomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TaskroomDatabase.class, DATABASE_NAME)
                            .addCallback(sRoomDatabseCallkback)
                            .build();
                }
            }
        }
        return INSTANCE;

    }
    public abstract AbstractDao abstractDao();

}
