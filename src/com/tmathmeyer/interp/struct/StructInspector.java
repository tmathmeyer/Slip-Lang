package com.tmathmeyer.interp.struct;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.InterpException;
import com.tmathmeyer.interp.Symbol;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public class StructInspector implements Expression
{
	public static Symbol name;
	public static final Symbol STRUCT_SYM = new Symbol("struct");
	
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
	public Value interp(ImmutableList<Binding> env) throws InterpException
	{
		Binding b = env.filter(B -> B.name.equals(STRUCT_SYM)).first();
		if (b != null)
		{
			return ((Struct)b.val).values.filter(B -> B.name.equals(name)).first();
		}
		throw new NullPointerException();
		
	}

}
