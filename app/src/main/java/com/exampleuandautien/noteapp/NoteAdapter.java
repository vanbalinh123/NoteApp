package com.exampleuandautien.noteapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {
    public NoteAdapter(Context context, List<Note> noteList) {
        super(context, 0, noteList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        Note note = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(note.getTitle());

        return convertView;
    }
}
