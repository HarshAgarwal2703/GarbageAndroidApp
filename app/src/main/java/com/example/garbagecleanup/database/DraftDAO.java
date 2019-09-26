package com.example.garbagecleanup.database;

import com.example.garbagecleanup.model.Draft;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * User: Aman
 * Date: 22-09-2019
 * Time: 09:42 PM
 */
@Dao
public interface DraftDAO {

    @Query("SELECT * FROM DraftTable")
    List<Draft> getAll();

    @Query("SELECT * FROM DraftTable WHERE user_id = :userId")
    List<Draft> getAllCurrentUserDrafts(int userId);

    @Insert
    void insert(Draft draft);

    @Query("UPDATE DraftTable SET description = :description, title =:title WHERE timestamp=:timestamp")
    void update(String description, String title, String timestamp);

    @Delete
    void delete(Draft draft);
}
