package edu.timurmakhmutov.bottomnavstrip.DataBase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TableForDB")
public class TableForDB {
    public TableForDB() {
    }

    public TableForDB(@NonNull String identification,
                      String title, String address, String description, String location,
                      String imageURLs, String lat, String lon, int inLiked, int inPath)
    {
        this.identification = identification;
        this.title = title;
        this.address = address;
        this.description = description;
        this.location = location;
        this.imageURLs = imageURLs;
        this.lat = lat;
        this.lon = lon;
        this.inLiked = inLiked;
        this.inPath = inPath;
    }

    @NonNull
    @PrimaryKey
    public String identification;
    public String title;
    public String address;
    public String description;
    public String location;
    public String imageURLs;
    public String lat;
    public String lon;
    public int inLiked;
    public int inPath;
}
