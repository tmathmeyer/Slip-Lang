package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Bool;
import com.tmathmeyer.interp.values.ImmutableList;

public class Not implements Expression
{
    private final Expression invert;

    public Not(Expression l)
    {
        invert = l;
    }

    @Override
    public Expression desugar()
    {
        return new Not(invert.desugar());
    }

    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        Value res = invert.interp(env);
        Bool val = (Bool) res;
        return val.other();
    }

}
