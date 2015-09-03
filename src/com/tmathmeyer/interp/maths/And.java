package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Bool;
import com.tmathmeyer.interp.values.ImmutableList;

class And implements Expression
{
    private final ImmutableList<Expression> exprs;

    And(ImmutableList<Expression> parts)
    {
        exprs = parts;
    }

    @Override
    public Expression desugar()
    {
        return new And(exprs.map(a -> a.desugar()));
    }

    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        for (Expression e : exprs)
        {
            Value v = e.interp(env);
            if (v == Bool.FALSE)
            {
                return Bool.TRUE;
            }
            if (!(v instanceof Bool))
            {
                throw new RuntimeException("cannot do boolean arithmetic on " + v.toString());
            }
        }
        return Bool.TRUE;
    }

    public String toString()
    {
        return "and:" + exprs;
    }
}