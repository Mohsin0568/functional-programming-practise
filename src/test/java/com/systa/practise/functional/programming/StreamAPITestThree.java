/**
 * 
 */
package com.systa.practise.functional.programming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mohsin
 *
 */

@Slf4j
public class StreamAPITestThree {
	
	
	// convert a two dimensional array to single dimensional array
	@Test
	public void test1() {
		int[][] twoDimensionalArray = {{ 1, 2 }, { 3, 4, 5, 6 }, { 7, 8, 9 }};
		
		
		List<Integer> list = new ArrayList<>();
		
		for(int[] x : twoDimensionalArray) {
			Arrays.stream(x).forEach(list :: add);
		}
		
		Integer[] finalOutput = list.stream().toArray(Integer[] :: new);
		
		Arrays.stream(finalOutput).forEach(a -> log.info(a.toString()));
		
	}
	
	// convert a list of arrays to a single list
	@Test
	public void test2() {
		
		List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
		List<Integer> list2 = Arrays.asList(6, 7, 8, 9, 10);
		List<Integer> list3 = Arrays.asList(11, 12, 13, 14, 15);
		List<Integer> list4 = Arrays.asList(16, 17, 18, 19, 20);
		
		List<List<Integer>> masterList = Arrays.asList(list1, list2, list3, list4);
		
//		System.out.println(masterList);
		
		List<Integer> outputList = masterList.stream().flatMap(list -> list.stream()).collect(Collectors.toList());
		
		outputList.stream().forEach(a -> log.info(a.toString()));		
	}
	
	// convert a map containing lists in values, take lists available in values and make a combined list
	@Test
	public void test3() {
		
		List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
		List<Integer> list2 = Arrays.asList(6, 7, 8, 9, 10);
		List<Integer> list3 = Arrays.asList(11, 12, 13, 14, 15);
		List<Integer> list4 = Arrays.asList(16, 17, 18, 19, 20);
		
		Map<Integer, List<Integer>> masterMap = new HashMap<>();
		List<Integer> outputList = new ArrayList<>();
		masterMap.put(1, list1);
		masterMap.put(2, list2);
		masterMap.put(3, list3);
		masterMap.put(4, list4);
		
		masterMap.entrySet().forEach(a -> outputList.addAll(a.getValue()));
		
		outputList.stream().forEach(a -> log.info(a.toString()));
		
	}
	
	// convert a string to list of characters
	@Test
	public void test4() {
		
		String str = "This is a demo string";
		
		List<Character> chars = str.chars().mapToObj(a -> (char)a).collect(Collectors.toList());
		
		chars.stream().forEach(a -> log.info(a.toString()));
	}
	
	// convert an IntStream to a string
	@Test
	public void test5() {
		
		String str = IntStream.range(0, 100).mapToObj(Integer :: toString).collect(Collectors.joining());
		log.info(str);		
	}
	
	// convert string to intStream
	@Test
	public void test6() {
		
		String str = "This is a demo string";
		
		str.chars();
		
	}
	
	// remove an element from an array in java
	@Test
	public void test7() {
		
		int[] inputArray = { 1, 2, 3, 4, 5 };
		int index = 2;
		
		int[] outputArray = IntStream.range(0, inputArray.length)
					.filter(i -> i != index)
					.map(i -> inputArray[i])
					.toArray();
		
		Arrays.stream(outputArray).forEach(i -> log.info(Integer.toString(i)));
		
	}
	
	// convert boxed array to a stream in java
	public void test8() {
		
		String array[] = { "Geeks", "forGeeks", "A Computer Portal" };
		Arrays.stream(array);
		
		Stream.of(array);
		
		Arrays.asList(array).stream();
		
	}
	
	// convert primitive array to a stream in java
	public void test9() {
		
		int[] inputArray = { 1, 2, 3, 4, 5 };
		
		Arrays.stream(inputArray);
		
		Stream.of(inputArray);
		
	}

}
