package com.example.dangfiztssi.todoapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by DangF on 09/21/16.
 */

public class NoteDBHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String LONG_TYPE = " LONG";
    public NoteDBHelper(Context context){
        super(context,NoteContact.DB_NAME,null, NoteContact.BD_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDB = "CREATE TABLE " + NoteContact.NoteEntry.TABLE +
                "(" + NoteContact.NoteEntry._ID + " INTEGER PRIMARY KEY," +
                NoteContact.NoteEntry.COL_NOTE_TITLE + TEXT_TYPE + "," + //TITLE
                NoteContact.NoteEntry.COL_NOTE_DESCRIPTION + TEXT_TYPE + "," + //Description
                NoteContact.NoteEntry.COL_NOTE_COLOR + TEXT_TYPE + "," + //Color
                NoteContact.NoteEntry.COL_NOTE_DUE_DATE + TEXT_TYPE + "," + //Due date
                NoteContact.NoteEntry.COL_NOTE_PRIORITY + INT_TYPE + "," + //priority
                NoteContact.NoteEntry.COL_NOTE_DONE + INT_TYPE + ");";

        Log.e("sql query",createDB + "");
        db.execSQL(createDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }
}
