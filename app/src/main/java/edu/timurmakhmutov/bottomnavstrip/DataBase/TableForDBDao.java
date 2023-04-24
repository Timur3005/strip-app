package edu.timurmakhmutov.bottomnavstrip.DataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface TableForDBDao {
    @Insert
    void insert(TableForDB tableForDB);

    @Delete
    void delete(TableForDB tableForDB);

    @Query("SELECT * FROM TableForDB")
    LiveData<List<TableForDB>> getAllTables();

    @Query("SELECT * FROM TableForDB WHERE identification=:id")
    LiveData<TableForDB> getById(String id);
}
