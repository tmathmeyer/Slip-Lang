package com.tmathmeyer.interp.values;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

class List<T> extends ImmutableList<T>
{
    private final T data;
    private final ImmutableList<T> rest;
    private final int size;

    List(T data, ImmutableList<T> rest)
    {
        this.data = data;
        this.rest = rest;
        size = 1 + rest.size();
    }

    @Override
    public T first()
    {
        return data;
    }

    @Override
    public ImmutableList<T> rest()
    {
        return rest;
    }

    @Override
    public ImmutableList<T> add(T t)
    {
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
        return " " + data + rest.asCSV();
    }

    @Override
    public String toString()
    {
        return "(" + data + rest.asCSV() + ")";
    }

    @Override
    public String getPrintString()
    {
        return "'" + this;
    }

    @Override
    public boolean contains(T name)
    {
        return name.equals(data) || rest.contains(name);
    }

    @Override
    public <R> ImmutableList<R> map(Function<? super T, ? extends R> fn)
    {
        return new List<R>(fn.apply(data), rest.map(fn));
    }

    @Override
    public ImmutableList<T> filter(Function<? super T, Boolean> fn)
    {
        if (fn.apply(data))
        {
            return new List<T>(data, rest.filter(fn));
        }
        return rest.filter(fn);
    }

    @Override
    public void forEach(Consumer<? super T> fn)
    {
        fn.accept(data);
        rest.forEach(fn);
    }

    @Override
    public Iterator<T> iterator()
    {
        ImmutableList<T> cont = this;
        return new Iterator<T>() {

            ImmutableList<T> container = cont;

            @Override
            public boolean hasNext()
            {
                return !container.isEmpty();
            }

            @Override
            public T next()
            {
                T res = container.first();
                container = container.rest();
                return res;
            }
        };
    }

    @Override
    public ImmutableList<T> append(ImmutableList<T> result)
    {
        return new List<T>(data, rest.append(result));
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public String getTypeName()
    {
        return "list";
    }
}