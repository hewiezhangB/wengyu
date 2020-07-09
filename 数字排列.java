package com.test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Tesr {	
	public static void main(String[] args) {
		//接受控制台信息
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] numbers = line.split(",");
        //，必须为英文格式
        if(!checkParm(line) || numbers.length != 3) {
            System.out.println("参数输入有误");
            return;
        }
        int a = 0, b = 0, c = 0;
        //控制台输入的String类型转换为int
        try{
            a = Integer.parseInt(numbers[0]);
            b = Integer.parseInt(numbers[1]);
            c = Integer.parseInt(numbers[2]);
        } catch(Exception ex) {
            System.out.println("数字转换异常");
            return;
        }
        //判断数字的边界
        if(a <= 0 || a > 9 || b <= 0 || b > 9 || c <= 0 || c > 9) {
            return;
        }
        //判断是否相等
        if(a == b || a == c || b == c) {       
            return;
        }
        //set集合元素不能重复，适用于注意中的：1，2
        //判断是否有同时存在2和5、6和9的情况 
        Set<Integer> numSet = new HashSet<Integer>();    
        numSet.add(a); 
        numSet.add(b); 
        numSet.add(c);
        if((numSet.contains(2) && numSet.contains(5))) {
        	System.out.println("2和5同事存在");
            return;
        }
        if((numSet.contains(6) && numSet.contains(9))){
        	System.out.println("6和9同事存在");
        	return;
        }
        //判断输入三数字最大值
        int maxNum = Math.max(a, Math.max(c, b));
        ArrayList<Integer> numList = new ArrayList<Integer>();
        threeNumbersSort(numList, a, b, c);
        // 对2和5、6和9的使用，三个数字都可以使用，并计算组成的集合(7和15	做数字调换使用)
        if(a == 2 || a == 5) {
        	threeNumbersSort(numList, 7 - a, b, c);
        }
        if(b == 2 || b == 5){
        	threeNumbersSort(numList, a, 7 - b, c);
        }
        if(c == 2 || c == 5){
        	threeNumbersSort(numList, a, b, 7 - c);
        }
        if(a == 6 || a == 9){
        	threeNumbersSort(numList, 15 - a, b, c);
        }
        if(b == 6 || b == 9){
        	threeNumbersSort(numList, a, 15 - b, c);
        }
        if(c == 6 || c == 9){
        	threeNumbersSort(numList, a, b, 15 - c);
        }

        if((numSet.contains(2) || numSet.contains(5)) && (numSet.contains(6) || numSet.contains(9))) {
            int x = numSet.contains(2)? 2 : 5;
            int y = numSet.contains(6)? 6 : 9;
            int z = a + b + c - x - y;
            threeNumbersSort(numList, 7 - x, 15 - y, z);
        }
        removeSort(numList);
        //快速排序和优化的归并排序，对数字排序
        Collections.sort(numList);
        System.out.println(numList.get(maxNum - 1));
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
    
	//取三位数字分个位，十位，百位，放入链表(相当于计算他们所能组成的所有数字)
    public static void threeNumbersSort(ArrayList<Integer> numList, int a, int b, int c) {
    	//个位
    	numList.add(a);
    	numList.add(b);
    	numList.add(c);
        //十位
    	numList.add(a * 10 + b);
    	numList.add(a * 10 + c);
    	numList.add(b * 10 + a);
    	numList.add(b * 10 + c);
    	numList.add(c * 10 + a);
    	numList.add(c * 10 + b);
    	//百位
    	numList.add(a * 100 + b * 10 + c);
    	numList.add(a * 100 + c * 10 + b);
    	numList.add(b * 100 + a * 10 + c);
    	numList.add(b * 100 + c * 10 + a);
    	numList.add(c * 100 + b * 10 + a);
    	numList.add(c * 100 + a * 10 + b);
    }

    //去调重复排序的数字,又使用set
    public static void removeSort(ArrayList<Integer> numList) {
        Set<Integer> setNum = new HashSet<Integer>();
        //链表的
        for(Integer numL : numList){
        	setNum.add(numL);
        }
        numList.removeAll(numList);
        //集合的
        for(Integer setN : setNum){
        	numList.add(setN);
        }
    }
}