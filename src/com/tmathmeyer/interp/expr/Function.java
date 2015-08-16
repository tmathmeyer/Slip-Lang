package com.tmathmeyer.interp.expr;

/**
 * Created by ted on 12/19/14.
 */
public interface Function<I, O>
{
	public O eval(I in);

	public class Pair<A, B>
	{
		public final A a;
		public final B b;

		public Pair(A aa, B bb)
		{
			a = aa;
			b = bb;
		}
	}
}
