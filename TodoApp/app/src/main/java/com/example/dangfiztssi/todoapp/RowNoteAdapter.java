package com.example.dangfiztssi.todoapp;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Paint;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by DangF on 09/23/16.
 */

public class RowNoteAdapter extends RecyclerView.Adapter<RowNoteAdapter.myViewHolder> {

    private Activity activity;
    private List<Note> lstNoteMain;

    public RowNoteAdapter(Activity activity, List<Note> lstNoteMain) {
        this.activity = activity;
        this.lstNoteMain = lstNoteMain;
    }


    public static class myViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lnHeader, lnOptional;
        TextView tvTile;
        TextView tvDes;
        ImageView imgStar;
        TextView tvDueDate;
        LinearLayout lnDone, lnEdit, lnDelete;
        ImageView imgIconDone;

        public myViewHolder(View itemView) {
            super(itemView);

            lnHeader = (LinearLayout) itemView.findViewById(R.id.lnHeaderNote);
            lnOptional = (LinearLayout) itemView.findViewById(R.id.lnOptionalNote);

            tvTile = (TextView) itemView.findViewById(R.id.tvTitleNote);
            tvDueDate = (TextView) itemView.findViewById(R.id.tvDueDateNote);
            tvDes = (TextView) itemView.findViewById(R.id.tvDescriptionNote);
            imgStar = (ImageView) itemView.findViewById(R.id.imgStarNote);
            lnEdit = (LinearLayout) itemView.findViewById(R.id.btnEditNote);
            lnDone = (LinearLayout) itemView.findViewById(R.id.btnCheckDoneNote);
            lnDelete = (LinearLayout) itemView.findViewById(R.id.btnDeleteNote);
            imgIconDone = (ImageView) itemView.findViewById(R.id.imgDone);
        }
    }

    @Override
    public RowNoteAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_new, parent, false);
        return new RowNoteAdapter.myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RowNoteAdapter.myViewHolder holder, int position) {
        Note note = lstNoteMain.get(position);

        holder.tvTile.setText(note.getTitle() + "");
        holder.tvDes.setText(note.getDescription() + "");

        if(note.isReminder()){
            holder.tvDueDate.setVisibility(View.VISIBLE);
            long str = Long.parseLong(note.getDueDate()+"");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            holder.tvDueDate.setText("Due: " + sdf.format(new Date(str*1000)));
        }
        else{
            holder.tvDueDate.setVisibility(View.GONE);
        }

        if(note.isPriority())
            holder.imgStar.setImageResource(R.drawable.star);
        else
            holder.imgStar.setImageResource(R.drawable.star_black);


        if(note.isOpen())
        {
            holder.lnOptional.setVisibility(View.VISIBLE);
//            ViewGroup.LayoutParams params = holder.tvDes.getLayoutParams();
            holder.tvDes.setMaxLines(Integer.MAX_VALUE);
        }
        else {
            holder.lnOptional.setVisibility(View.GONE);
            holder.tvDes.setMaxLines(2);
        }


        animationMenuOption(holder);

        if(note.isDone()) {
            holder.imgIconDone.setImageResource(R.drawable.check_done);
            holder.tvTile.setPaintFlags(holder.tvTile.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.imgIconDone.setImageResource(R.drawable.check_done_black);
            holder.tvTile.setPaintFlags(holder.tvTile.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        setOnClick(holder,note);
    }

    private void animationMenuOption(final myViewHolder holder){
        holder.lnOptional.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                holder.lnOptional.removeOnLayoutChangeListener(this);
                runAnim(holder.lnOptional);

            }
        });
    }

    private void runAnim(final View view){
        int centerX = (view.getLeft() + view.getRight()) / 2;
        int centerY = (view.getTop() + view.getBottom()) / 2;

        float initRadius = (float) Math.max(view.getHeight(),centerX);

        if(view.getVisibility() == View.VISIBLE) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Animator animator = ViewAnimationUtils.createCircularReveal(view, centerX, 0, 0, initRadius);

                view.setVisibility(View.VISIBLE);
                animator.start();
            }
            else
                view.setVisibility(View.VISIBLE);
        }
        else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Animator animator = ViewAnimationUtils.createCircularReveal(view, centerX, 0, initRadius, 0);

                view.setVisibility(View.GONE);
//            animator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    view.setVisibility(View.GONE);
//                }
//            });

                animator.start();
            }
            else
                view.setVisibility(View.GONE);
        }
    }

    public void setOnClick(final myViewHolder holder, final Note note){
        holder.lnHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setOpen(!note.isOpen());

                for(Note n : lstNoteMain)
                    if(n != note) n.setOpen(false);

                notifyDataSetChanged();
            }
        });

        holder.lnHeader.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((MainActivity)activity).saveImage(note);
                return false;
            }
        });

        holder.imgStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = holder.getAdapterPosition();

                lstNoteMain.get(currentPos).setPriority(!lstNoteMain.get(currentPos).isPriority());

                Note tmp = new Note();
                tmp = note;

                lstNoteMain.remove(currentPos);
                if(note.isPriority()) {
//                    holder.imgStar.setImageResource(R.drawable.star);
                    notifyItemMoved(currentPos,0);
                    lstNoteMain.add(0,tmp);
                    updateStarBtn(holder.imgStar);
                }
                else {
                    holder.imgStar.setImageResource(R.drawable.star_black);
                    notifyItemMoved(currentPos,lstNoteMain.size());
                    lstNoteMain.add(lstNoteMain.size(),tmp);
                }
                ((MainActivity)activity).resetPosId();



                ((MainActivity)activity).updateAllDB();
            }
        });

        holder.lnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setDone(!note.isDone());

                if(note.isDone()) {
                    holder.imgIconDone.setImageResource(R.drawable.check_done);
                    updateDoneBtn(holder.imgIconDone);
                    holder.tvTile.setPaintFlags(holder.tvTile.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else {
                    holder.imgIconDone.setImageResource(R.drawable.check_done_black);
                    holder.tvTile.setPaintFlags(holder.tvTile.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
                ((MainActivity)activity).updateDB(note);
            }
        });

        holder.lnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)activity).deleteDB(note);
                int i = holder.getAdapterPosition();
                lstNoteMain.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, lstNoteMain.size());
            }
        });

        holder.lnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = holder.getAdapterPosition();
                ((MainActivity)activity).addNewNote(lstNoteMain.indexOf(note));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstNoteMain.size();
    }

    public void updateStarBtn(final ImageView star){

        ((MainActivity)activity).updateStar(star);

    }

    private void updateDoneBtn(final ImageView checkDone){
//        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.rotate_scale);
//
//        animation.setInterpolator(new AccelerateInterpolator());
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                checkDone.setImageResource(R.drawable.check_done);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
//
//        checkDone.startAnimation(animation);
        ((MainActivity)activity).updateRotateAndScale(checkDone, true);
    }
}
