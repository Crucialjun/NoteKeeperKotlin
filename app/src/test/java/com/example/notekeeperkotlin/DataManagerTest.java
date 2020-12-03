package com.example.notekeeperkotlin;

import junit.framework.TestCase;

import org.junit.Before;

public class DataManagerTest extends TestCase {

    static DataManager sDataManager;

    public void setUp() {
        sDataManager = DataManager.getInstance();
    }

    @Before
    public void setUpDataBase() {
        sDataManager.getNotes().clear();
        sDataManager.initializeExampleNotes();
    }

    public void testCreateNewNote() {
        final CourseInfo course = sDataManager.getCourse("android_async");
        final String noteTitle = "Test Note Title";
        final String noteText = "This is the body text of my test note";


        int noteIndex = sDataManager.createNewNote();

        NoteInfo newNote = sDataManager.getNotes().get(noteIndex);
        newNote.setCourse(course);
        newNote.setTitle(noteTitle);
        newNote.setText(noteText);


        NoteInfo compareNote = sDataManager.getNotes().get(noteIndex);
        assertEquals(course, compareNote.getCourse());
        assertEquals(noteTitle, compareNote.getTitle());
        assertEquals(noteText, compareNote.getText());


    }

    public void testFindSimilarNOtes() {
        final CourseInfo course = sDataManager.getCourse("android_async");
        final String noteTitle = "Test Note Title";
        final String noteText1 = "This is the body text of my test note";
        final String noteText2 = "This is the body text of my second test note";


        int noteIndex1 = sDataManager.createNewNote();
        NoteInfo newNote1 = sDataManager.getNotes().get(noteIndex1);
        newNote1.setCourse(course);
        newNote1.setTitle(noteTitle);
        newNote1.setText(noteText1);

        int noteIndex2 = sDataManager.createNewNote();
        NoteInfo newNote2 = sDataManager.getNotes().get(noteIndex2);
        newNote2.setCourse(course);
        newNote2.setTitle(noteTitle);
        newNote2.setText(noteText2);


        int foundIndex1 = sDataManager.findNote(newNote1);
        assertEquals(noteIndex1, foundIndex1);

        int foundIndex2 = sDataManager.findNote(newNote2);
        assertEquals(noteIndex2, foundIndex2);


    }

    public void testCreateNewNoteOneStepCreation() {
        final CourseInfo course = sDataManager.getCourse("android_async");
        final String noteTitle = "Test Note Title";
        final String noteText = "This is the body text of my test note";


        int noteIndex = sDataManager.createNewNote(course, noteTitle, noteText);

        NoteInfo compareNote = sDataManager.getNotes().get(noteIndex);
        assertEquals(course, compareNote.getCourse());
        assertEquals(noteTitle, compareNote.getTitle());
        assertEquals(noteText, compareNote.getText());

    }
}