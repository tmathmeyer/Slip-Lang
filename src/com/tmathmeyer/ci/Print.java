package com.tmathmeyer.ci;

import static com.tmathmeyer.ci.values.ImmutableList.map;

import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;
import com.tmathmeyer.ci.values.ImmutableList;
import com.tmathmeyer.ci.values.Number;

public class Print implements Expression
{
	private final ImmutableList<Expression> toPrint;

	public Print(ImmutableList<Expression> out, int dummy)
	{
		toPrint = out;
	}

	public Print(ImmutableList<AST> rest)
	{
		this(map(AST.toExpression(), rest), 0);
	}

	@Override
	public Expression desugar()
	{
		return new Print(map(ImmutableList.getDesugarer(), toPrint), 0);
	}

	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		ImmutableList<Expression> copy = toPrint;
		int count = 0;
		while (!copy.isEmpty())
		{
			count++;
			System.out.println(copy.first().interp(env));
			copy = copy.rest();
		}
		return new Number(new Real(count));
	}
}