package com.example.notes_application_android.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.notes_application_android.Model.Note;
import java.util.List;
@Dao
public interface NoteDAO {
    @Update
    void update(Note note);
    @Query("delete from note where noteId=:id")
    void deleteNote(int id);
    @Delete
    void delete(Note note);
    @Insert
    void add(Note note);
    @Query("Select * from Note")
    List<Note> getAllNotes();
    @Query("Select * from Note where noteId=:id")
    Note getNote(int id);
}
