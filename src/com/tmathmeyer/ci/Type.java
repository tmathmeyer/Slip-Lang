package com.tmathmeyer.ci;

import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;
import com.tmathmeyer.ci.values.ImmutableList;
import com.tmathmeyer.ci.values.Str;

public class Type implements Expression
{
	final Expression of;

	public Type(Expression l)
	{
		of = l;
	}

	public Type(ImmutableList<AST> rest)
	{
		of = rest.first().asExpression();
	}

	@Override
	public Expression desugar()
	{
		return new Type(of.desugar());
	}

	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		return new Str(of.interp(env).getClass().getSimpleName());
	}
}