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

    @Query("SELECT * FROM TableForDB WHERE inLiked = 1")
    LiveData<List<TableForDB>> getAllLiked();

    @Query("SELECT * FROM TableForDB WHERE inPath = 1")
    LiveData<List<TableForDB>> getAllPaths();

    @Query("SELECT * FROM TableForDB")
    LiveData<List<TableForDB>> getAllTables();

    @Query("SELECT * FROM TableForDB WHERE identification=:id")
    LiveData<TableForDB> getById(String id);

    @Query("UPDATE TableForDB SET inPath = :new_path WHERE identification=:given_id")
    void updatePath(String given_id, String new_path);

    @Query("UPDATE TableForDB SET inLiked = :new_liked WHERE identification=:given_id")
    void updateLiked(String given_id, String new_liked);
}
