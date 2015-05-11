package com.tmathmeyer.interp;

import com.tmathmeyer.interp.ast.ASNode;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ds.DefSansSet;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public abstract class Def implements Expression
{
	public static class FunctionDefinition extends Def
	{
		final Symbol name;
		final Application args;
		final Expression body;

		public FunctionDefinition(Symbol name2, Application args2, Expression body2)
		{
			name = name2;
			args = args2;
			body = body2;
		}

		@Override
		public Expression desugar()
		{
			ImmutableList<Expression> arglist = args.args;
			arglist = arglist.add(args.func);
			Lambda inner = new Lambda(body.desugar(), arglist.map(a -> ((ID)a).I));
			Lambda outer = new Lambda(inner, name);

			Application yca = new Application(Y, outer);
			return new DefSansSet(new DefSans(name, yca.desugar()));
		}

		@Override
		public Value interp(MappingPartial<Binding> env)
		{
			throw new RuntimeException("attempting to interp a #def, please desugar first");
		}
	}

	public static class ValueDefinition extends Def
	{
		public final Symbol name;
		public final Expression expr;

		public ValueDefinition(Symbol name, Expression asExpression)
		{
			this.name = name;
			this.expr = asExpression;
		}

		@Override
		public Expression desugar()
		{
			return new DefSansSet(new DefSans(name, expr.desugar()));
		}

		@Override
		public Value interp(MappingPartial<Binding> env)
		{
			throw new RuntimeException("attempting to interp a #def, please desugar first");
		}
	}

	public static Def getDefn(ImmutableList<AST> rest)
	{
		Symbol name = new Symbol(((ASNode) rest.first()).value);
		Expression expr = rest.rest().first().asExpression();

		if (expr instanceof Application)
		{
			Application args = (Application) expr;
			Expression body = rest.rest().rest().first().asExpression();
			return new FunctionDefinition(name, args, body);
		}

		return new ValueDefinition(name, rest.rest().first().asExpression());
	}

}