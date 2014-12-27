package com.tmathmeyer.ci;

import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;
import com.tmathmeyer.ci.values.ImmutableList;

public class Rest implements Expression
{
	final Expression list;

	public Rest(Expression l)
	{
		list = l;
	}

	public Rest(ImmutableList<AST> rest)
	{
		list = rest.first().asExpression();
	}

	@Override
	public Expression desugar()
	{
		return new Rest(list.desugar());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		return ((ImmutableList<Value>) list.interp(env)).rest();
	}
}