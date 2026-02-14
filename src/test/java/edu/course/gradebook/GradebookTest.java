package edu.course.gradebook;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class GradebookTest {

    @Test
    public void testAddStudent() {
        Gradebook gb = new Gradebook();
        assertTrue(gb.addStudent("Leah"));
        assertFalse(gb.addStudent("Leah"));
        assertTrue(gb.findStudentGrades("Leah").isPresent());
    }

    @Test
    public void testAddGrade() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Rivka");
        assertTrue(gb.addGrade("Rivka", 95));
        assertEquals(List.of(95), gb.findStudentGrades("Rivka").get());
    }

    @Test
    public void testAddGradeMissingStudent() {
        Gradebook gb = new Gradebook();
        assertFalse(gb.addGrade("Chaya", 88));
    }

    @Test
    public void testRemoveStudent() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Sara");
        gb.addGrade("Sara", 80);
        assertTrue(gb.removeStudent("Sara"));
        assertFalse(gb.findStudentGrades("Sara").isPresent());
    }

    @Test
    public void testRemoveStudentUndo() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Miriam");
        gb.addGrade("Miriam", 92);
        gb.removeStudent("Miriam");
        assertTrue(gb.undo());
        assertTrue(gb.findStudentGrades("Miriam").isPresent());
        assertEquals(List.of(92), gb.findStudentGrades("Miriam").get());
    }

    @Test
    public void testUndoAddGrade() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Esther");
        gb.addGrade("Esther", 77);
        assertTrue(gb.undo());
        assertEquals(0, gb.findStudentGrades("Esther").get().size());
    }

    @Test
    public void testAverageFor() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Devorah");
        gb.addGrade("Devorah", 90);
        gb.addGrade("Devorah", 100);
        Optional<Double> avg = gb.averageFor("Devorah");
        assertTrue(avg.isPresent());
        assertEquals(95.0, avg.get());
    }

    @Test
    public void testAverageForEmpty() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Tova");
        assertTrue(gb.averageFor("Tova").isEmpty());
    }

    @Test
    public void testLetterGradeFor() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Batya");
        gb.addGrade("Batya", 85);
        Optional<String> letter = gb.letterGradeFor("Batya");
        assertTrue(letter.isPresent());
        assertEquals("B", letter.get());
    }

    @Test
    public void testLetterGradeForEmpty() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Shira");
        assertTrue(gb.letterGradeFor("Shira").isEmpty());
    }

    @Test
    public void testClassAverage() {
        Gradebook gb = new Gradebook();
        gb.addStudent("Yael");
        gb.addStudent("Hadas");
        gb.addGrade("Yael", 80);
        gb.addGrade("Hadas", 100);
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
        gb.addStudent("Leah");
        gb.addGrade("Leah", 90);
        gb.addGrade("Leah", 95);

        List<String> log = gb.recentLog(2);
        assertEquals(2, log.size());
        assertTrue(log.get(0).contains("Added grade 95"));
        assertTrue(log.get(1).contains("Added grade 90"));
    }
}