package edu.timurmakhmutov.bottomnavstrip.DataBase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TableForDBRepository {
    private TableForDBDao tableForDBDao;
    private LiveData<List<TableForDB>> listLiveData;
    private LiveData<TableForDB> itemLiveData;

    public TableForDBRepository(Application application) {
        TableForDBDataBase db = TableForDBDataBase.getDatabase(application);
        tableForDBDao = db.tableForDBDao();
        listLiveData = tableForDBDao.getAllTables();
    }
    public void insert(TableForDB tableForDB){
        TableForDBDataBase.databaseWriteExecutor.execute(()-> tableForDBDao.insert(tableForDB));
    }

    public void delete(TableForDB tableForDB){
        TableForDBDataBase.databaseWriteExecutor.execute(()-> tableForDBDao.delete(tableForDB));
    }

    public LiveData<List<TableForDB>> getAllTables(){
        return listLiveData;
    }

    public LiveData<TableForDB> getById(String id){
        itemLiveData = tableForDBDao.getById(id);
        return itemLiveData;
    }
    public void updatePath(String given_id, int new_path){
        tableForDBDao.updatePath(given_id, new_path);
    }
    public void updateLiked(String given_id, int new_liked){
        tableForDBDao.updateLiked(given_id, new_liked);
    }
    public LiveData<List<TableForDB>> getAllLiked(){
        return tableForDBDao.getAllLiked();
    }
    public LiveData<List<TableForDB>> getAllPaths(){
        return tableForDBDao.getAllPaths();
    }
}
