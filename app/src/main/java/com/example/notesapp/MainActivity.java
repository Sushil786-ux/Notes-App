package com.example.notesapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NotesAdapter.OnNoteListener {
    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private List<Note> noteList;
    private FloatingActionButton fabAddNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        noteList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewNotes);
        fabAddNote = findViewById(R.id.fabAddNote);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadNotes();
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEditDialog(null);
            }
        });

    }

    private void showAddEditDialog(final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
        builder.setView(dialogView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText editTitle = dialogView.findViewById(R.id.editTitle);
        final EditText editDescription = dialogView.findViewById(R.id.editDescription);

        boolean isUpdate = (note != null);

        if (isUpdate) {
            builder.setTitle("Edit Note");
            editTitle.setText(note.getTitle());
            editDescription.setText(note.getDescription());
        } else {
            builder.setTitle("Add New Note");
        }

        builder.setPositiveButton(isUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = editTitle.getText().toString().trim();
                String desc = editDescription.getText().toString().trim();
                String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

                if (title.isEmpty() && desc.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Note is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isUpdate) {
                    dbHelper.updateNote(note.getId(), title, desc, currentDate);
                    Toast.makeText(MainActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.addNote(title, desc, currentDate);
                    Toast.makeText(MainActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                }
                loadNotes();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }

    @Override
    public void onNoteClick(int position) {
        // Clicking a note opens it in edit mode
        Note clickedNote = noteList.get(position);
        showAddEditDialog(clickedNote);
    }

    @Override
    public void onNoteLongClick(int position) {
        // Long clicking a note triggers a delete prompt
        Note clickedNote = noteList.get(position);
        new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dbHelper.deleteNote(clickedNote.getId());
                    loadNotes();
                    Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }


    private void loadNotes() {
        noteList.clear();
        noteList.addAll(dbHelper.getAllNotes());
        notesAdapter = new NotesAdapter(this, noteList, this);
        recyclerView.setAdapter(notesAdapter);
    }
}