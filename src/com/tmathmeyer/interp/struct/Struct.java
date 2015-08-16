package com.tmathmeyer.interp.struct;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.Symbol;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public class Struct implements Value
{
	public final ImmutableList<Binding> values;
	public final Symbol name;

	public Struct(Symbol sname, ImmutableList<Binding> intersect)
	{
		values = intersect;
		name = sname;
	}

	@Override
	public String toString()
	{
		return name + "" + values;
	}

	@Override
    public String getTypeName()
    {
	    return "@"+name;
    }
}
