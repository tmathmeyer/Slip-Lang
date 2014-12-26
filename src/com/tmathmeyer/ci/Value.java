package com.tmathmeyer.ci;

import com.tmathmeyer.ci.ds.MappingPartial;

/**
 * Created by ted on 12/20/14.
 */
public interface Value
{
    public static class Number implements Value
    {
        public final Real V;

        public Number(Real r)
        {
            V = r;
        }
        
        public String toString()
        {
        	return V.toString();
        }
        
        public boolean equals(Number n)
        {
        	return V.equals(n.V);
        }
        
        public boolean equals(Object other)
        {
        	if (other == null)
        	{
        		return false;
        	}
        	if (other instanceof Number)
        	{
        		return equals((Number)other);
        	}
        	return false;
        }
    }

    public static class Closure implements Value
    {
        public final MappingPartial<Binding> environment;
        public final Expression body;
        public final ImmutableList<Symbol> args;

        public Closure(MappingPartial<Binding> env, Expression bod, ImmutableList<Symbol> arg)
        {
            environment = env;
            body = bod;
            args = arg;
        }
    }
}
