package com.example.garbagecleanup.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.garbagecleanup.model.Draft;

/**
 * User: Aman
 * Date: 22-09-2019
 * Time: 09:46 PM
 */
@Database(entities = {Draft.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DraftDAO draftDAO();
}
