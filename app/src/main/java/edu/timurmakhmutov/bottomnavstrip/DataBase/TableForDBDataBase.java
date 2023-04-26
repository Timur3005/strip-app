package edu.timurmakhmutov.bottomnavstrip.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {TableForDB.class}, version = 2)
public abstract class TableForDBDataBase extends RoomDatabase {
    public abstract TableForDBDao tableForDBDao();

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static volatile TableForDBDataBase INSTANCE;

    public static TableForDBDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TableForDBDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TableForDBDataBase.class, "app_database").fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}