package com.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class St {
	public static String getPermutation(int n, int k) {
	    int[] factorials = new int[n];
	    List<Integer> nums = new ArrayList() {{add(1);}};

	    factorials[0] = 1;
	    for(int i = 1; i < n; ++i) {
	      // generate factorial system bases 0!, 1!, ..., (n - 1)!
	      factorials[i] = factorials[i - 1] * i;
	      // generate nums 1, 2, ..., n
	      nums.add(i + 1);
	    }

	    // fit k in the interval 0 ... (n! - 1)
	    --k;

	    // compute factorial representation of k
	    StringBuilder sb = new StringBuilder();
	    for (int i = n - 1; i > -1; --i) {
	      int idx = k / factorials[i];
	      k -= idx * factorials[i];

	      sb.append(nums.get(idx));
	      nums.remove(idx);
	    }
	    return sb.toString();
	  }
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String numlin = sc.nextLine();
		String[] numGr = numlin.trim().split(",");
		int n = Integer.parseInt(numGr[0].split("=")[1].toString());
		int k = Integer.parseInt(numGr[1].split("=")[1].toString());
		String num = getPermutation(n, k);
		System.out.println(num);
	}
}
