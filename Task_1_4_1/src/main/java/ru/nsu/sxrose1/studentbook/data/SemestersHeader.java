package ru.nsu.sxrose1.studentbook.data;

import ru.nsu.sxrose1.studentbook.assessments.Assessment;

import java.util.IdentityHashMap;
import java.util.Objects;

public class SemestersHeader {
    private class SemesterNode {
        private SemesterNode next = null;

        public final int semesterNo;
        public final IdentityHashMap<Assessment, AssessmentInfo> assessments =
                new IdentityHashMap<>();

        public SemesterNode(int semesterNo) {
            this.semesterNo = semesterNo;
        }

        private void insertNext(SemesterNode node) {
            assert (Objects.isNull(node.next));
            var newNext = this.next;
            this.next = node;
            node.next = next;
        }
    }

    private SemesterNode head = null;
    private SemesterNode last = null;
}
