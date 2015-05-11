package com.tmathmeyer.interp.values;

import com.tmathmeyer.interp.Function;
import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.types.Value;

/**
 * Created by ted on 12/19/14.
 */
public abstract class ImmutableList<T> implements Value, Iterable<T>
{
	public abstract T first();

	public abstract ImmutableList<T> rest();

	public abstract ImmutableList<T> add(T t);

	public abstract ImmutableList<T> reverse();

	public abstract ImmutableList<T> addEnd(T t);

	public abstract boolean isEmpty();

	public abstract String asCSV();

	public abstract boolean contains(T name);

	public abstract <R> ImmutableList<R> map(Function<? super T, ? extends R> arg0);

	public abstract ImmutableList<T> filter(Function<? super T, Boolean> arg0);

	public static <T> ImmutableList<T> fromSTD(java.util.List<T> list)
	{
		ImmutableList<T> result = new EmptyList<>();
		for (int i = list.size(); i > 0; i--)
		{
			result = result.add(list.get(i - 1));
		}
		return result;
	}

	public abstract ImmutableList<T> append(ImmutableList<T> result);

	public abstract int size();

}
