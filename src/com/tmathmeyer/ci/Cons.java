package com.tmathmeyer.ci;

import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;
import com.tmathmeyer.ci.values.ImmutableList;

public class Cons implements Expression
{
	final Expression app;
	final Expression list;
	
	public Cons(Expression a, Expression l)
	{
		app = a;
		list = l;
	}
	
	public Cons(ImmutableList<AST> rest)
    {
        app = rest.first().asExpression();
        list = rest.rest().first().asExpression();
    }

	@Override
    public Expression desugar()
    {
        return new Cons(app.desugar(), list.desugar());
    }

	@SuppressWarnings("unchecked")
    @Override
    public Value interp(MappingPartial<Binding> env)
    {
        return ((ImmutableList<Value>) list.interp(env)).add(app.interp(env));
    }
	
	public String toString()
	{
		return "(cons " + app + " " + list + ")";
	}
}