package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.interp.values.Number;

class Divide implements Expression
{
    private final Expression from;
    private final Expression by;

    Divide(ImmutableList<Expression> exprs)
    {
        this(exprs.first(), new Mult(exprs.rest()));
    }
    
    private Divide(Expression from, Expression by)
    {
        this.from = from;
        this.by = by;
    }

    @Override
    public Expression desugar()
    {
        return new Divide(from.desugar(), by.desugar());
    }

    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        Number l = (Number) from.interp(env);
        Number r = (Number) by.interp(env);

        return new Number(l.value.divide(r.value));
    }

    public String toString()
    {
        return "(" + from + " - " + by + ")";
    }
}