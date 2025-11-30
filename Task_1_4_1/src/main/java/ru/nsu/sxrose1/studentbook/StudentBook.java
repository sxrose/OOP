package ru.nsu.sxrose1.studentbook;

import ru.nsu.sxrose1.studentbook.data.SubjectHeader;
import ru.nsu.sxrose1.studentbook.subjects.Subject;

import java.util.IdentityHashMap;

public class StudentBook {
    private IdentityHashMap<Subject, SubjectHeader> subjects;
}
