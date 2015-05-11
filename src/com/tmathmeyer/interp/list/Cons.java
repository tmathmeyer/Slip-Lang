package com.tmathmeyer.interp.list;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ds.MappingPartial;
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
	public Value interp(MappingPartial<Binding> env)
	{
		return ((ImmutableList<Value>) list.interp(env)).add(app.interp(env));
	}

	public String toString()
	{
		return "(cons " + app + " " + list + ")";
	}

	public static Expression fromList(ImmutableList<AST> list)
	{
		if (list.isEmpty())
		{
			return new Empty();
		}
		return new Cons(list.first().asExpression(), Cons.fromList(list.rest()));
	}
}