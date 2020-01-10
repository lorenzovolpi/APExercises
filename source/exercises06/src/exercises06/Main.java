package exercises06;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
	
	public static int sumOddIter(List<Integer> list) {
		int sum = 0;
		for(int i : list) {
			sum += i%2 == 1 ? i : 0;
		}
		
		return sum;
	}
	
	public static int sumOddStrem(List<Integer> list) {
		int sum = list.
				stream().
				filter(a -> a%2 == 1).
				reduce(0, (a1, a2) -> a1 + a2);
		
		return sum;
	}
	
	public static ImmutablePair<Integer, Double> someCalculationIter(List<Double> lst) {
		int f = 0;
		for(int i = 0; i<lst.size(); ++i) {
			if(lst.get(i) >= 0.2 && lst.get(i) <= Math.PI) f++;
		}
		double s = 0;
		int c = 0;
		for(int i = 0; i<lst.size(); ++i) {
			if(lst.get(i) >= 10 && lst.get(i) <= 100) {
				s += lst.get(i);
				c++;
			}
		}
		s /= c;
		
		return new ImmutablePair<Integer, Double>(f, s);
	}
	
	public static ImmutablePair<Integer, Double> someCalculationStream(List<Double> lst) {
		int f = lst.stream().
					filter(e -> e>=0.2 && e<=Math.PI).
					map(e -> 1).
					reduce(0, (e1, e2) -> e1 + e2);
		
		int c = 0;
		Double[] p = lst.stream().
						filter(e -> e >= 10 && e <= 100).
						collect(() -> new Double[] {0.0, 0.0},
								(p1, e) -> { p1[0] += e; p1[1]++; },
								(p1, p2) -> { p1[0] += p2[0]; p1[1] += p2[1]; } );
		
		return new ImmutablePair<Integer, Double>(f, p[0]/p[1]);
	}
	
	public static Object[] replIter(Object[] a, int n) {
		ArrayList<Object> r = new ArrayList<>();
		for(int i = 0; i<a.length; ++i) {
			for(int j = 0; j<n; ++j) r.add(a[i]);
		}
		
		return r.toArray();
	}
	
	public static Object[] replStream(Object[] a, int n) {
		List<Object> l = Arrays.asList(a);
		Object[] r = l.stream().
					flatMap((Object o) -> Stream.iterate(o, (Object i) -> {return i;}).limit(n)).
					toArray();
		return r;
	}
	
	public static <T> T[] replStreamL(Class<T> c, List<T> a, int n) {
		List<T> r = a.stream().<T>flatMap((T t) -> Stream.<T>iterate(t, i -> i).limit(n)).collect(Collectors.toList());				
		@SuppressWarnings("unchecked")
		T[] a1 = (T[])Array.newInstance(c, n*a.size());
		return r.toArray(a1);
	}
	
	public static String titlecaseStream(String s) {
		return Arrays.asList(s.split(" ")).
			stream().
			map(str -> str.substring(0, 1).toUpperCase() + str.substring(1)).
			collect(Collectors.joining(" "));
	}
	
	public static String titlecaseIter(String s) {
		String[] split = s.split(" ");
		StringBuilder b = new StringBuilder();
		for(int i = 0; i<split.length; ++i) {
			b.append(split[i].substring(0, 1).toUpperCase());
			b.append(split[i].substring(1));
			b.append(" ");
		}
		
		return b.toString();
	}
	
	public static <T> Stream<ImmutablePair<T, Integer>> zipWithIndex(T[] a) {
		return IntStream.
				range(0, a.length).
				mapToObj(i -> new ImmutablePair<T, Integer>(a[i], i));
	}
		
	public static <T> T[] filterOdd(Class<T> c, T[] xs) {
		List<T> l = zipWithIndex(xs).
					filter(p -> p.getSecond() % 2 == 0).
					map(p -> p.getFirst()).
					collect(Collectors.toList());
		@SuppressWarnings("unchecked")
		T[] a = (T[])Array.newInstance(c, l.size());
		return l.toArray(a);
	}
	
	public static void replaceWord(String fileName, String word, String repl) {
		try {
			Files.lines(Paths.get(Main.class.getResource("/" + fileName).toURI())).
				map(s -> s.replaceAll(word, repl)).
				forEach(s -> System.out.println(s));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public static long serialEvenSum(long threshold) {
		return LongStream.
				range(0, threshold + 1).
				filter(l -> l%2 == 0).
				sum();
	}
	
	public static long parallelEvenSum(long threshold) {
		return LongStream.
				range(0, threshold + 1).
				parallel().
				filter(l -> l%2 == 0).
				sum();
	}
	
	public static void testSum(long threshold) {
		long t0 = System.nanoTime();
		serialEvenSum(threshold);
		long t1 = System.nanoTime();
		parallelEvenSum(threshold);
		long t2 = System.nanoTime();
		
		System.out.println("Serial execution on threshold " + threshold + " took: " + (double)(t1 - t0)/1000000.0 + "ms");
		System.out.println("Parallel execution on threshold " + threshold + " took: " + (double)(t2 - t1)/1000000.0 + "ms");
	}
	
	public static Optional<Integer> getElement(Integer[] arr, int index) {
		if(index >=0 && index <arr.length) return Optional.<Integer>of(arr[index]);
		else return Optional.empty();
	}
	
	public static Optional<Double> sqrt(Optional<Integer> i) {
		if(i.isPresent() && i.get() >= 0) return Optional.of(Math.sqrt(i.get()));
		else return Optional.empty();
	}
	
	public static Optional<Integer> half(Optional<Integer> i) {
		if(i.isPresent() && i.get() % 2 == 0) return Optional.of(i.get() / 2);
		else return Optional.empty();
	}
	
	public static List<String> listDir(Path path) {
		try {
			return Files.walk(path).
					filter(p -> Files.isDirectory(p)).
					map(p -> p.toString()).
					collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<String>();
	}
	
	public static List<Subscriber> loadDatabase(String fileName) {
		try {
			return Files.lines(Paths.get(Main.class.getResource("/" + fileName).toURI())).
					skip(1).
					map(s -> Arrays.asList(s.split(",")).stream().map(s1 -> s1.strip()).collect(Collectors.toList())).
					map(al -> new Subscriber(al.get(0), al.get(1), al.get(2), al.get(3), al.get(4), al.get(5), al.get(6), al.get(7), al.get(8), al.get(9), al.get(10))).
					//forEach(sub -> System.out.println(sub));
					collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<Subscriber>();
	}
	
	public static void paymentFromGB(List<Subscriber> subs) {
		subs.stream().
			filter(sub -> sub.subscriptionPaid && sub.country.isPresent() && sub.country.get().equals("gb")).
			forEach(sub -> System.out.println(sub));
	}
	
	public static void main(String[] args) {
		
		//ex01
		List<Integer> l = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
		System.out.println(sumOddIter(l));
		System.out.println(sumOddStrem(l));
		
		//ex02
		List<Double> l1 = new ArrayList<>(Arrays.asList(new Double[] {0.1, 0.2, 3.0, 100.0, 500.0, 17.0}));
		System.out.println(someCalculationIter(l1));
		System.out.println(someCalculationStream(l1));
		
		//ex03
		List<Integer> l2 = new ArrayList<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}));
		for(Object i : Arrays.asList(replIter(l2.toArray(), 6))) {
			System.out.print(i + " ");
		}
		System.out.println();
		for(Object i : Arrays.asList(replStream(l2.toArray(), 6))) {
			System.out.print(i + " ");
		}
		System.out.println();
		for(Integer i : Arrays.asList(Main.<Integer>replStreamL(Integer.class, l2, 6))) {
			System.out.print(i + " ");
		}
		System.out.println();		
		
		//ex04
		String s = "red fox jumped over the lazy dog";
		System.out.println(titlecaseStream(s));
		System.out.println(titlecaseIter(s));
		
		//ex05
		Integer[] a5 = {1, 2, 3, 4, 5};
		for(Integer i : Arrays.asList(filterOdd(Integer.class, a5))) {
			System.out.print(i + " ");
		}
		System.out.println();
		
		//ex06
		replaceWord("people.csv", "true", "false");
		
		//ex07
		testSum(100);
		testSum(1000);
		testSum(100000);
		//testSum(Integer.MAX_VALUE);
		
		//ex08
		Integer a[] = {1,2,3,4,5,6};
		System.out.println(sqrt(half(getElement(a, 3))));
		
		//ex09
		for(String d : listDir(Paths.get("C:\\Users\\Lorenzo Volpi\\Desktop\\"))) {
			System.out.println(d);
		}
		
		//ex10
		paymentFromGB(loadDatabase("people.csv"));
	}

}
