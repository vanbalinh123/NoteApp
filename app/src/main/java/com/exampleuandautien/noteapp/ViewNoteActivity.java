package com.exampleuandautien.noteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewNoteActivity extends AppCompatActivity {
    private TextView tvNoteTitle, tvNoteContent;
    private NoteDatabaseHelper databaseHelper;
    private Note note;

    private void openEditNoteActivity() {
        Intent intent = new Intent(ViewNoteActivity.this, EditNoteActivity.class);
        intent.putExtra("note_id", note.getId());
        startActivity(intent);
    }

    private void deleteNote() {
        if (note != null) {
            databaseHelper.deleteNoteById(note.getId());
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        databaseHelper = new NoteDatabaseHelper(this);

        tvNoteTitle = findViewById(R.id.tvNoteTitle);
        tvNoteContent = findViewById(R.id.tvNoteContent);

        Button btnEditNote = findViewById(R.id.btnEditNote);
        btnEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditNoteActivity();
            }
        });

        Button btnDeleteNote = findViewById(R.id.btnDeleteNote);
        btnDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            int noteId = intent.getIntExtra("note_id", -1);
            if (noteId != -1) {
                // Retrieve the note from the database using the ID
                note = databaseHelper.getNoteById(noteId);
                if (note != null) {
                    // Display the note's current title and content in the TextViews
                    tvNoteTitle.setText(note.getTitle());
                    tvNoteContent.setText(note.getContent());
                }
            }
        };

        Button btnShareNote = findViewById(R.id.btnShareNote);
        btnShareNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareNote();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNote();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void shareNote() {
        String title = tvNoteTitle.getText().toString();
        String content = tvNoteContent.getText().toString();
        String shareText = "Title: " + title + "\nContent: " + content;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Shared Note");
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(intent, "Share Note via"));
    }


}
