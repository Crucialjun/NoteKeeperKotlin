package com.example.notekeeperkotlin;

import junit.framework.TestCase;

public class DataManagerTest extends TestCase {

    public void testCreateNewNote() {
        DataManager dataManager = DataManager.getInstance();
        final CourseInfo course = dataManager.getCourse("android_async");
        final String noteTitle = "Test Note Title";
        final String noteText = "This is the body text of my test note";


        int noteIndex = dataManager.createNewNote();

        NoteInfo newNote = dataManager.getNotes().get(noteIndex);
        newNote.setCourse(course);
        newNote.setTitle(noteTitle);
        newNote.setText(noteText);


        NoteInfo compareNote = dataManager.getNotes().get(noteIndex);
        assertEquals(compareNote.getCourse(), course);
        assertEquals(compareNote.getTitle(), noteTitle);
        assertEquals(compareNote.getText(), noteText);


    }
}