package com.tmathmeyer.ci;

import com.tmathmeyer.ci.Function.Pair;

/**
 * Created by ted on 12/19/14.
 */
public abstract class ImmutableList<T> implements Value
{
    public abstract T first();
    public abstract ImmutableList<T> rest();
    public abstract ImmutableList<T> add(T t);
    public abstract ImmutableList<T> reverse();
    public abstract ImmutableList<T> addEnd(T t);
    public abstract boolean isEmpty();
    public abstract String asCSV();

    public static class List<T> extends ImmutableList<T>
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

    public static class EmptyList<T> extends ImmutableList<T>
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
    
    
    
    
    
    
    
    
    public static <T> ImmutableList<T> fromSTD(java.util.List<T> list)
    {
    	ImmutableList<T> result = new EmptyList<>();
    	for(int i = list.size(); i > 0; i--)
    	{
    		result = result.add(list.get(i-1));
    	}
    	return result;
    }

    public static <I, O> ImmutableList<O> map(Function<I, O> func, ImmutableList<I> in) {
        if (in.isEmpty())
        {
            return new EmptyList<O>();
        }
        return map(func, in.rest()).add(func.eval(in.first()));
    }
    
    public static <I> ImmutableList<I> filter(Function<I, Boolean> func, ImmutableList<I> in) {
        if (in.isEmpty())
        {
            return new EmptyList<I>();
        }
        I i = in.first();
        if (func.eval(i))
        {
        	return filter(func, in.rest()).add(i);
        }
        return filter(func, in.rest());
    }
    
    public static <I, O> O foldl(Function<Pair<I, O>, O> func, ImmutableList<I> in, O start)
    {
    	if (in.isEmpty())
    	{
    		return start;
    	}
    	return foldl(func, in.rest(), func.eval(new Function.Pair<>(in.first(), start)));
    }
	
}
