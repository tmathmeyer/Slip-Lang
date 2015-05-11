package com.tmathmeyer.interp.list;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;

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