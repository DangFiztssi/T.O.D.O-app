package com.example.dangfiztssi.todoapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private MainActivityPresenter presenter;
    private RecyclerView rvTodoMain;
    public RecyclerView.LayoutManager layoutManager;
    private LinearLayout lnMain;

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lnMain = (LinearLayout) findViewById(R.id.lnMain);

        presenter = new MainActivityPresenter(this);

        rvTodoMain = (RecyclerView) findViewById(R.id.rvMain);
         layoutManager = new LinearLayoutManager(this);
        rvTodoMain.setLayoutManager(layoutManager);
        rvTodoMain.setAdapter(presenter.getAdapter());
        rvTodoMain.setNestedScrollingEnabled(false);

        readDB();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                addNewNote(-1);
            }
        });


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DAY_OF_MONTH, 23);

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 34);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM,Calendar.PM);

        long test = calendar.getTimeInMillis()/1000;
        Log.e("epoch time", test + "");

        Intent intent = new Intent(MainActivity.this, MyReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            saveAsImage(lnMain);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveAsImage(LinearLayout ln){
        Log.e("save as image","...");
        String fileName = "test.jpeg";
        File sdCard = Environment.getExternalStorageDirectory();
        ln.setDrawingCacheEnabled(true);
        Bitmap bitmap = ln.getDrawingCache();
        Log.e("run","....");
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, new FileOutputStream(new File(sdCard, fileName)));
            Log.e("success","");
        }
        catch (Exception e){
            Log.e("error", e + "");
            e.printStackTrace();
        }
        Log.e("done",".");

    }

    public void addNewNote(int position){
        presenter.addNewNote(position);
    }

    public void readDB(){presenter.readDB();}

    public void updateDB(Note note){
        presenter.updateDB(note);
    }

    public void deleteDB(Note note){
        presenter.deleteDB(note);
    }

    public void deleteAllDB(){
        presenter.deleteAllDB();
    }

    public void updateAllDB(){
        presenter.updateAllDB();
    }

    public void resetPosId(){
        presenter.resetPosId();
    }

}
