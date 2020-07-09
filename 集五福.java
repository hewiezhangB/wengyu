package com.rst;

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
public class Test2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] numbers = line.split(",");
        //，必须为英文格式
        if(!checkParm(line)) {
            System.out.println("参数输入有误");
            return;
        }
        int[] nums = new int[]{0, 0, 0, 0, 0};
        for (int i = 0; i < numbers.length; i++) {
            char[] luCharacter = numbers[i].toCharArray();
            for (int j = 0; j < luCharacter.length; j++) {
                if (luCharacter[j] == '1') {
                	nums[j]++;
                }
            }
        }
        //冒泡
        Arrays.sort(nums);
        System.out.println(nums[0]);
    }
    
    //参数校验，数字或","
    public static boolean checkParm(String line) {       
        for(int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if(Character.isDigit(ch) || ch == ',') {
            	return true;
            }
        }
        return false;
    }
}
