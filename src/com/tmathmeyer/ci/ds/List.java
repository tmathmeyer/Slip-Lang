package com.tmathmeyer.ci.ds;

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

	@Override
    public int hashCode()
    {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((data == null) ? 0 : data.hashCode());
	    result = prime * result + ((rest == null) ? 0 : rest.hashCode());
	    return result;
    }

	@Override
    public boolean equals(Object obj)
    {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    List<?> other = (List<?>) obj;
	    if (data == null)
	    {
		    if (other.data != null)
			    return false;
	    } else if (!data.equals(other.data))
		    return false;
	    if (rest == null)
	    {
		    if (other.rest != null)
			    return false;
	    } else if (!rest.equals(other.rest))
		    return false;
	    return true;
    }
}