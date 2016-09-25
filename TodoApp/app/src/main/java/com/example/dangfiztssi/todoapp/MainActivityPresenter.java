package com.example.dangfiztssi.todoapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.dangfiztssi.todoapp.db.NoteContact;
import com.example.dangfiztssi.todoapp.db.NoteDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DangF on 09/19/16.
 */
public class MainActivityPresenter {
    MainActivity activity;
//    ItemNoteAdapter adapter;
    RowNoteAdapter adapter;
    private ArrayList<Note> lstNoteMain = new ArrayList<>();
    NoteDBHelper dbHelper;

    String color;

    public MainActivityPresenter(MainActivity activity) {
        this.activity = activity;
//        adapter = new ItemNoteAdapter(activity, lstNoteMain);
        adapter = new RowNoteAdapter(activity, lstNoteMain);
        dbHelper = new NoteDBHelper(activity);

    }

    public RowNoteAdapter getAdapter(){
        return adapter;
    }

    public void addNewNote(final int position){
        /**
         *position to determine Update or New
         * position = -1 :  New note
         * position >=0 : Update
         */
        final Dialog dia = new Dialog(activity);
        LayoutInflater inflater = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        final View view = inflater.inflate(R.layout.dialog_new_note,null);
        dia.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dia.setContentView(view);
        dia.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dia.setCanceledOnTouchOutside(false);

        final EditText edtTitle, edtDescription;
        final Switch isImportant;
        TextView tvReminder;
        TextView tvOffReminder;
        ImageView btnNew;
        ImageView btnCancel;

        edtTitle = (EditText) view.findViewById(R.id.edtTitle);
        edtDescription = (EditText) view.findViewById(R.id.edtDescription);
        isImportant = (Switch) view.findViewById(R.id.markIsImportant);
        tvReminder = (TextView) view.findViewById(R.id.tvReminder);
        tvOffReminder = (TextView)view.findViewById(R.id.tvSwitch);
        btnNew = (ImageView) view.findViewById(R.id.btnSave);
        btnCancel = (ImageView) view.findViewById(R.id.btnCancel);



        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean important = false;
                if(isImportant.isChecked())
                    important = true;


                if(isValidForAdd(edtTitle.getText() + "",edtDescription.getText() + "")) {
                    if(position < 0) {
                        saveNewNote(new Note(edtTitle.getText() + "",
                                edtDescription.getText() + "",
                                color,
                                "",
                                false,
                                false,
                                important ? true : false));
                    }
                    else{
                        Note tmp = lstNoteMain.get(position);
                        tmp.setTitle(edtTitle.getText()+ "");
                        tmp.setDescription(edtDescription.getText() + "");
                        //TODO: save Reminder and Star

                        adapter.notifyItemChanged(position);
                        activity.updateDB(tmp);
                    }
                    setAnimHide(dia.getCurrentFocus().getRootView());
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnimHide(dia.getCurrentFocus().getRootView());
            }
        });


        dia.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                setAnimShow(dia.getCurrentFocus().getRootView());
            }
        });

        if(position >= 0){
            Note noteEdit = lstNoteMain.get(position);
            edtTitle.setText(noteEdit.getTitle() + "");
            edtDescription.setText(noteEdit.getDescription() + "");
            //TODO set data for reminder and star
        }

        dia.show();
    }

    private void setAnimShow(View view){
        int centerX = (view.getLeft() + view.getRight()) / 2;
        int centerY = (view.getTop() + view.getBottom()) / 2;

        float finalRadius = (float) Math.max(centerX, centerY);

        Animator animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        view.setVisibility(View.VISIBLE);
        animator.start();
    }

    private void setAnimHide(final View view){
        int centerX = (view.getLeft() + view.getRight()) / 2;
        int centerY = (view.getTop() + view.getBottom()) / 2;

        float initRadius = view.getWidth();

        Animator animator = ViewAnimationUtils.createCircularReveal(view, centerX, view.getHeight(), initRadius,0);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
            }
        });

        animator.start();
    }

    private boolean isValidForAdd(String title, String description){
        if(title.equalsIgnoreCase("") && description.equalsIgnoreCase(""))
            return false;

        return true;
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
                NoteContact.NoteEntry.COL_NOTE_DONE,
                NoteContact.NoteEntry.COL_POSITION_ID
        };

        Cursor c = db.query(
                NoteContact.NoteEntry.TABLE,
                projection,
                null, null, null, null, null
        );

        List<Note> lstTmp = new ArrayList<>();
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
            note.setPosId((Integer)c.getInt(7));

            Log.e("pos id", note.getPosId() + "");

            lstTmp.add(note);

            c.moveToNext();
        }
        c.close();


        for(int i = 0; i < lstTmp.size() ; i++){
            for(int j = 0; j < lstTmp.size(); j++){
                if(lstTmp.get(j).getPosId() ==  i) {
                    lstNoteMain.add(lstTmp.get(j));
                    break;
                }
            }
        }



//        lstNoteMain.addAll(lstTmp);

//                for(Note note : lstNoteMain){
//            Log.e("pos - name", note.getPosId() + " - " + note.getTitle());
//        }
        adapter.notifyDataSetChanged();
//        for(Note n : lstTmp){
//            lstNoteMain.add(lstTmp.size(),n);
//            adapter.notifyItemInserted(lstTmp.size());
//        }
    }

    private void saveNewNote(Note note){
        //TODO; save to Database

        if(note.isPriority()) { //Add first
            lstNoteMain.add(0,note);
            adapter.notifyItemInserted(0);

        }
        else { //Add end
            lstNoteMain.add(lstNoteMain.size(),note);
            adapter.notifyItemInserted(lstNoteMain.size());
        }

        resetPosId();
        saveNewNoteDB(note);
        updateAllDB();
    }

    public void saveNewNoteDB(Note node){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteContact.NoteEntry.COL_NOTE_TITLE, node.getTitle());
        values.put(NoteContact.NoteEntry.COL_NOTE_DESCRIPTION, node.getDescription());
        values.put(NoteContact.NoteEntry.COL_NOTE_COLOR, node.getColor());
        values.put(NoteContact.NoteEntry.COL_NOTE_DUE_DATE, node.getDueDate());
        values.put(NoteContact.NoteEntry.COL_NOTE_PRIORITY, node.isPriority() ? 1 : 0);
        values.put(NoteContact.NoteEntry.COL_NOTE_DONE, node.isDone() ? 1 :0);
        values.put(NoteContact.NoteEntry.COL_POSITION_ID, node.getPosId());

        long id_ = db.insert(NoteContact.NoteEntry.TABLE, null, values);
        node.setId(id_);
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

//        if(count >= 0) readDB();
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
    }

    public void deleteAllDB(){
        for(Note n : lstNoteMain)
            deleteDB(n);
    }

    private void addNotesIntoDB(){
        for(Note n : lstNoteMain)
            saveNewNoteDB(n);
    }

    public void updateAllDB(){
//        deleteAllDB();
//        addNotesIntoDB();
        for(Note n: lstNoteMain)
            updateDB(n);
    }

    public void resetPosId(){
        for(int i = 0; i < lstNoteMain.size() ; i++)
            lstNoteMain.get(i).setPosId(i);

//        for(Note note : lstNoteMain){
//            Log.e("pos - name", note.getPosId() + " - " + note.getTitle());
//        }
    }
}
