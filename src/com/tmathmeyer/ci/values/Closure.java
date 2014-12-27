package com.tmathmeyer.ci.values;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.Symbol;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;

public class Closure implements Value
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
    
    public String toString()
    {
    	return "#Closure : "+body;
    }
}