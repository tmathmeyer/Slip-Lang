package com.tmathmeyer.interp.expr;

import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public class FunctionMapping implements Expression
{
    private final Symbol name;
    private final Expression body;

    public FunctionMapping(Symbol n, Expression b)
    {
        name = n;
        body = b;
    }

    @Override
    public Expression desugar()
    {
        throw new RuntimeException("can't desugar a FunctionMapping");
    }

    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        return new Binding(name, body.interp(env));
    }

    public Binding catchInterp(ImmutableList<Binding> env)
    {
        try
        {
            return (Binding) interp(env);
        }
        catch (InterpException e)
        {
            e.printStackTrace();
            return null;
        }
    }

}