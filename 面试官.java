package com.test;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

class Interval {
	int start = 0;
	int end = 0;

	public Interval(int start, int end) {
		this.start = start;
		this.end = end;
	}
}

public class MSG {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		Interval[] intervals = new Interval[n];
		for (int i = 0; i < n; i++) {
			intervals[i] = new Interval(in.nextInt(), in.nextInt());
		}
		MSG m = new MSG();
		int size = m.minMeetingRooms(intervals);
		System.out.println(size);
	}

	public int minMeetingRooms(Interval[] intervals) {
		if (intervals == null || intervals.length == 0) {
			return 0;
		}
		// 按照开始时间排序
		Arrays.sort(intervals, (o1, o2) -> (o1.start - o2.start));
		// 按照结束时间排序
		PriorityQueue<Interval> heap = new PriorityQueue<Interval>((o1, o2) -> (o1.end - o2.end));
		heap.offer(intervals[0]);

		// 逐个添加面试官,如果你的开始时间>已有会议的面试官结束时间，就不用开辟会议室了
		// 否则需要新开一个
		for (int i = 1; i < intervals.length; i++) {
			Interval tmp = heap.poll();
			if (tmp.end <= intervals[i].start) {
				tmp.end = intervals[i].end;
			} else {
				heap.offer(intervals[i]);
			}
			heap.offer(tmp);
		}

		return heap.size();
	}

	

}
