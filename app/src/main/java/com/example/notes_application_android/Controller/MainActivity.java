package com.example.notes_application_android.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.notes_application_android.Database.NoteAppDatabase;
import com.example.notes_application_android.Model.Note;
import com.example.notes_application_android.R;
import com.example.notes_application_android.View.NoteAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageButton addBtn;
    private RecyclerView notes;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    public static NoteAppDatabase noteAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.addNotesBtn);
        notes= findViewById(R.id.rcvNotes);

        checkPermission();
        addControls();
        addEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        class GetAllNotes extends AsyncTask<Void, Void, Void>
        {

            @Override
            protected Void doInBackground(Void... voids) {
                noteList = noteAppDatabase.noteDao().getAllNotes();
                return null;
            }

            protected void onPostExecute(Void aVoid) {
                noteAdapter = new NoteAdapter(MainActivity.this, noteList);
                notes.setAdapter(noteAdapter);
            }
        }
        GetAllNotes getAllNotes = new GetAllNotes();
        getAllNotes.execute();
    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        }
    }

    private void addControls() {
        noteAppDatabase = NoteAppDatabase.getAppDatabase(MainActivity.this);
        notes.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));

        class GetAllNotes extends AsyncTask<Void, Void, Void>
        {

            @Override
            protected Void doInBackground(Void... voids) {
                noteList=noteAppDatabase.noteDao().getAllNotes();
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                noteAdapter = new NoteAdapter(MainActivity.this, noteList);
                notes.setAdapter(noteAdapter);
            }
        }

        GetAllNotes getAllNotes = new GetAllNotes();
        getAllNotes.execute();

    }

    private void addEvent()
    {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewNote.class);
                intent.putExtra("action","create");
                startActivity(intent);
            }
        });

    }


}