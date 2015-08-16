package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.interp.values.Number;

public class Mult implements Expression
{
    public final Expression L, R;

    public Mult(Expression left, Expression right)
    {
        L = left;
        R = right;
    }

    @Override
    public Expression desugar()
    {
        return new Mult(L.desugar(), R.desugar());
    }

    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        Number l = (Number) L.interp(env);
        Number r = (Number) R.interp(env);

        return new Number(l.value.multiply(r.value));
    }

    public String toString()
    {
        return "(" + L + " * " + R + ")";
    }
}