package com.tmathmeyer.ci.values;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.Real;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;

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
	public Value interp(MappingPartial<Binding> env)
	{
		return new Number(value);
	}

	public String toString()
	{
		return value.toString();
	}
}