package com.tmathmeyer.ci.values;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.Symbol;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;

public class Closure implements Value
{
	public final MappingPartial<Binding> environment;
	public final Expression body;
	public final ImmutableList<Symbol> args;

	public Closure(MappingPartial<Binding> env, Expression bod, ImmutableList<Symbol> arg)
	{
		environment = env;
		body = bod;
		args = arg;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((args == null) ? 0 : args.hashCode());
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((environment == null) ? 0 : environment.hashCode());
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
		Closure other = (Closure) obj;
		if (args == null)
		{
			if (other.args != null)
				return false;
		} else if (!args.equals(other.args))
			return false;
		if (body == null)
		{
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (environment == null)
		{
			if (other.environment != null)
				return false;
		} else if (!environment.equals(other.environment))
			return false;
		return true;
	}

	public String toString()
	{
		return "#Closure";
	}
}