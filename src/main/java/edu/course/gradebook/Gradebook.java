package edu.course.gradebook;

import java.util.*;

public class Gradebook {

    private final Map<String, List<Integer>> gradesByStudent = new HashMap<>();
    private final Deque<UndoAction> undoStack = new ArrayDeque<>();
    private final LinkedList<String> activityLog = new LinkedList<>();

    public Optional<List<Integer>> findStudentGrades(String name) {
        return Optional.ofNullable(gradesByStudent.get(name));
    }

    public boolean addStudent(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (gradesByStudent.containsKey(name)) {
            return false;
        }
        gradesByStudent.put(name, new ArrayList<>());
        activityLog.add("Added student " + name);
        return true;
    }

    public boolean addGrade(String name, int grade) {
        Optional<List<Integer>> gradesOptional = findStudentGrades(name);
        if (gradesOptional.isEmpty()) {
            return false;
        }

        List<Integer> gradeList = gradesOptional.get();
        gradeList.add(grade);

        List<Integer> listForUndo = gradeList;

        undoStack.push(gb -> {
            if (!listForUndo.isEmpty()) {
                listForUndo.remove(listForUndo.size() - 1);
            }
        });

        activityLog.add("Added grade " + grade + " for " + name);
        return true;
    }

    public boolean removeStudent(String name) {
        Optional<List<Integer>> gradesOptional = findStudentGrades(name);
        if (gradesOptional.isEmpty()) {
            return false;
        }

        List<Integer> originalGrades = new ArrayList<>(gradesOptional.get());
        gradesByStudent.remove(name);

        undoStack.push(gb -> gb.gradesByStudent.put(name, new ArrayList<>(originalGrades)));

        activityLog.add("Removed student " + name);
        return true;
    }

    public Optional<Double> averageFor(String name) {
        var gradesOpt = findStudentGrades(name);
        if (gradesOpt.isEmpty()) {
            return Optional.empty();
        }

        var grades = gradesOpt.get();
        if (grades.isEmpty()) {
            return Optional.empty();
        }

        double sum = 0.0;
        for (var g : grades) {
            sum += g;
        }

        double avg = sum / grades.size();
        return Optional.of(avg);
    }

    public Optional<String> letterGradeFor(String name) {
        var avgOpt = averageFor(name);
        if (avgOpt.isEmpty()) {
            return Optional.empty();
        }

        double avg = avgOpt.get();
        int bucket = (int) avg / 10;

        String letter = switch (bucket) {
            case 10, 9 -> {
                yield "A";
            }
            case 8 -> {
                yield "B";
            }
            case 7 -> {
                yield "C";
            }
            case 6 -> {
                yield "D";
            }
            default -> {
                yield "F";
            }
        };

        return Optional.of(letter);
    }

    public Optional<Double> classAverage() {
        double sum = 0.0;
        int count = 0;

        for (var entry : gradesByStudent.values()) {
            for (var grade : entry) {
                sum += grade;
                count++;
            }
        }

        if (count == 0) {
            return Optional.empty();
        }

        double avg = sum / count;
        return Optional.of(avg);
    }

    public boolean undo() {
        if (undoStack.isEmpty()) {
            return false;
        }

        UndoAction action = undoStack.pop();
        action.undo(this);
        activityLog.add("Undid last action");
        return true;
    }

    public List<String> recentLog(int maxItems) {
        if (maxItems <= 0) {
            return List.of();
        }

        var result = new ArrayList<String>();
        var it = activityLog.descendingIterator();
        while (it.hasNext() && result.size() < maxItems) {
            result.add(it.next());
        }
        return result;
    }
}