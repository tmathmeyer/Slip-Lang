package com.tmathmeyer.interp;

import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;

public class DefSans implements Expression
{
	public final Symbol name;
	public final Expression body;

	public DefSans(Symbol n, Expression b)
	{
		name = n;
		body = b;
	}

	@Override
	public Expression desugar()
	{
		throw new RuntimeException("can't desugar a DefSans");
	}

	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		return new Binding(name, body.interp(env));
	}

}