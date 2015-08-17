package com.tmathmeyer.interp.expr;

import com.tmathmeyer.interp.types.Value;

/**
 * Created by ted on 12/20/14.
 */
public class Binding implements Value
{
    public final Symbol name;
    public final Value val;

    public Binding(Symbol s, Value v)
    {
        val = v;
        name = s;
    }

    public String toString()
    {
        return name + ":" + val;
    }

    @Override
    public String getTypeName()
    {
        return "binding";
    }
}
