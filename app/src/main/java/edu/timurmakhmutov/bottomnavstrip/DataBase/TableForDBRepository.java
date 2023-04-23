package edu.timurmakhmutov.bottomnavstrip.DataBase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TableForDBRepository {
    private TableForDBDao tableForDBDao;
    private LiveData<List<TableForDB>> listLiveData;

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
}
