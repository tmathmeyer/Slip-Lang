package com.tmathmeyer.interp;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public class Let implements Expression
{
	public final Expression body;
	public final ImmutableList<Defn> bind;

	public Let(Expression bod, Defn... args)
	{
		body = bod;
		ImmutableList<Defn> temp = new EmptyList<Defn>();
		for (Defn e : args)
		{
			temp = temp.add(e);
		}
		bind = temp;
	}

	public Let(ImmutableList<AST> rest)
	{
		body = rest.rest().first().asExpression();
		bind = ImmutableList.fromSTD(rest.first().getParts()).map(a -> new Defn(a));
	}

	public Let(Expression demacro, ImmutableList<Defn> bind2)
	{
		body = demacro;
		bind = bind2;
	}

	@Override
	public Expression desugar()
	{
		return new Application(new Lambda(body.desugar(), bind.map(a -> (Symbol) a.S)), bind.map(a -> a.E.desugar()));
	}

	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		throw new RuntimeException("you can't interp a let...");
	}

	public String toString()
	{
		return "<>";
	}
}