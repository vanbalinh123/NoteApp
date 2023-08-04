package com.exampleuandautien.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private List<Note> noteList;
    private NoteAdapter noteAdapter;
    private ListView listView;
    private NoteDatabaseHelper databaseHelper;

    private void searchNotes(String query) {
        List<Note> filteredNotes = new ArrayList<>();
        List<Note> allNotes = databaseHelper.getAllNotes();

        for (Note note : allNotes) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredNotes.add(note);
            }
        }

        noteAdapter.clear(); // Clear the current list in the adapter
        noteAdapter.addAll(filteredNotes); // Add the filteredNotes to the adapter
        noteAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new NoteDatabaseHelper(this);
        noteList = databaseHelper.getAllNotes();

        listView = findViewById(R.id.listView);
        noteAdapter = new NoteAdapter(this, noteList);
        listView.setAdapter(noteAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note selectedNote = noteList.get(position);
                Intent intent = new Intent(MainActivity.this, ViewNoteActivity.class);
                intent.putExtra("note_id", selectedNote.getId());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Note selectedNote = noteList.get(position);
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                intent.putExtra("note_id", selectedNote.getId());
                startActivity(intent);
                return true;
            }
        });

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the search query when the user presses the search button.
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle the search query as the user types.
                searchNotes(newText);
                return false;
            }
        });


        Button btnAddNote = findViewById(R.id.btnAddNote);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the list of notes when returning from other activities
        noteList.clear();
        noteList.addAll(databaseHelper.getAllNotes());
        noteAdapter.notifyDataSetChanged();
    }
}