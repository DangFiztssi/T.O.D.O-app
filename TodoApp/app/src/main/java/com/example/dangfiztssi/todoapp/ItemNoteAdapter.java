package com.example.dangfiztssi.todoapp;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    public static class myViewHolder extends RecyclerView.ViewHolder{
        TextView tvTile;
        TextView tvDes;
        ImageView imgStar;
        TextView tvDueDate;
        ImageView imgCheckDone;
        ImageView imgEdit;
        ImageView imgDelete;

        public myViewHolder(View itemView) {
            super(itemView);

            tvTile = (TextView) itemView.findViewById(R.id.title_card);
            tvDueDate = (TextView) itemView.findViewById(R.id.description_card);
            tvDes = (TextView) itemView.findViewById(R.id.description_card);
            imgStar = (ImageView)itemView.findViewById(R.id.btnStar);
            imgCheckDone = (ImageView)itemView.findViewById(R.id.btnCheckDone);
            imgEdit = (ImageView) itemView.findViewById(R.id.btnEdit);
            imgDelete = (ImageView) itemView.findViewById(R.id.btnDelete);
        }


    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note,parent,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        Note note = lstNoteMain.get(position);

        holder.tvTile.setText(note.getTitle() + "");

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm, dd yyyy");
        holder.tvDueDate.setText(note.getDueDate());
        holder.tvDes.setText(note.getDescription());

        if(note.isPriority())
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

    private void clickEvent(final myViewHolder holder, final Note note){
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

                if(note.isDone()){
                    holder.imgCheckDone.setImageResource(R.drawable.check_done);
                }
                else
                    holder.imgCheckDone.setImageResource(R.drawable.check_done_black);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstNoteMain.size();
    }
}
