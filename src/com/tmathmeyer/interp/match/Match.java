package com.tmathmeyer.interp.match;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public class Match implements Expression
{
	private final Expression matching;
	private final ImmutableList<Expression> statements;

	public Match(AST first, ImmutableList<AST> rest)
	{
		this(first.asExpression(), rest.map(a -> a.asExpression()));
	}

	public Match(Expression asExpression, ImmutableList<Expression> map)
	{
		matching = asExpression;
		statements = map;
	}

	@Override
	public Expression desugar()
	{
		statements.reverse();

		return null;
	}

	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
