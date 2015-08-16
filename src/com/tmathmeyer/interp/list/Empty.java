package com.tmathmeyer.interp.list;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.InterpException;
import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public class Empty implements Expression
{
	@Override
	public Expression desugar()
	{
		return this;
	}

	@Override
	public Value interp(ImmutableList<Binding> env) throws InterpException
	{
		return new EmptyList<>();
	}

	public String toString()
	{
		return "empty";
	}
}