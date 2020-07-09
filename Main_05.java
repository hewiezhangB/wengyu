package com.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * 屏幕给出1~9中任意3个不重复的数字，大家以最快的时间给出这几个数字可以拼成的数字从小到大排列位于第N位置的数字，其中N为给出数字中最大的（如果不到这么多数字，则给出最后一个即可）。
 * 注意： 1、2可以当作5来使用，5可以当作2来使用进行数字拼接，且屏幕不能同时给出2和5；
 * 2、6可以当作9来使用，9可以当作6来使用进行数字拼接，且屏幕不能同时给出6和9；
 * 如果给出1，4，8，则可以拼接成的数字为1，4，8，14，18，41，48，81，84，148，184，418，481，814，841。那么最第N（即8)个数字为81。
 * 输入描述：输入以逗号分隔的描述3个int类型整数的字符串。
 * 
 * 输出描述： 输出为这几个数字可以拼成的数字，从小到大排列于第N（N为输入数字中国最大的数字）位置的数字，如果输入的数字不在范围内或者有重复，则输出-1。
 * 示例： 输入 1，4，8 输出 81
 * 
 * @author mi
 *
 */
public class Main_05 {
	public static void addThreeNumber(ArrayList<Integer> lst, int a, int b, int c) {
		lst.add(a);
		lst.add(b);
		lst.add(c);

		lst.add(10 * a + b);
		lst.add(10 * a + c);
		lst.add(10 * b + a);
		lst.add(10 * b + c);
		lst.add(10 * c + a);
		lst.add(10 * c + b);

		lst.add(100 * a + 10 * b + c);
		lst.add(100 * a + 10 * c + b);
		lst.add(100 * b + 10 * a + c);
		lst.add(100 * b + 10 * c + a);
		lst.add(100 * c + 10 * a + b);
		lst.add(100 * c + 10 * b + a);

	}

	// 去除重复数字
	public static void remove(ArrayList<Integer> lst) {
		Set<Integer> set = new HashSet<Integer>();
		for (Integer x : lst) {
			set.add(x);
			lst.removeAll(lst);
		}
		for (Integer x : set) {
			lst.add(x);
		}

	}

	public static boolean check(String line) {
		for (int i = 0; i < line.length(); i++) {
			char ch = line.charAt(i);
			if (!(Character.isDefined(ch) || ch == ',')) {
				return false;
			}

		}
		return true;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		String[] number = line.split(",");
		if (!check(line) || number.length != 3) {
			System.out.println(-1);
			return;
		}
		int a = 0, b = 0, c = 0;
		try {
			a = Integer.parseInt(number[0]);
			b = Integer.parseInt(number[1]);
			c = Integer.parseInt(number[2]);
		} catch (NumberFormatException e) {
			System.out.println(-2);
			return;

		}
		if (a <= 0 || a > 9 || b <= 0 || b > 9 || c <= 0 || c > 0) {
			System.out.println(-1);
		}
		if (a == b || a == c || b == c) {
			System.out.println(-3);
		}
		Set<Integer> s = new HashSet<Integer>();
		s.add(a);
		s.add(b);
		s.add(c);
		if (s.contains(2) && s.contains(5) || s.contains(6) && s.contains(9)) {
			System.out.println(-4);
			return;

		}
		int max = Math.max(a, Math.max(b, c));
		ArrayList<Integer> lst = new ArrayList<Integer>();
		addThreeNumber(lst, a, b, c);
		if (a == 2 || a == 5)
			addThreeNumber(lst, 7 - a, b, c);
		if (b == 2 || b == 5)
			addThreeNumber(lst, a, 7 - b, c);
		if (c == 2 || c == 5)
			addThreeNumber(lst, a, b, 7 - c);
		if (a == 6 || a == 9)
			addThreeNumber(lst, 15 - a, b, c);
		if (b == 6 || b == 9)
			addThreeNumber(lst, a, 15 - b, c);
		if (c == 6 || c == 9)
			addThreeNumber(lst, a, b, 15 - c);
		// 这个if没有看懂
		if ((s.contains(2) || s.contains(5)) && (s.contains(6) || s.contains(9))) {
			int x = s.contains(2) ? 2 : 5;
			int y = s.contains(6) ? 6 : 9;
			int z = a + b + c - x - y;
			addThreeNumber(lst, 7 - x, 15 - y, z);
		}
		remove(lst);
		Collections.sort(lst);
		System.out.println(lst.get(max) - 1);

	}

}
