package com.exampleuandautien.noteapp;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class NoteDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "notes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";

    public NoteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_CONTENT + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public Note getNoteById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        Note note = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndexId = cursor.getColumnIndex(COLUMN_ID);
                int columnIndexTitle = cursor.getColumnIndex(COLUMN_TITLE);
                int columnIndexContent = cursor.getColumnIndex(COLUMN_CONTENT);

                if (columnIndexId >= 0 && columnIndexTitle >= 0 && columnIndexContent >= 0) {
                    note = new Note();
                    note.setId(cursor.getInt(columnIndexId));
                    note.setTitle(cursor.getString(columnIndexTitle));
                    note.setContent(cursor.getString(columnIndexContent));
                }
            }
            cursor.close();
        }
        db.close();
        return note;
    }

    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int columnIndexId = cursor.getColumnIndex(COLUMN_ID);
                int columnIndexTitle = cursor.getColumnIndex(COLUMN_TITLE);
                int columnIndexContent = cursor.getColumnIndex(COLUMN_CONTENT);

                if (columnIndexId >= 0 && columnIndexTitle >= 0 && columnIndexContent >= 0) {
                    Note note = new Note();
                    note.setId(cursor.getInt(columnIndexId));
                    note.setTitle(cursor.getString(columnIndexTitle));
                    note.setContent(cursor.getString(columnIndexContent));
                    noteList.add(note);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return noteList;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());
        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(note.getId())});
        db.close();
        return rowsAffected;
    }

    public void deleteNoteById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

}


