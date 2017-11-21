package cc.dhandho.util;

public class Tuple2<A, B> {

	public A a;

	public B b;

	public static <A, B> Tuple2<A, B> valueOf(A a, B b) {
		Tuple2<A, B> rt = new Tuple2<>();
		rt.a = a;
		rt.b = b;
		return rt;
	}
}