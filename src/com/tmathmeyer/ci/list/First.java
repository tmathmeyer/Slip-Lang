package com.tmathmeyer.ci.list;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;
import com.tmathmeyer.ci.values.ImmutableList;

public class First implements Expression
{
	final Expression list;

	public First(Expression l)
	{
		list = l;
	}

	public First(ImmutableList<AST> rest)
	{
		list = rest.first().asExpression();
	}

	@Override
	public Expression desugar()
	{
		return new First(list.desugar());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		return ((ImmutableList<Value>) list.interp(env)).first();
	}
}