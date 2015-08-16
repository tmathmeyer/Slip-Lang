package com.tmathmeyer.interp.list;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

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
	public Value interp(ImmutableList<Binding> env) throws InterpException
	{
		return ((ImmutableList<Value>) list.interp(env)).first();
	}
}