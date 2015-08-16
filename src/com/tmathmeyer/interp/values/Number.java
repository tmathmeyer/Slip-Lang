package com.tmathmeyer.interp.values;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.expr.Real;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;

public class Number implements Expression, Value
{
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Number other = (Number) obj;
		if (value == null)
		{
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public final Real value;

	public Number(int n)
	{
		value = new Real(n, 1);
	}

	public Number(Real r)
	{
		value = r;
	}

	@Override
	public Expression desugar()
	{
		return this;
	}

	@Override
	public Value interp(ImmutableList<Binding> env) throws InterpException
	{
		return new Number(value);
	}

	public String toString()
	{
		return value.toString();
	}

	@Override
    public String getTypeName()
    {
	    return "number";
    }
}