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
        undoStack.push(gb -> {
            if (!gradeList.isEmpty()) {
                gradeList.remove(gradeList.size() - 1);
                gb.activityLog.add("Undo: removed grade " + grade + " for " + name);
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
        undoStack.push(gb -> {
            gb.gradesByStudent.put(name, new ArrayList<>(originalGrades));
            gb.activityLog.add("Undo: restored student " + name);
        });
        activityLog.add("Removed student " + name);
        return true;
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

    public Optional<Double> averageFor(String name) {
        Optional<List<Integer>> gradesOptional = findStudentGrades(name);
        if (gradesOptional.isEmpty() || gradesOptional.get().isEmpty()) {
            return Optional.empty();
        }
        List<Integer> grades = gradesOptional.get();
        double avg = grades.stream().mapToInt(Integer::intValue).average().orElse(Double.NaN);
        return Optional.of(avg);
    }

    public Optional<String> letterGradeFor(String name) {
        Optional<Double> avgOptional = averageFor(name);
        if (avgOptional.isEmpty()) {
            return Optional.empty();
        }
        double avg = avgOptional.get();
        if (avg >= 90) return Optional.of("A");
        if (avg >= 80) return Optional.of("B");
        if (avg >= 70) return Optional.of("C");
        if (avg >= 60) return Optional.of("D");
        return Optional.of("F");
    }

    public Optional<Double> classAverage() {
        if (gradesByStudent.isEmpty()) {
            return Optional.empty();
        }
        List<Integer> allGrades = gradesByStudent.values().stream()
                .flatMap(Collection::stream)
                .toList();
        if (allGrades.isEmpty()) {
            return Optional.empty();
        }
        double avg = allGrades.stream().mapToInt(Integer::intValue).average().orElse(Double.NaN);
        return Optional.of(avg);
    }

    public List<String> recentLog(int n) {
        int size = activityLog.size();
        if (size == 0) {
            return List.of();
        }
        int fromIndex = Math.max(size - n, 0);
        List<String> subList = activityLog.subList(fromIndex, size);
        List<String> reversed = new ArrayList<>(subList);
        Collections.reverse(reversed);
        return reversed;
    }
}