package com.tmathmeyer.interp.ds;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.FunctionMapping;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public class FunctionMappingCollection implements Expression
{
	private final Set<FunctionMapping> backing = new HashSet<>();
	private final ImmutableList<FunctionMapping> functions;

	public FunctionMappingCollection(ImmutableList<FunctionMapping> sans)
	{
		functions = sans;
		while (!sans.isEmpty())
		{
			backing.add(sans.first());
			sans = sans.rest();
		}
	}

	public ImmutableList<FunctionMapping> getFunctions()
	{
		return functions;
	}
	
	public void doWithCopy(Consumer<FunctionMapping> consumer)
	{
		for (FunctionMapping ds : backing)
		{
			consumer.accept(ds);
		}
	}

	@Override
	public Expression desugar()
	{
		throw new RuntimeException("can't desugar a DefSansSet");
	}

	@Override
	public Value interp(ImmutableList<Binding> env) throws InterpException
	{
		Value last = null;
		for (FunctionMapping ds : backing)
		{
			last = ds.interp(env);
		}
		return last;
	}
}
