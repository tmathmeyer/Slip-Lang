package com.tmathmeyer.interp.struct;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.Function;
import com.tmathmeyer.interp.Symbol;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public class StructFactory implements Expression
{
	public final ImmutableList<Symbol> symbols;
	public final Symbol name;

	public StructFactory(StructDefn structDefn)
	{
		symbols = structDefn.args;
		name = structDefn.name;
	}

	@Override
	public Expression desugar()
	{
		throw new RuntimeException("can't desugar a struct creating function");
	}

	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		return new Struct(name, intersect(env, symbols));
	}

	public static MappingPartial<Binding> intersect(MappingPartial<Binding> env, ImmutableList<Symbol> s)
	{
		final ImmutableList<Symbol> copyOfS = s;
		return env.filter(new Function<Binding, Boolean>() {

			@Override
			public Boolean eval(Binding in)
			{
				return copyOfS.contains(in.name);
			}

		});
	}
}
