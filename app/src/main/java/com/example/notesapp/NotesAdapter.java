package com.example.notesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private Context context;
    private List<Note> noteList;
    private OnNoteListener onNoteListener;

    public interface OnNoteListener {
        void onNoteClick(int position);
        void onNoteLongClick(int position);
    }

    public NotesAdapter(Context context, List<Note> noteList, OnNoteListener onNoteListener) {
        this.context = context;
        this.noteList = noteList;
        this.onNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.descTextView.setText(note.getDescription());
        holder.dateTextView.setText(note.getDate());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView titleTextView, descTextView, dateTextView;
        OnNoteListener onNoteListener;

        public NoteViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textNoteTitle);
            descTextView = itemView.findViewById(R.id.textNoteDesc);
            dateTextView = itemView.findViewById(R.id.textNoteDate);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            onNoteListener.onNoteLongClick(getAdapterPosition());
            return true;
        }
    }
}
