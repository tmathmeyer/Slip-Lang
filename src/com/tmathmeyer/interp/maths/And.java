package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Bool;
import com.tmathmeyer.interp.values.ImmutableList;

public class And implements Expression
{
	public final ImmutableList<Expression> exprs;

	public And(ImmutableList<AST> ast)
	{
		this(ast.map(a -> a.asExpression()), 0);
	}

	private And(ImmutableList<Expression> parts, int i)
	{
		exprs = parts;
	}

	@Override
	public Expression desugar()
	{
		return new And(exprs.map(a -> a.desugar()), 0);
	}

	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		ImmutableList<Expression> exprs = this.exprs;
		while (!exprs.isEmpty())
		{
			Value v = exprs.first().interp(env);
			if (v == Bool.FALSE)
			{
				return Bool.FALSE;
			}
			if (v == Bool.TRUE)
			{
				exprs = exprs.rest();
				continue;
			}
			throw new RuntimeException("cannot do boolean arithmetic on " + v.toString());
		}
		return Bool.TRUE;
	}

	public String toString()
	{
		return "and:" + exprs;
	}
}