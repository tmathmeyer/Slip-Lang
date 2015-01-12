package com.tmathmeyer.ci.list;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.ds.EmptyList;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;

public class Empty implements Expression
{
	@Override
	public Expression desugar()
	{
		return this;
	}

	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		return new EmptyList<>();
	}

	public String toString()
	{
		return "empty";
	}
}