package com.example.dangfiztssi.todoapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.dangfiztssi.todoapp.db.NoteContact;
import com.example.dangfiztssi.todoapp.db.NoteDBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by DangF on 09/19/16.
 */
public class MainActivityPresenter {
    MainActivity activity;
    RowNoteAdapter adapter;
    private ArrayList<Note> lstNoteMain = new ArrayList<>();
    NoteDBHelper dbHelper;

    String color;

    int currentDay,currentMonth, currentYear;
    int currentHour,currentMinute;


    public MainActivityPresenter(MainActivity activity) {
        this.activity = activity;
        adapter = new RowNoteAdapter(activity, lstNoteMain);
        dbHelper = new NoteDBHelper(activity);

        Calendar calendar = Calendar.getInstance();

        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);


        currentHour = calendar.get(Calendar.HOUR);
        currentMinute = calendar.get(Calendar.MINUTE);

    }

    public RowNoteAdapter getAdapter(){
        return adapter;
    }

    public void showDialogAddNew(final int position){
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
        final TextView tvDateReminder, tvTimeReminder;
        final ImageView btnStar, btnEnableReminder;
        ImageView btnNew;
        final ImageView btnCancel;

        edtTitle = (EditText) view.findViewById(R.id.edtTitle);
        edtDescription = (EditText) view.findViewById(R.id.edtDescription);
        btnNew = (ImageView) view.findViewById(R.id.btnSave);
        btnCancel = (ImageView) view.findViewById(R.id.btnCancel);
        btnStar= (ImageView) view.findViewById(R.id.btnStar);
        btnEnableReminder = (ImageView) view.findViewById(R.id.btnEnableReminder);
        tvDateReminder = (TextView) view.findViewById(R.id.tvDateReminder);
        tvTimeReminder = (TextView) view.findViewById(R.id.tvTimeReminder);


        final SimpleDateFormat sdfDate = new SimpleDateFormat("MMM,dd yyyy");
        final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

        if(position >= 0){
            Note noteEdit = lstNoteMain.get(position);
            edtTitle.setText(noteEdit.getTitle() + "");
            edtDescription.setText(noteEdit.getDescription() + "");

            if(noteEdit.isPriority()) {
                btnStar.setImageResource(R.drawable.star);
                btnStar.setTag(1);
            }
            else {
                btnStar.setImageResource(R.drawable.star_black);
                btnStar.setTag(0);
            }

            if(noteEdit.isReminder()){

                btnEnableReminder.setTag(1);
                btnEnableReminder.setImageResource(R.drawable.cancel_black);
                tvDateReminder.setEnabled(true);
                tvTimeReminder.setEnabled(true);
                tvDateReminder.setTextColor(activity.getResources().getColor(R.color.white_color));
                tvTimeReminder.setTextColor(activity.getResources().getColor(R.color.white_color));
                long strTime = Long.parseLong(noteEdit.getDueDate());
                Date tmp = new Date(strTime*1000);
                currentYear = tmp.getYear() + 1900;
                currentMonth =tmp.getMonth();
                currentDay = tmp.getDate();
                currentHour = tmp.getHours();
                currentMinute = tmp.getMinutes();

                tvDateReminder.setText(sdfDate.format(new Date(Long.parseLong(noteEdit.getDueDate()+"")*1000)));
                tvTimeReminder.setText(sdfTime.format(new Date(Long.parseLong(noteEdit.getDueDate()+"")*1000)));
            }
            else{
                btnStar.setTag(0);
                btnEnableReminder.setTag(0);
                btnEnableReminder.setImageResource(R.drawable.check_yes);
                tvDateReminder.setEnabled(false);
                tvTimeReminder.setEnabled(false);
                tvDateReminder.setTextColor(activity.getResources().getColor(R.color.gray_color));
                tvTimeReminder.setTextColor(activity.getResources().getColor(R.color.gray_color));
                Date date1 = new Date(currentYear -1900, currentMonth, currentDay, currentHour, currentMinute);
                tvDateReminder.setText(sdfDate.format(date1));
                tvTimeReminder.setText(sdfTime.format(date1));

            }
        }
        else{
            btnStar.setTag(0);
            btnEnableReminder.setTag(1);
            Date date1 = new Date(currentYear - 1900, currentMonth, currentDay, currentHour, currentMinute);
            tvDateReminder.setText(sdfDate.format(date1));
            tvTimeReminder.setText(sdfTime.format(date1));
        }


        btnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((Integer)btnStar.getTag() == 1){
                    btnStar.setTag(0);
                    btnStar.setImageResource(R.drawable.star_black);
                }
                else{
                    btnStar.setTag(1);
                    btnStar.setImageResource(R.drawable.star);
                }
            }
        });

        btnEnableReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvDateReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dia = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        currentDay = dayOfMonth;
                        currentMonth = month;
                        currentYear = year;

                        Date d = new Date(year - 1900, month, dayOfMonth);

                        String str = sdfDate.format(d);
                        tvDateReminder.setText(str);

                    }
                },currentYear, currentMonth, currentDay);

                dia.show();
            }
        });

        tvTimeReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dia = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        currentHour = hourOfDay;
                        currentMinute = minute;

                        String str = sdfTime.format(new Date(2016,1,1,currentHour, currentMinute));
                        tvTimeReminder.setText(str);

                    }
                },currentHour,currentMinute,false);

                dia.show();

            }
        });

        btnEnableReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((Integer) btnEnableReminder.getTag() == 1){
                    tvDateReminder.setEnabled(false);
                    tvTimeReminder.setEnabled(false);
                    tvDateReminder.setTextColor(activity.getResources().getColor(R.color.gray_color));
                    tvTimeReminder.setTextColor(activity.getResources().getColor(R.color.gray_color));
                    btnEnableReminder.setTag(0);
                    btnEnableReminder.setImageResource(R.drawable.check_yes);
                }
                else{
                    tvDateReminder.setEnabled(true);
                    tvTimeReminder.setEnabled(true);
                    tvDateReminder.setTextColor(activity.getResources().getColor(R.color.white_color));
                    tvTimeReminder.setTextColor(activity.getResources().getColor(R.color.white_color));
                    btnEnableReminder.setTag(1);
                    btnEnableReminder.setImageResource(R.drawable.cancel_black);
                }
            }
        });

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidForAdd(edtTitle.getText() + "",edtDescription.getText() + "")) {
                    boolean check = (Integer) btnEnableReminder.getTag() == 1 ? true : false;
                    if(position < 0) {
                        Note n = new Note(edtTitle.getText() + "",
                                edtDescription.getText() + "",
                                color,
                                check ? getStringTime() : "",
                                false,
                                check,
                                (Integer)btnStar.getTag()==1 ? true : false);
                        saveNewNote(n);

                    }
                    else{
                        Note tmp = lstNoteMain.get(position);
                        tmp.setTitle(edtTitle.getText()+ "");
                        tmp.setDescription(edtDescription.getText() + "");
                        tmp.setPriority((Integer)btnStar.getTag()==1 ? true : false);
                        if(check){
                            tmp.setReminder(true);
                            tmp.setDueDate(getStringTime());
                        }
                        else {
                            tmp.setReminder(false);
                            tmp.setDueDate("");
                        }

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



        dia.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        dia.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtTitle.getWindowToken(), 0);
            }
        });

        dia.show();
    }

    private String getStringTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, currentMonth);
        calendar.set(Calendar.DAY_OF_MONTH, currentDay);

        calendar.set(Calendar.HOUR, currentHour);
        calendar.set(Calendar.MINUTE, currentMinute);

        long str = calendar.getTimeInMillis()/1000;

        return str + "";
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
            if(c.getString(4).equalsIgnoreCase(""))
                note.setReminder(false);
            else
                note.setReminder(true);
            note.setPriority((c.getInt(5) == 1)? true : false);
            note.setDone((c.getInt(6) == 1)? true : false);
            note.setPosId((Integer)c.getInt(7));

            lstTmp.add(note);

            c.moveToNext();
        }
        c.close();

        int n = lstTmp.size();
        while (n != 0){
            for(int i = 0; i < lstTmp.size() ; i++)
                if(lstTmp.get(i).getPosId() == n - 1) {
                    lstNoteMain.add(0, lstTmp.get(i));
                }

            n-=1;
        }

        adapter.notifyDataSetChanged();
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
        updateDB(node);
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
        values.put(NoteContact.NoteEntry.COL_POSITION_ID, note.getPosId());

        String selection = NoteContact.NoteEntry._ID + " LIKE ? ";
        String[] selectionArgs = {note.getId() + ""};

        int count = db.update(
                NoteContact.NoteEntry.TABLE,
                values,
                "_id=" + note.getId(),
                null
        );


        if(note.isReminder()){
            if(Calendar.getInstance().getTimeInMillis() < Long.parseLong(note.getDueDate() + "")*1000) {
                setReminder(note);
            }
        }

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
        for(Note n: lstNoteMain)
            updateDB(n);
    }

    public void resetPosId(){
        for(int i = 0; i < lstNoteMain.size() ; i++)
            lstNoteMain.get(i).setPosId(i);
    }

    public void setAlarm(Note note){
        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.MONTH, 8);
//        calendar.set(Calendar.DAY_OF_MONTH, 23);
//
//        calendar.set(Calendar.HOUR_OF_DAY, 23);
//        calendar.set(Calendar.MINUTE, 34);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.AM_PM,Calendar.PM);

        long strTime = Long.parseLong(note.getDueDate());
        Date tmp = new Date(strTime*1000);
        calendar.set(Calendar.MONTH, tmp.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, tmp.getDay());

        calendar.set(Calendar.HOUR_OF_DAY, tmp.getHours());
        calendar.set(Calendar.MINUTE, tmp.getMinutes() + 1);
        calendar.set(Calendar.SECOND, tmp.getSeconds());



        Intent intent = new Intent(activity, MyReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("title",note.getTitle()  + "" );
        intent.putExtra("data", bundle);

        activity.pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTime().getTime(),activity.pendingIntent);

    }

    public void setReminder(Note note){
        long triggerMillis = Long.parseLong(note.getDueDate()+"");

        Intent intent = new Intent(activity, MyReceiver.class);
        intent.putExtra(activity.getResources().getString(R.string.title_key), note.getTitle() + "");
        intent.putExtra(activity.getResources().getString(R.string.des_key), note.getDescription() + "");
        intent.putExtra(activity.getResources().getString(R.string.id_key),note.getId() + "");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity,(int)note.getId(),intent,0);

        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerMillis *1000,pendingIntent);
    }
}
