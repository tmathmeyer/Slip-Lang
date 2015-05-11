package com.tmathmeyer.interp.struct;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.Symbol;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;

public class StructInspector implements Expression
{
	public static Symbol name;

	public StructInspector(Symbol in)
    {
	    name = in;
    }

	@Override
	public Expression desugar()
	{
		throw new RuntimeException("cant desugar the struct inspector");
	}

	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		Struct s = (Struct) env.findPartial(new Binding(new Symbol("struct"), null)).val;
		
		return s.values.findPartial(new Binding(name, null)).val;
	}

}
