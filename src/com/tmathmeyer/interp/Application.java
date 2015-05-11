package com.tmathmeyer.interp;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Closure;
import com.tmathmeyer.interp.values.ImmutableList;

public class Application implements Expression
{
    public final Expression func;
    public final ImmutableList<Expression> args;

    public Application(Expression function, Expression... arguments)
    {
        func = function;
        ImmutableList<Expression> temp = new EmptyList<Expression>();
        for (Expression e : arguments)
        {
            temp = temp.add(e);
        }
        args = temp;
    }

    public Application(Expression function, ImmutableList<Expression> arguments)
    {
        func = function;
        args = arguments;
    }

    public Application(ImmutableList<AST> list)
    {
        func = list.first().asExpression();
        args = list.rest().map(a -> a.asExpression());
    }

    @Override
    public Expression desugar()
    {
        return new Application(func.desugar(), args.map(a -> a.desugar()));
    }

    @Override
    public Value interp(MappingPartial<Binding> env)
    {
        Closure cloj = (Closure) func.interp(env);

        ImmutableList<Expression> expTemp = args;
        ImmutableList<Symbol> symTemp = cloj.args;

        MappingPartial<Binding> passOn = env;

        while (!expTemp.isEmpty())
        {
        	try
        	{
        		passOn = passOn.add(new Binding(symTemp.first(), expTemp.first().interp(env)));
        	}
        	catch(RuntimeException re)
        	{
        		throw new RuntimeException("failed to add binding in context: ["+this+"] "+cloj.args, re);
        	}
            expTemp = expTemp.rest();
            symTemp = symTemp.rest();
        }

        if (!symTemp.isEmpty())
        {
            throw new RuntimeException("mismatched arg length - " + this);
        }

        return cloj.body.interp(passOn);
    }

    public String toString()
    {
    	return "<>";
    }
}