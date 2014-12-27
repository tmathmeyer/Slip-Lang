package com.tmathmeyer.ci;

import com.tmathmeyer.ci.ds.Partial;
import com.tmathmeyer.ci.types.Value;

/**
 * Created by ted on 12/20/14.
 */
public class Binding implements Partial<Binding>, Value
{
	public final Symbol name;
	public final Value val;

	public Binding(Symbol s, Value v)
	{
		val = v;
		name = s;
	}

	@Override
	public int compareTo(Binding binding)
	{
		return name.compareTo(binding.name);
	}

	public String toString()
	{
		return name + " <--> " + val;
	}
}
