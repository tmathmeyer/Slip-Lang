package com.tmathmeyer.ci;

import static com.tmathmeyer.ci.values.ImmutableList.map;

import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.ast.AST.ASNode;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;
import com.tmathmeyer.ci.values.ImmutableList;

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
			Lambda inner = new Lambda(body.desugar(), map(ImmutableList.exprToSymbol(), arglist));
			Lambda outer = new Lambda(inner, name);
			
			Application yca = new Application(Y, outer);
			
			return new DefSans(name, yca.desugar());
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
	        return new DefSans(name, expr.desugar());
        }

		@Override
        public Value interp(MappingPartial<Binding> env)
        {
			throw new RuntimeException("attempting to interp a #def, please desugar first");
        }
	}
	
	public static Def getDefn(ImmutableList<AST> rest)
	{
		Symbol name = new Symbol(((ASNode)rest.first()).value);
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