package com.tmathmeyer.interp.struct;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.expr.Symbol;
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
	public Value interp(ImmutableList<Binding> env) throws InterpException
	{
		return new Struct(name, env.filter(B -> symbols.contains(B.name)));
	}
}
