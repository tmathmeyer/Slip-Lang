package com.tmathmeyer.interp;

import com.tmathmeyer.interp.ds.Partial;
import com.tmathmeyer.interp.types.Value;

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
		if (name == null)
		{
			throw new RuntimeException("binding with no name: value of -> " + val);
		}
		return name.compareTo(binding.name);
	}

	public String toString()
	{
		return name + ":" + val;
	}

	@Override
    public String getTypeName()
    {
	    return "binding";
    }
}
