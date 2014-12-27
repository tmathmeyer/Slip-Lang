package com.tmathmeyer.ci;

import com.tmathmeyer.ci.values.ImmutableList;

public class EmptyList<T> extends ImmutableList<T>
{

    @Override
    public T first() {
        return null;
    }

    @Override
    public ImmutableList<T> rest() {
        return this;
    }

    @Override
    public ImmutableList<T> add(T t) {
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
}