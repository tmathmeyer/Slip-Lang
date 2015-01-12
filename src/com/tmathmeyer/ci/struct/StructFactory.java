package com.tmathmeyer.ci.struct;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.Function;
import com.tmathmeyer.ci.Symbol;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;
import com.tmathmeyer.ci.values.ImmutableList;

public class StructFactory implements Expression
{
	public final ImmutableList<Symbol> symbols;
	public final Symbol name;
	
	public StructFactory(StructDefn structDefn)
    {
	    symbols = structDefn.args;
	    name = structDefn.name;
    }

	@Override
    public Expression desugar()
    {
	    throw new RuntimeException("can't desugar a struct creating function");
    }

	@Override
    public Value interp(MappingPartial<Binding> env)
    {
	    return new Struct(name, intersect(env, symbols));
    }
	
	public static MappingPartial<Binding> intersect(MappingPartial<Binding> env, ImmutableList<Symbol> s)
	{
		return env.filter(new Function<Binding, Boolean>() {

			@Override
            public Boolean eval(Binding in)
            {
	            return s.contains(in.name);
            }
			
		});
	}
}
