package com.tmathmeyer.ci.values;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.Real;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;

public class Number implements Expression, Value
{
    public final Real value;

    public Number(int n)
    {
        value = new Real(n, 1);
    }

    public Number(Real r)
    {
        value = r;
    }

    @Override
    public Expression desugar()
    {
        return this;
    }

    @Override
    public Value interp(MappingPartial<Binding> env)
    {
        return new Number(value);
    }
    
    public String toString()
    {
    	return value.toString();
    }
}