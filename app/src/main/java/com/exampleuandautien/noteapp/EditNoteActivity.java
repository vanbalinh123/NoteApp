package com.exampleuandautien.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;

public class EditNoteActivity extends AppCompatActivity {
    private TextInputEditText edtEditTitle, edtEditContent;
    private NoteDatabaseHelper databaseHelper;
    private Note note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        databaseHelper = new NoteDatabaseHelper(this);

        edtEditTitle = findViewById(R.id.edtEditTitle);
        edtEditContent = findViewById(R.id.edtEditContent);

        Button btnSaveEdit = findViewById(R.id.btnSaveEdit);
        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtEditTitle.getText().toString().trim();
                String content = edtEditContent.getText().toString().trim();

                if (!title.isEmpty()) {
                    note.setTitle(title);
                    note.setContent(content);
                    databaseHelper.updateNote(note);

                    setResult(RESULT_OK);
                    Intent intent = new Intent(EditNoteActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });



        // Get the note ID from the intent
        int noteId = getIntent().getIntExtra("note_id", -1);
        if (noteId != -1) {
            // Retrieve the note from the database using the ID
            note = databaseHelper.getNoteById(noteId);
            if (note != null) {
                // Display the note's current title and content in the EditText fields
                edtEditTitle.setText(note.getTitle());
                edtEditContent.setText(note.getContent());
            }
        }
    }
}
