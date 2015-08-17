package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.interp.values.Number;

class Mult implements Expression
{
    private final ImmutableList<Expression> exprs;

    Mult(ImmutableList<Expression> exprs)
    {
        this.exprs = exprs;
        if (exprs.size() == 0)
        {
            throw new RuntimeException("no arguments to +");
        }
    }

    @Override
    public Expression desugar()
    {
        return new Mult(exprs.map(E -> E.desugar()));
    }

    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        Number result = new Number(0);

        for (Expression e : exprs)
        {
            try
            {
                Number n = (Number) e.interp(env);
                result = new Number(result.value.multiply(n.value));
            }
            catch (ClassCastException cce)
            {
                throw new InvalidTypeException(e, this);
            }
        }

        return result;
    }

    public String toString()
    {
        String result = "(*";
        for (Expression e : exprs)
        {
            result += (" " + e);
        }
        return result + ")";
    }
}