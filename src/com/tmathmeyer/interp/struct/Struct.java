package com.tmathmeyer.interp.struct;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.Symbol;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.types.Value;

public class Struct implements Value
{
	public final MappingPartial<Binding> values;
	public final Symbol name;
	
	public Struct(Symbol sname, MappingPartial<Binding> intersect)
    {
	    values = intersect;
	    name = sname;
    }
	
	@Override
	public String toString()
	{
		return "#"+name+":"+values;
	}
}
