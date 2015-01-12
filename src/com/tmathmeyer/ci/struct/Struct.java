package com.tmathmeyer.ci.struct;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.Symbol;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Value;

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
