package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;
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
	public Value interp(ImmutableList<Binding> env) throws InterpException
	{
		Number l;
		Number r;
		
		try
		{
			l = (Number) L.interp(env);
		}
		catch (ClassCastException cce)
		{
			throw new NANException(L, this);
		}
		
		try
		{
			r = (Number) R.interp(env);
		}
		catch (ClassCastException cce)
		{
			throw new NANException(L, this);
		}

		return new Number(l.value.add(r.value));
	}

	public String toString()
	{
		return "(" + L + " + " + R + ")";
	}
}