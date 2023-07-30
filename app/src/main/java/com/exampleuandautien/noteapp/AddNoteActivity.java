package com.exampleuandautien.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;

public class AddNoteActivity extends AppCompatActivity {
    private TextInputEditText edtTitle, edtContent;
    private NoteDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnote_activity);

        databaseHelper = new NoteDatabaseHelper(this);

        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString().trim();
                String content = edtContent.getText().toString().trim();

                if (!title.isEmpty()) {
                    Note newNote = new Note(title, content);
                    long id = databaseHelper.addNote(newNote);
                    newNote.setId((int) id);

                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }
}
