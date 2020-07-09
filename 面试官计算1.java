package test1.test1;

import java.util.ArrayList;
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
		String line = in.nextLine();
		String[] input = line.split(" ");
		Interval[] intervals = new Interval[input.length/2];
		for (int i = 0; i < input.length / 2; i++) {
			int s = Integer.parseInt(input[i].toString());
			int e = Integer.parseInt(input[i+1].toString());
			intervals[i] = new Interval(s, e);
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

		// 逐个添加面试官,如果你的开始时间>已有面试的结束时间，就不用分配面试官
		// 否则一个面试官
		int n = 0;
		for (int i = 1; i < intervals.length; i++) {
			Interval tmp = heap.poll();
			if (tmp.end <= intervals[i].start && n < 2) {
				tmp.end = intervals[i].end;
				++ n;
			} else {
				heap.offer(intervals[i]);
			}
			heap.offer(tmp);
		}

		return heap.size();
	}
}
