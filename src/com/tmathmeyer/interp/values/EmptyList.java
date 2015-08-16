package com.tmathmeyer.interp.values;

import java.util.Iterator;
import java.util.function.Consumer;

import com.tmathmeyer.interp.expr.Function;

public class EmptyList<T> extends ImmutableList<T>
{
	@Override
	public T first()
	{
		return null;
	}

	@Override
	public ImmutableList<T> rest()
	{
		return this;
	}

	@Override
	public ImmutableList<T> add(T t)
	{
		return new List<T>(t, this);
	}

	@Override
	public boolean isEmpty()
	{
		return true;
	}

	@Override
	public ImmutableList<T> reverse()
	{
		return this;
	}

	@Override
	public ImmutableList<T> addEnd(T t)
	{
		return add(t);
	}

	@Override
	public String toString()
	{
		return "[]";
	}

	@Override
	public String asCSV()
	{
		return "";
	}

	public boolean equals(Object other)
	{
		return other != null && other instanceof EmptyList;
	}

	@Override
	public boolean contains(T name)
	{
		return false;
	}

	@Override
	public <R> ImmutableList<R> map(Function<? super T, ? extends R> arg0)
	{
		return new EmptyList<>();
	}

	@Override
	public ImmutableList<T> filter(Function<? super T, Boolean> arg0)
	{
		return this;
	}

	@Override
	public void forEach(Consumer<? super T> fn)
	{
	}

	@Override
	public Iterator<T> iterator()
	{
		return new Iterator<T>() {
			@Override
			public boolean hasNext()
			{
				return false;
			}

			@Override
			public T next()
			{
				return null;
			}
		};
	}

	@Override
	public ImmutableList<T> append(ImmutableList<T> result)
	{
		return result;
	}

	@Override
	public int size()
	{
		return 0;
	}

	@Override
    public String getTypeName()
    {
	    return "list";
    }
}