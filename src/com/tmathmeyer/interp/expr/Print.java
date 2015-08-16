package com.tmathmeyer.interp.expr;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.interp.values.Number;

public class Print implements Expression
{
	private final ImmutableList<Expression> toPrint;

	public Print(ImmutableList<Expression> out, int dummy)
	{
		toPrint = out;
	}

	public Print(ImmutableList<AST> rest)
	{
		this(rest.map(a -> a.asExpression()), 0);
	}

	@Override
	public Expression desugar()
	{
		return new Print(toPrint.map(a -> a.desugar()), 0);
	}

	@Override
	public Value interp(ImmutableList<Binding> env) throws InterpException
	{
		for(Expression e : toPrint)
		{
			System.out.println(e.interp(env));
		}
		return new Number(new Real(toPrint.size()));
	}
}