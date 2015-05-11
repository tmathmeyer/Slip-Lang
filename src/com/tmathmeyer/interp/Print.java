package com.tmathmeyer.interp;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ds.MappingPartial;
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
	public Value interp(MappingPartial<Binding> env)
	{
		ImmutableList<Expression> copy = toPrint;
		int count = 0;
		while (!copy.isEmpty())
		{
			count++;
			copy = copy.rest();
		}
		return new Number(new Real(count));
	}
}