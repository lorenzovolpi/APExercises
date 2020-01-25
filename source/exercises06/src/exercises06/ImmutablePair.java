package exercises06;

public class ImmutablePair<T1, T2> {
	private T1 e1;
	private T2 e2;
	
	public ImmutablePair(T1 e1, T2 e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	
	T1 getFirst() {
		return e1;
	}
	
	T2 getSecond() {
		return e2;
	}
		
	@Override
	public String toString() {
		return e1.toString() + "; " + e2.toString();
	}
	
}
