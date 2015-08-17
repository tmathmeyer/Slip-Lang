package com.tmathmeyer.interp.types;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.values.ImmutableList;

/**
 * Created by ted on 12/19/14.
 */
public interface Expression
{
    public Expression desugar();

    public Value interp(ImmutableList<Binding> env) throws InterpException;
}
