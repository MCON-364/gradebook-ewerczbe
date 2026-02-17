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
        assertFalse(gb.addGrade("Blimy", 88));
    }

    @Test
    public void testRemoveStudent() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Hindy");
        gb.addGrade("Hindy", 80);
        assertTrue(gb.removeStudent("Hindy"));
        assertFalse(gb.findStudentGrades("Hindy").isPresent());
    }

    @Test
    public void testRemoveStudentUndo() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Raizy");
        gb.addGrade("Raizy", 92);
        gb.removeStudent("Raizy");
        assertTrue(gb.undo());
        assertTrue(gb.findStudentGrades("Raizy").isPresent());
        assertEquals(List.of(92), gb.findStudentGrades("Raizy").get());
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
    public void testUndoDoesNotUndoAddStudent() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Gitty");
        assertFalse(gb.undo());
        assertTrue(gb.findStudentGrades("Gitty").isPresent());
    }

    @Test
    public void testUndoLogsAction() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Faigy");
        gb.addGrade("Faigy", 90);
        gb.undo();
        List<String> log = gb.recentLog(2);
        assertTrue(log.stream().anyMatch(s -> s.contains("Undo")));
    }

    @Test
    public void testUndoHandlesMultipleOperations() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Sura");
        gb.addGrade("Sura", 80);
        gb.addGrade("Sura", 90);
        gb.undo();
        assertEquals(List.of(80), gb.findStudentGrades("Sura").get());
    }

    @Test
    public void testAverageFor() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Bracha");
        gb.addGrade("Bracha", 90);
        gb.addGrade("Bracha", 100);
        Optional<Double> avg = gb.averageFor("Bracha");
        assertTrue(avg.isPresent());
        assertEquals(95.0, avg.get());
    }

    @Test
    public void testAverageForEmpty() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Hendel");
        assertTrue(gb.averageFor("Hendel").isEmpty());
    }

    @Test
    public void testLetterGradeFor() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Yocheved");
        gb.addGrade("Yocheved", 85);
        Optional<String> letter = gb.letterGradeFor("Yocheved");
        assertTrue(letter.isPresent());
        assertEquals("B", letter.get());
    }

    @Test
    public void testLetterGradeForEmpty() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Goldie");
        assertTrue(gb.letterGradeFor("Goldie").isEmpty());
    }

    @Test
    public void testClassAverage() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Frayda");
        gb.addStudent("Bluma");
        gb.addGrade("Frayda", 80);
        gb.addGrade("Bluma", 100);
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
        gb.addStudent("Hindy");
        gb.addGrade("Hindy", 90);
        gb.addGrade("Hindy", 95);
        List<String> log = gb.recentLog(3);
        assertTrue(log.stream().anyMatch(s -> s.contains("Added grade")));
    }
}