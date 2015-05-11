package com.tmathmeyer.interp;

import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;

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
		if (b == null)
		{
			env.findPartial(new Binding(I, null));
			System.out.println(env);
			throw new RuntimeException("failed to lookup: " + I);
		}
		return b.val;
	}

	public String toString()
	{
		return I.toString();
	}
}