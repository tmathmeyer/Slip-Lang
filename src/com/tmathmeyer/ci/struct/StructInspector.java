package com.tmathmeyer.ci.struct;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.Symbol;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;

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
