package com.example.garbagecleanup.database;

import com.example.grabagecleanup.model.Draft;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * User: Aman
 * Date: 22-09-2019
 * Time: 09:46 PM
 */
@Database(entities = {Draft.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DraftDAO draftDAO();
}
