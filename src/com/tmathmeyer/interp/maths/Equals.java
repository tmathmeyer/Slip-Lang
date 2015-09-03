package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.ID;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.expr.Symbol;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Bool;
import com.tmathmeyer.interp.values.ImmutableList;

class Equals implements Expression
{
    private final ImmutableList<Expression> exprs;

    Equals(ImmutableList<Expression> exprs)
    {
        this.exprs = exprs;
    }

    @Override
    public Expression desugar()
    {
        return new Equals(exprs.map(E -> E.desugar()));
    }

    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        Value v = exprs.first().interp(env);

        for (Expression e : exprs.rest())
        {
            if (!e.interp(env).equals(v))
            {
                return Bool.FALSE;
            }
        }

        return Bool.TRUE;
    }

    public String toString()
    {
        return exprs.add(new ID(new Symbol("="))).toString();
    }
}