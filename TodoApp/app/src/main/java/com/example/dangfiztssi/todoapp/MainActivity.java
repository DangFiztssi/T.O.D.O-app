package com.example.dangfiztssi.todoapp;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private MainActivityPresenter presenter;
    private RecyclerView rvTodoMain;
    public RecyclerView.LayoutManager layoutManager;
    private LinearLayout lnMain;

    public PendingIntent pendingIntent;

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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            saveAsImage(lnMain);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveAsImage(Note note){
        Log.e("save as image","...");
        String fileName = "test2.jpeg";
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_new_note, null, false );

        EditText edttitle, edtDes;
        edttitle = (EditText) v.findViewById(R.id.edtTitle);
        edtDes = (EditText) v.findViewById(R.id.edtDescription);

        edtDes.setText(note.getTitle());
        edtDes.setText(note.getDescription());

        File sdCard = Environment.getExternalStorageDirectory();
        v.setDrawingCacheEnabled(true);
//        Bitmap bitmap = v.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(255, 255, Bitmap.Config.ARGB_8888);
        v.draw(new Canvas(bitmap));
        Log.e("run","..");
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
        presenter.showDialogAddNew(position);
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
