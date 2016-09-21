package com.example.dangfiztssi.todoapp;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dangfiztssi.todoapp.db.Note;

import java.util.List;

/**
 * Created by DangF on 09/20/16.
 */

public class ItemNoteAdapter extends RecyclerView.Adapter<ItemNoteAdapter.myViewHolder> {

    private Activity activity;
    private List<Note> lstNoteMain;

    public ItemNoteAdapter(Activity activity, List<Note> lstNoteMain) {
        this.activity = activity;
        this.lstNoteMain = lstNoteMain;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView tvTile;
        TextView tvDes;
        ImageView imgStar;
        TextView tvDueDate;
        ImageView imgCheckDone;
        ImageView imgEdit;
        ImageView imgDelete;
        CardView mainCard;

        public myViewHolder(View itemView) {
            super(itemView);

            tvTile = (TextView) itemView.findViewById(R.id.title_card);
            tvDueDate = (TextView) itemView.findViewById(R.id.description_card);
            tvDes = (TextView) itemView.findViewById(R.id.description_card);
            imgStar = (ImageView) itemView.findViewById(R.id.btnStar);
            imgCheckDone = (ImageView) itemView.findViewById(R.id.btnCheckDone);
            imgEdit = (ImageView) itemView.findViewById(R.id.btnEdit);
            imgDelete = (ImageView) itemView.findViewById(R.id.btnDelete);
            mainCard = (CardView) itemView.findViewById(R.id.main_card);
        }


    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        Note note = lstNoteMain.get(position);

        holder.tvTile.setText(note.getTitle() + "");

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm, dd yyyy");
        holder.tvDueDate.setText(note.getDueDate());
        holder.tvDes.setText(note.getDescription());

        //TODO: set background
//        int colorBg = 0;
//        switch (note.getColorType()) {
//            case 0:
//                colorBg = R.color.white_color;
//                break;
//            case 1:
//                colorBg = R.color.orange_color;
//                break;
//            case 2:
//                colorBg = R.color.pink_color;
//                break;
//            default:
//                colorBg = R.color.blue_color;
//        }
        Log.e("color card", note.getColor() + "");
        holder.mainCard.setCardBackgroundColor(Color.parseColor("#" + note.getColor()));

        //TODO: set priority
        if (note.isPriority())
            holder.imgStar.setImageResource(R.drawable.star);
        else
            holder.imgStar.setImageResource(R.drawable.star_black);

        holder.imgStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        clickEvent(holder, note);

    }

    private void clickEvent(final myViewHolder holder, final Note note) {
        holder.imgStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setPriority(!note.isPriority());
                notifyDataSetChanged();
            }
        });

        holder.imgCheckDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setDone(!note.isDone());

                Note tmp = new Note();
                tmp = note;
                if (note.isDone()) {
                    holder.imgCheckDone.setImageResource(R.drawable.check_done);
                    tmp.setDone(true);
                } else {
                    holder.imgCheckDone.setImageResource(R.drawable.check_done_black);
                    tmp.setDone(false);
                }
                ((MainActivity) activity).updateDB(tmp);
            }
        });

        holder.imgStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setPriority(!note.isPriority());
                Note tmp = new Note();
                tmp = note;
                if (note.isPriority()) {
                    holder.imgStar.setImageResource(R.drawable.star);
                    tmp.setPriority(true);
                } else {
                    holder.imgStar.setImageResource(R.drawable.star_black);
                    tmp.setPriority(false);
                }
                ((MainActivity) activity).updateDB(tmp);
            }
        });

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) activity).deleteDB(note);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstNoteMain.size();
    }
}
