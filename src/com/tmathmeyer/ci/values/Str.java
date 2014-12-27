package com.tmathmeyer.ci.values;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;

public class Str implements Expression, Value
{
	public final String value;

	public Str(CharSequence charSequence)
	{
		value = charSequence.toString();
	}

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
		Str other = (Str) obj;
		if (value == null)
		{
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public Expression desugar()
	{
		return this;
	}

	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		return new Str(value);
	}

	public String toString()
	{
		return value;
	}
}