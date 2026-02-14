package edu.course.gradebook;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class GradebookTest {

    @Test
    public void testAddStudent() {
        Gradebook gb = new Gradebook();
        assertTrue(gb.addStudent("Goldie"));
        assertFalse(gb.addStudent("Goldie"));
        assertTrue(gb.findStudentGrades("Goldie").isPresent());
    }

    @Test
    public void testAddGrade() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Frayda");
        assertTrue(gb.addGrade("Frayda", 95));
        assertEquals(List.of(95), gb.findStudentGrades("Frayda").get());
    }

    @Test
    public void testAddGradeMissingStudent() {
        Gradebook gb = new Gradebook();
        assertFalse(gb.addGrade("Bluma", 88));
    }

    @Test
    public void testRemoveStudent() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Hinda");
        gb.addGrade("Hinda", 80);
        assertTrue(gb.removeStudent("Hinda"));
        assertFalse(gb.findStudentGrades("Hinda").isPresent());
    }

    @Test
    public void testRemoveStudentUndo() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Raizel");
        gb.addGrade("Raizel", 92);
        gb.removeStudent("Raizel");
        assertTrue(gb.undo());
        assertTrue(gb.findStudentGrades("Raizel").isPresent());
        assertEquals(List.of(92), gb.findStudentGrades("Raizel").get());
    }

    @Test
    public void testUndoAddGrade() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Tzivia");
        gb.addGrade("Tzivia", 77);
        assertTrue(gb.undo());
        assertEquals(0, gb.findStudentGrades("Tzivia").get().size());
    }

    @Test
    public void testAverageFor() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Gittel");
        gb.addGrade("Gittel", 90);
        gb.addGrade("Gittel", 100);
        Optional<Double> avg = gb.averageFor("Gittel");
        assertTrue(avg.isPresent());
        assertEquals(95.0, avg.get());
    }

    @Test
    public void testAverageForEmpty() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Faiga");
        assertTrue(gb.averageFor("Faiga").isEmpty());
    }

    @Test
    public void testLetterGradeFor() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Sura");
        gb.addGrade("Sura", 85);
        Optional<String> letter = gb.letterGradeFor("Sura");
        assertTrue(letter.isPresent());
        assertEquals("B", letter.get());
    }

    @Test
    public void testLetterGradeForEmpty() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Bracha");
        assertTrue(gb.letterGradeFor("Bracha").isEmpty());
    }

    @Test
    public void testClassAverage() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Hendel");
        gb.addStudent("Yocheved");
        gb.addGrade("Hendel", 80);
        gb.addGrade("Yocheved", 100);
        Optional<Double> avg = gb.classAverage();
        assertTrue(avg.isPresent());
        assertEquals(90.0, avg.get());
    }

    @Test
    public void testClassAverageEmpty() {
        Gradebook gb = new Gradebook();
        assertTrue(gb.classAverage().isEmpty());
    }

    @Test
    public void testRecentLog() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Goldie");
        gb.addGrade("Goldie", 90);
        gb.addGrade("Goldie", 95);
        List<String> log = gb.recentLog(2);
        assertEquals(2, log.size());
        assertTrue(log.get(0).contains("Added grade 95"));
        assertTrue(log.get(1).contains("Added grade 90"));
    }
}