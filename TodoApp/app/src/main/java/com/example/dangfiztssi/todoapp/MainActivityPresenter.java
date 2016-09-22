package com.example.dangfiztssi.todoapp;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.dangfiztssi.todoapp.db.Note;
import com.example.dangfiztssi.todoapp.db.NoteContact;
import com.example.dangfiztssi.todoapp.db.NoteDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DangF on 09/19/16.
 */
public class MainActivityPresenter {
    MainActivity activity;
    ItemNoteAdapter adapter;
    private ArrayList<Note> lstNoteMain = new ArrayList<>();
    NoteDBHelper dbHelper;

    String color;

    public MainActivityPresenter(MainActivity activity) {
        this.activity = activity;
        adapter = new ItemNoteAdapter(activity, lstNoteMain);
        dbHelper = new NoteDBHelper(activity);
    }

    public ItemNoteAdapter getAdapter(){
        return adapter;
    }

    public void addNewNote(){
        final Dialog dia = new Dialog(activity);
        LayoutInflater inflater = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View view = inflater.inflate(R.layout.dialog_new_note,null);
        dia.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dia.setContentView(view);
        dia.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        final EditText edtTitle, edtDescription;
        RelativeLayout white, orange, pink, blue;
        final Switch isImportant;
        TextView tvReminder;
        TextView tvOffReminder;
        ImageView btnNew;

        edtTitle = (EditText) view.findViewById(R.id.edtTitle);
        edtDescription = (EditText) view.findViewById(R.id.edtDescription);
        white = (RelativeLayout) view.findViewById(R.id.picker_image_white);
        orange = (RelativeLayout) view.findViewById(R.id.picker_image_orange);
        pink = (RelativeLayout) view.findViewById(R.id.picker_image_pink);
        blue = (RelativeLayout) view.findViewById(R.id.picker_image_blue);
        isImportant = (Switch) view.findViewById(R.id.markIsImportant);
        tvReminder = (TextView) view.findViewById(R.id.tvReminder);
        tvOffReminder = (TextView)view.findViewById(R.id.tvSwitch);
        btnNew = (ImageView) view.findViewById(R.id.btnSave);

        color = "ffffff";

        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: set tick circle
                Log.e("white",color + "");
                color  = activity.getResources().getString(R.string.white_color);
            }
        });
        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: set tick circle
                Log.e("orange",color + "");
                color = activity.getResources().getString(R.string.orange_color);
            }
        });

        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: set tick circle
                Log.e("pink",color + "");
                color = activity.getResources().getString(R.string.pink_color);
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: set tick circle
                Log.e("blue",color + "");
                color = activity.getResources().getString(R.string.blue_color);
            }
        });

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("has click","");
                boolean important = false;
                if(isImportant.isChecked())
                    important = true;

                if(isValidForAdd(edtTitle.getText() + "",edtDescription.getText() + "")) {
                    saveNewNote(new Note(edtTitle.getText() + "",
                            edtDescription.getText() + "",
                            color,
                            "",
                            false,
                            false,
                            important ? true : false));
                    dia.dismiss();
                }
            }
        });

        dia.show();
    }

    private boolean isValidForAdd(String title, String description){
        if(title.equalsIgnoreCase("") && description.equalsIgnoreCase(""))
            return false;

        return true;
    }

    private void saveNewNote(Note note){
        //TODO; save to Database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteContact.NoteEntry.COL_NOTE_TITLE, note.getTitle());
        values.put(NoteContact.NoteEntry.COL_NOTE_DESCRIPTION, note.getDescription());
        values.put(NoteContact.NoteEntry.COL_NOTE_COLOR, note.getColor());
        values.put(NoteContact.NoteEntry.COL_NOTE_DUE_DATE, note.getDueDate());
        values.put(NoteContact.NoteEntry.COL_NOTE_PRIORITY, note.isPriority() ? 1 : 0);
        values.put(NoteContact.NoteEntry.COL_NOTE_DONE, note.isDone() ? 1 :0);

        long id_ = db.insert(NoteContact.NoteEntry.TABLE, null, values);
        note.setId(id_);

        lstNoteMain.add(note);
        adapter.notifyDataSetChanged();
    }

    public void readDB(){
        lstNoteMain.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                NoteContact.NoteEntry._ID,
                NoteContact.NoteEntry.COL_NOTE_TITLE,
                NoteContact.NoteEntry.COL_NOTE_DESCRIPTION,
                NoteContact.NoteEntry.COL_NOTE_COLOR,
                NoteContact.NoteEntry.COL_NOTE_DUE_DATE,
                NoteContact.NoteEntry.COL_NOTE_PRIORITY,
                NoteContact.NoteEntry.COL_NOTE_DONE
        };

        Cursor c = db.query(
                NoteContact.NoteEntry.TABLE,
                projection,
                null, null, null, null, null
        );

        c.moveToFirst();
        while (!c.isAfterLast()){
            Note note = new Note();

            note.setId(c.getLong(0));
            note.setTitle(c.getString(1));
            note.setDescription(c.getString(2));
            note.setColor(c.getString(3));
            note.setDueDate(c.getString(4));
            note.setPriority((c.getInt(5) == 1)? true : false);
            note.setDone((c.getInt(6) == 1)? true : false);

            lstNoteMain.add(note);

            c.moveToNext();
        }

        c.close();

        adapter.notifyDataSetChanged();
    }


    public void updateDB(Note note){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteContact.NoteEntry.COL_NOTE_TITLE, note.getTitle());
        values.put(NoteContact.NoteEntry.COL_NOTE_DESCRIPTION, note.getDescription());
        values.put(NoteContact.NoteEntry.COL_NOTE_COLOR, note.getColor());
        values.put(NoteContact.NoteEntry.COL_NOTE_DUE_DATE, note.getDueDate());
        values.put(NoteContact.NoteEntry.COL_NOTE_PRIORITY, note.isPriority() ? 1 : 0);
        values.put(NoteContact.NoteEntry.COL_NOTE_DONE, note.isDone() ? 1 :0);

        String selection = NoteContact.NoteEntry._ID + " LIKE ?";
        String[] selectionArgs = {note.getId() + ""};

        int count = db.update(
                NoteContact.NoteEntry.TABLE,
                values,
                selection,
                selectionArgs
        );

        if(count >= 0) readDB();
    }

    public void deleteDB(Note note){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = NoteContact.NoteEntry._ID + " LIKE ?";
        String[] selectionArgs = {note.getId() + ""};

        int count = db.delete(
                NoteContact.NoteEntry.TABLE,
                selection,
                selectionArgs
        );

        if(count >= 0) readDB();
    }

    public void deleteAllDB(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        int count = db.delete(
                NoteContact.NoteEntry.TABLE,
                null,
                null
        );
    }

    public void addAllDB(List<Note> lstNote){
        try {
            for (Note note : lstNote)
                saveNewNote(note);
        }
        catch (Exception e){
            Log.e("error", e + "");
        }
    }



}
