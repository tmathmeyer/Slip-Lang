package com.tmathmeyer.ci;

import static com.tmathmeyer.ci.values.ImmutableList.foldl;

import com.tmathmeyer.ci.Function.Pair;
import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.ast.AST.ASNode;
import com.tmathmeyer.ci.ast.AST.ASTree;
import com.tmathmeyer.ci.ds.EmptyList;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;
import com.tmathmeyer.ci.values.Closure;
import com.tmathmeyer.ci.values.ImmutableList;

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
	public Value interp(MappingPartial<Binding> env)
	{
		return new Closure(env, body, args);
	}

	public static ImmutableList<Symbol> getArgs(AST first)
	{
		if (first instanceof ASNode)
		{
			throw new RuntimeException("these arent args!!");
		}

		ASTree tree = (ASTree) first;

		return ImmutableList.map(AST.toSymbol(), ImmutableList.fromSTD(tree.parts));
	}

	public String toString()
	{
		return "(lambda (" + args.first() + foldl(new Function<Pair<Symbol, String>, String>() {
			@Override
			public String eval(Pair<Symbol, String> in)
			{
				return in.b + " " + in.a;
			}
		}, args.rest(), "") + ") " + body + ")";
	}
}