package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Number;

public class Plus implements Expression
{
	public final Expression L, R;

	public Plus(Expression left, Expression right)
	{
		L = left;
		R = right;
	}

	@Override
	public Expression desugar()
	{
		return new Plus(L.desugar(), R.desugar());
	}

	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		Number l = (Number) L.interp(env);
		Number r = (Number) R.interp(env);

		return new Number(l.value.add(r.value));
	}

	public String toString()
	{
		return "(" + L + " + " + R + ")";
	}
}