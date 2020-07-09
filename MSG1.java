package com.test;

import java.util.Arrays;

public class MSG1 {
    public int minMeetingRooms(Interval[] intervals) {
        int[] starts = new int[intervals.length];
        int[] ends = new int[intervals.length];
        for(int i=0; i<intervals.length; i++) {
            starts[i] = intervals[i].start;
            ends[i] = intervals[i].end;
        }
        Arrays.sort(starts);
        Arrays.sort(ends);
        int rooms = 0;
        int activeMeetings = 0;
        int i=0, j=0;
        while (i < intervals.length && j < intervals.length) {
            if (starts[i] < ends[j]) {
                activeMeetings ++;
                i ++;
            } else {
                activeMeetings --;
                j ++;
            }
            rooms = Math.max(rooms, activeMeetings);
        }
        return rooms;
    }
}
