package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.InterpException;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Bool;
import com.tmathmeyer.interp.values.ImmutableList;

public class Equals implements Expression
{
	public final Expression L, R;

	public Equals(Expression left, Expression right)
	{
		L = left;
		R = right;
	}

	@Override
	public Expression desugar()
	{
		return new Equals(L.desugar(), R.desugar());
	}

	@Override
	public Value interp(ImmutableList<Binding> env) throws InterpException
	{
		return L.interp(env).equals(R.interp(env)) ? Bool.TRUE : Bool.FALSE;
	}

	public String toString()
	{
		return "(" + L + " == " + R + ")";
	}
}