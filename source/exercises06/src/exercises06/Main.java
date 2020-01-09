package exercises06;

import java.util.List;
import java.util.stream.Stream;
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
					flatMap((Object o) -> Stream.iterate(o, (Object i) -> {return i;})).
					toArray();
		return r;
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
		System.out.println(replIter(l2.toArray(), 6));
		System.out.println(replStream(l2.toArray(), 6));
	}

}
