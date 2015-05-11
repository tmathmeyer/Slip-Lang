package com.tmathmeyer.interp;

import java.util.List;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ast.ASTree;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Bool;
import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.interp.values.Maybe;

public class Cond implements Expression
{
	ImmutableList<CondClause> conditionals;

	public Cond(ImmutableList<AST> rest)
	{
		conditionals = rest.map(a -> from(a));
	}

	public Cond(ImmutableList<CondClause> conditionals, int garbage)
	{
		this.conditionals = conditionals;
	}

	@Override
	public Expression desugar()
	{
		return new Cond(conditionals.map(a -> a.desugar()), 0);
	}

	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		for (CondClause cc : conditionals)
		{
			try
			{
				Value v = cc.interp(env);
				return v;
			} catch (CondFalseException cfe)
			{
				// do nothing?
			}
		}

		return Maybe.NOTHING;
	}

	private final class CondClause
	{
		private final Expression bool;
		private final Expression eval;

		public CondClause(Expression b, Expression e)
		{
			bool = b;
			eval = e;
		}

		public Value interp(MappingPartial<Binding> env) throws CondFalseException
		{
			Value v = bool.interp(env);
			if (v == Bool.TRUE)
			{
				return eval.interp(env);
			}
			throw new CondFalseException();
		}

		public CondClause desugar()
		{
			return new CondClause(bool.desugar(), eval.desugar());
		}
	}

	public CondClause from(AST a)
	{
		ASTree tree = (ASTree) a;
		List<AST> parts = tree.getParts();

		return new CondClause(parts.get(0).asExpression(), parts.get(1).asExpression());
	}

	@SuppressWarnings("serial")
	private class CondFalseException extends Exception
	{
	}
}
