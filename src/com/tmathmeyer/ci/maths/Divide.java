package com.tmathmeyer.ci.maths;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;
import com.tmathmeyer.ci.values.Number;

public class Divide implements Expression
{
	public final Expression L, R;

	public Divide(Expression left, Expression right)
	{
		L = left;
		R = right;
	}

	@Override
	public Expression desugar()
	{
		return new Divide(L.desugar(), R.desugar());
	}

	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		Number l = (Number) L.interp(env);
		Number r = (Number) R.interp(env);

		return new Number(l.value.divide(r.value));
	}

	public String toString()
	{
		return "(" + L + " / " + R + ")";
	}
}