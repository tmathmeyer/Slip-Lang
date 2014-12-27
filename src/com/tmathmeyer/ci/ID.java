package com.tmathmeyer.ci;

import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;

public class ID implements Expression
{
    public final Symbol I;

    public ID(Symbol s)
    {
        I = s;
    }

    @Override
    public Expression desugar()
    {
        return this;
    }
    
    @Override
    public Value interp(MappingPartial<Binding> env)
    {
    	Binding b = env.findPartial(new Binding(I, null));
    	if (b == null) {
    		env.findPartial(new Binding(I, null));
    		System.out.println(env);
    		throw new RuntimeException("failed to lookup: "+ I);
    	}
        return b.val;
    }
    
    public String toString()
    {
    	return I.toString();
    }
}