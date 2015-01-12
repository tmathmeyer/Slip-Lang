package com.tmathmeyer.ci.ds;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.DefSans;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;
import com.tmathmeyer.ci.values.ImmutableList;

public class DefSansSet implements Expression
{
	private final Set<DefSans> backing = new HashSet<>();
	
	public DefSansSet(DefSans ... sans)
	{
		for(DefSans ds : sans)
		{
			backing.add(ds);
		}
	}
	
	public DefSansSet(ImmutableList<DefSans> sans)
    {
	    while(!sans.isEmpty())
	    {
	    	backing.add(sans.first());
	    	sans = sans.rest();
	    }
    }

	public void doWithCopy(Consumer<DefSans> consumer)
	{
		for(DefSans ds : backing)
		{
			consumer.accept(ds);
		}
	}

	@Override
    public Expression desugar()
    {
	    throw new RuntimeException("can't desugar a DefSansSet");
    }

	@Override
    public Value interp(MappingPartial<Binding> env)
    {
		Value[] last = new Value[1];
	    doWithCopy(new Consumer<DefSans>(){
			@Override
            public void accept(DefSans arg0)
            {
	            last[0] = arg0.interp(env);
            }
	    });
	    return last[0];
    }
}
