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
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;

import static com.example.dangfiztssi.todoapp.R.id.edtTitle;

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
                addNewNote(-1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void saveAsImage(Note note){
        Log.e("save as image","...");
        String fileName = "test2.jpeg";
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_new_note, null, false );

        EditText edttitle, edtDes;
        edttitle = (EditText) v.findViewById(edtTitle);
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

    public void saveImage(Note note){
        View ln = findViewById(R.id.lnSave);

        EditText edtTile, edtDes;
        edtTile = (EditText)  ln.findViewById(edtTitle);
        edtDes = (EditText) ln.findViewById(R.id.edtDescription);

        edtTile.setText(note.getTitle());
        edtDes.setText(note.getDescription());

        String fileName = "testNew.jpeg";

        File sdCard = Environment.getExternalStorageDirectory();
        ln.setDrawingCacheEnabled(true);
//        Bitmap bitmap = v.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(255, 255, Bitmap.Config.ARGB_8888);
        ln.draw(new Canvas(bitmap));
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

    public void updateStar(final ImageView star){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);

//        AnimatorSet animatorSet = new AnimatorSet();
//
//        ObjectAnimator rotation = ObjectAnimator.ofFloat(star,"rotation", 0f, 360f);
//        rotation.setDuration(300);
//        rotation.setInterpolator(new AccelerateInterpolator());
//
//        ObjectAnimator bounceX = ObjectAnimator.ofFloat(star, "scaleX", 0.2f, 1f);
//        bounceX.setDuration(300);
//        bounceX.setInterpolator(new OvershootInterpolator());
//
//        ObjectAnimator bounceY = ObjectAnimator.ofFloat(star, "scaleY", 0.2f, 1f);
//        bounceY.setDuration(300);
//        bounceY.setInterpolator(new OvershootInterpolator());
//        bounceY.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                star.setImageResource(R.drawable.star);
//            }
//        });
//
//        animatorSet.play(rotation);
//        animatorSet.play(bounceX).with(bounceX).after(rotation);
//
//        animatorSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                star.setImageResource(R.drawable.star_black);
//            }
//        });
//
//        animatorSet.start();
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                star.setImageResource(R.drawable.star);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        star.startAnimation(animation);
    }

    public void updateRotateAndScale(final ImageView v, final boolean isCheckDone){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_scale);

        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(isCheckDone)
                    v.setImageResource(R.drawable.check_done);
                else{//check due date
                    if(((Integer)v.getTag()) == 0){
                        v.setImageResource(R.drawable.check_yes);
                    }
                    else{
                        v.setImageResource(R.drawable.cancel_black);
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        v.startAnimation(animation);
    }


}
