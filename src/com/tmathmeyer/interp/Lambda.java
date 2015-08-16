package com.tmathmeyer.interp;

import com.tmathmeyer.interp.ast.ASNode;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Closure;
import com.tmathmeyer.interp.values.ImmutableList;

public class Lambda implements Expression
{
	public final Expression body;
	public final ImmutableList<Symbol> args;

	public Lambda(Expression bod, Symbol... arguments)
	{
		body = bod;
		ImmutableList<Symbol> temp = new EmptyList<Symbol>();
		for (Symbol e : arguments)
		{
			temp = temp.add(e);
		}
		args = temp;
	}

	public Lambda(Expression function, ImmutableList<Symbol> arguments)
	{
		body = function;
		args = arguments;
	}

	@Override
	public Expression desugar()
	{
		return new Lambda(body.desugar(), args);
	}

	@Override
	public Value interp(ImmutableList<Binding> env) throws InterpException
	{
		return new Closure(env, body, args);
	}

	public static ImmutableList<Symbol> getArgs(AST first)
	{
		if (first instanceof ASNode)
		{
			throw new RuntimeException("these arent args!!");
		}

		return ImmutableList.fromSTD(first.getParts()).map(in -> new Symbol(((ASNode) in).value));
	}

	public String toString()
	{
		return "<>";
	}
}