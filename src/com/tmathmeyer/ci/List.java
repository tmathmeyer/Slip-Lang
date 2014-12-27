package com.tmathmeyer.ci;

import com.tmathmeyer.ci.values.ImmutableList;

public class List<T> extends ImmutableList<T>
{
    private final T data;
    private final ImmutableList<T> rest;

    public List(T data, ImmutableList<T> rest)
    {
        this.data = data;
        this.rest = rest;
    }

    @Override
    public T first() {
        return data;
    }

    @Override
    public ImmutableList<T> rest() {
        return rest;
    }

    @Override
    public ImmutableList<T> add(T t) {
        return new List<T>(t, this);
    }

	@Override
    public boolean isEmpty()
    {
        return false;
    }

	@Override
    public ImmutableList<T> reverse()
    {
        return rest.reverse().addEnd(data);
    }

	@Override
    public ImmutableList<T> addEnd(T t)
    {
        return new List<T>(data, rest.addEnd(t));
    }

	@Override
    public String asCSV()
    {
        return ", "+data+rest.asCSV();
    }
	
	@Override
	public String toString()
	{
		return "["+data+rest.asCSV()+"]";
	}
}