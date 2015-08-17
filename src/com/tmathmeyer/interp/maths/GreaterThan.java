package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Bool;
import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.interp.values.Number;

class GreaterThan implements Expression
{
    private final ImmutableList<Expression> exprs;

    GreaterThan(ImmutableList<Expression> exprs)
    {
        this.exprs = exprs;
    }

    @Override
    public Expression desugar()
    {
        return new GreaterThan(exprs.map(E -> E.desugar()));
    }

    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        try
        {
            Number n = (Number) exprs.first().interp(env);
            
            for(Expression e : exprs.rest())
            {
                Number v = (Number) e.interp(env);
                if (n.value.greaterThan(v.value) != Bool.TRUE)
                {
                    return Bool.FALSE;
                }
                n = v;
            }
            
            return Bool.TRUE;
        }
        catch (ClassCastException cce)
        {
            throw cce;
        }
    }
}