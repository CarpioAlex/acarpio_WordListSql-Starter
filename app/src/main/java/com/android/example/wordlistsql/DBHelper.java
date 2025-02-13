package com.android.example.wordlistsql;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.w3c.dom.Comment;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "WORDS";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_WORD = "WORD";
    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;
    public DBHelper(Context c) {


        super(c, "words.db", null, 6);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_WORD + " TEXT NOT NULL)");
        fillDatabaseWithData(db);

    }

    private void fillDatabaseWithData(SQLiteDatabase db) {
        String[] words = {"Android", "Adapter", "ListView", "AsyncTask",
                "Android Studio", "SQLiteDatabase", "SQLOpenHelper",
                "Data model", "ViewHolder","AndroidPerformance",
                "OnClickListener"};
        ContentValues values = new ContentValues();
        for (int i=0; i < words.length; i++) {
            values.put(COLUMN_WORD, words[i]);
            db.insert(TABLE_NAME, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @SuppressLint("Range")
    public WordItem query(int position) {
        String query = "SELECT * FROM " + TABLE_NAME +
                " ORDER BY " + COLUMN_WORD + " ASC " +
                "LIMIT " + position + ",1";

        WordItem entry = new WordItem();
        Cursor cursor = null;
        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                entry.setmId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                entry.setmWord(cursor.getString(cursor.getColumnIndex(COLUMN_WORD)));
            }
        } catch (Exception e) {
            Log.d("Debug", "EXCEPTION! " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return entry;
    }

    public long insert(String word){
        long newId = 0;
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD, word);
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            newId = mWritableDB.insert(TABLE_NAME, null, values);
            return newId;

        } catch (Exception e) {}
        return newId;
    }

    public long count(){
        if (mReadableDB == null) {
            mReadableDB = getReadableDatabase();
        }
        return DatabaseUtils.queryNumEntries(mReadableDB, TABLE_NAME);
    }

    public int delete(int id) {
        int deleted = 0;
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            deleted = mWritableDB.delete(TABLE_NAME, COLUMN_ID + " = " + id, null);
            return deleted;
        } catch (Exception e) {}
        return deleted;
    }

    public int update(int id, String word) {
        int mNumberOfRowsUpdated =-1;
        if (mWritableDB == null) {
            mWritableDB = getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD, word);

        mNumberOfRowsUpdated = mWritableDB.update(TABLE_NAME,
                values, // new values to insert
                // selection criteria for row (the _id column)
                COLUMN_ID + " = ?",
                //selection args; value of id
                new String[]{String.valueOf(id)});
        return mNumberOfRowsUpdated;

    }


    public Cursor search(String searchString) {
        String[] columns = new String[]{COLUMN_WORD};
        String where = COLUMN_WORD + " LIKE ?";
        searchString = "%" + searchString + "%";
        String[] whereArgs = new String[]{searchString};

        Cursor cursor = null;
        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.query(TABLE_NAME, columns, where, whereArgs, null, null, null);
        } catch (Exception e) {

        }
        return cursor;
    }
}
