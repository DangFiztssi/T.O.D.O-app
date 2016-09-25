package com.example.dangfiztssi.todoapp.db;

import android.provider.BaseColumns;

/**
 * Created by DangF on 09/21/16.
 */

public class NoteContact {
    public static final String DB_NAME = "todoapp.db";
    public static final int BD_VERSION = 1;

    public class NoteEntry implements BaseColumns{
        public static final String TABLE ="notes";
        public static final String COL_NOTE_TITLE = "title";
        public static final String COL_NOTE_DESCRIPTION = "description";
        public static final String COL_NOTE_DUE_DATE = "due";
        public static final String COL_NOTE_PRIORITY = "priority";
        public static final String COL_NOTE_DONE = "done";
        public static final String COL_NOTE_COLOR = "color";
        public static final String COL_POSITION_ID = "position";
    }
}
