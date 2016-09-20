package com.example.dangfiztssi.todoapp;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DangF on 09/19/16.
 */
public class MainActivityPresenter {
    MainActivity activity;
    ItemNoteAdapter adapter;
    private ArrayList<Note> lstNoteMain = new ArrayList<>();

    public MainActivityPresenter(MainActivity activity) {
        this.activity = activity;
        adapter = new ItemNoteAdapter(activity, lstNoteMain);
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
                            "",
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
        lstNoteMain.add(note);
        adapter.notifyDataSetChanged();
    }

}
