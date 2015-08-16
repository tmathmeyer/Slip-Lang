package com.tmathmeyer.interp.list;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public class Cons implements Expression
{
	final Expression app;
	final Expression list;

	public Cons(Expression a, Expression l)
	{
		app = a;
		list = l;
	}

	public Cons(ImmutableList<AST> rest)
	{
		app = rest.first().asExpression();
		list = rest.rest().first().asExpression();
	}

	@Override
	public Expression desugar()
	{
		return new Cons(app.desugar(), list.desugar());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Value interp(ImmutableList<Binding> env) throws InterpException
	{
		return ((ImmutableList<Value>) list.interp(env)).add(app.interp(env));
	}

	public String toString()
	{
		return "(cons " + app + " " + list + ")";
	}
}