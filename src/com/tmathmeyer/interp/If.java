package com.tmathmeyer.interp;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Bool;
import com.tmathmeyer.interp.values.ImmutableList;

public class If implements Expression
{
	public final Expression conditional, left, right;

	public If(Expression c, Expression l, Expression r)
	{
		conditional = c;
		left = l;
		right = r;
	}

	public If(ImmutableList<AST> rest)
	{
		conditional = rest.first().asExpression();
		left = rest.rest().first().asExpression();
		right = rest.rest().rest().first().asExpression();
	}

	@Override
	public Expression desugar()
	{
		return new If(conditional.desugar(), left.desugar(), right.desugar());
	}

	@Override
	public Value interp(ImmutableList<Binding> env) throws InterpException
	{
		if (conditional.interp(env) == Bool.TRUE)
		{
			return left.interp(env);
		}
		return right.interp(env);
	}

	public String toString()
	{
		return "(if " + conditional + " then>>" + left + "  else>>" + right + ")";
	}
}