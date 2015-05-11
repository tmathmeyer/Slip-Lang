package com.tmathmeyer.interp.ds;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.DefSans;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public class DefSansSet implements Expression
{
	private final Set<DefSans> backing = new HashSet<>();

	public DefSansSet(DefSans... sans)
	{
		for (DefSans ds : sans)
		{
			backing.add(ds);
		}
	}

	public DefSansSet(ImmutableList<DefSans> sans)
	{
		while (!sans.isEmpty())
		{
			backing.add(sans.first());
			sans = sans.rest();
		}
	}

	public void doWithCopy(Consumer<DefSans> consumer)
	{
		for (DefSans ds : backing)
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
	public Value interp(MappingPartial<Binding> env)
	{
		Value last = null;
		for (DefSans ds : backing)
		{
			last = ds.interp(env);
		}
		return last;
	}
}
