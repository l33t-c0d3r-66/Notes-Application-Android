package com.example.notes_application_android.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.notes_application_android.Database.NoteAppDatabase;
import com.example.notes_application_android.Model.Note;
import com.example.notes_application_android.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class NewNote extends AppCompatActivity {

    private ImageButton saveBtn;
    private EditText titleText, contentText;
    private Note note;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        saveBtn = findViewById(R.id.saveBtn);

        titleText = findViewById(R.id.titleText);
        contentText = findViewById(R.id.contentText);

        blindSaveButton();

        Intent intent=getIntent();
        action=intent.getStringExtra("action");
        if(!action.equalsIgnoreCase("create"))
        {
            addControls();
        }
        addEvents();
    }

    private void addControls() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        class GetNote extends AsyncTask<Void, Void, Void>
        {

            @Override
            protected Void doInBackground(Void... voids) {
                note = MainActivity.noteAppDatabase.noteDao().getNote(id);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                titleText.setText(note.getNoteTitle());
                contentText.setText(note.getNoteContent());
            }
        }
        GetNote getNote = new GetNote();
        getNote.execute();
    }

    private void addEvents() {

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(action.equalsIgnoreCase("create"))
               {
                   processSaveNote(v);
               }
               else
               {
                   processUpdateNote(v);
               }
            }
        });

        titleText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showSaveButton();
                } else {
                    blindSaveButton();
                }
            }
        });

        contentText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showSaveButton();
                } else {
                    blindSaveButton();
                }
            }
        });
    }

    private void blindSaveButton() {
        saveBtn.setVisibility(View.INVISIBLE);
    }

    private void showSaveButton() {
        saveBtn.setVisibility(View.VISIBLE);
    }

    private void processUpdateNote(View v) {
        blindSaveButton();
        hideKeyboard(v);

        if (contentText.getText().toString().length() <= 0) {
            return;
        }

        String title = titleText.getText().toString();
        String content = contentText.getText().toString();
        note.setNoteTitle(title);
        note.setNoteContent(content);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        note.setModificationDate(formatter.format(date));

        class UpdateNote extends AsyncTask<Void, Void, Void>
        {

            @Override
            protected Void doInBackground(Void... voids) {
                NoteAppDatabase.getAppDatabase(getApplicationContext()).noteDao().update(note);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        UpdateNote updateNote = new UpdateNote();
        updateNote.execute();
    }


    private void processSaveNote(View v) {
        blindSaveButton();
        hideKeyboard(v);

        if (contentText.getText().toString().length() <= 0) {
            return;
        }

        String title = titleText.getText().toString();
        String content = contentText.getText().toString();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        Note note = new Note(0,title, content, formatter.format(date),"");
        class SaveNote extends AsyncTask<Void, Void, Void>
        {

            @Override
            protected Void doInBackground(Void... voids) {
                NoteAppDatabase.getAppDatabase(getApplicationContext()).noteDao().add(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        SaveNote saveNote = new SaveNote();
        saveNote.execute();
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}