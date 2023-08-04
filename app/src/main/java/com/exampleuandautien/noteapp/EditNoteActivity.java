package com.exampleuandautien.noteapp;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;

public class EditNoteActivity extends AppCompatActivity {
    private TextInputEditText edtEditTitle, edtEditContent;
    private NoteDatabaseHelper databaseHelper;
    private Note note;
    private static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 1;

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

                // Make phone numbers in content clickable and linkified
                Spannable spannable = (Spannable) edtEditContent.getText();
                URLSpan[] urlSpans = spannable.getSpans(0, spannable.length(), URLSpan.class);
                for (URLSpan urlSpan : urlSpans) {
                    int start = spannable.getSpanStart(urlSpan);
                    int end = spannable.getSpanEnd(urlSpan);
                    spannable.removeSpan(urlSpan);
                    spannable.setSpan(new PhoneClickableSpan(urlSpan.getURL()), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                edtEditContent.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    private class PhoneClickableSpan extends ClickableSpan {
        private String phoneNumber;

        PhoneClickableSpan(String url) {
            super();
            this.phoneNumber = url;
        }

        @Override
        public void onClick(@NonNull View widget) {
            openPhoneDialer(phoneNumber);
        }
    }

    private void openPhoneDialer(String phoneNumber) {
        // Loại bỏ các ký tự không cần thiết trong số điện thoại
        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);

    }

    private void makePhoneCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted yet
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, make the phone call
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PHONE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, make the phone call
                Spannable spannable = (Spannable) edtEditContent.getText();
                ClickableSpan[] spans = spannable.getSpans(0, spannable.length(), ClickableSpan.class);
                for (ClickableSpan span : spans) {
                    int start = spannable.getSpanStart(span);
                    int end = spannable.getSpanEnd(span);
                    if (start <= edtEditContent.getSelectionStart() && edtEditContent.getSelectionStart() <= end) {
                        String phoneNumber = spannable.subSequence(start, end).toString();
                        makePhoneCall(phoneNumber);
                        break;
                    }
                }
            } else {
                // User denied the permission, you can show a message or handle it as per your requirement
                Toast.makeText(this, "Call Phone permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
