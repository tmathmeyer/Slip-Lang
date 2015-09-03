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
        if (s == null)
        {
            System.out.println("uh oh...");
        }
        val = v;
        name = s;
    }

    @Override
    public String getPrintString()
    {
        return toString();
    }

    @Override
    public String getTypeName()
    {
        return "binding";
    }

    @Override
    public String toString()
    {
        return name + "->" + val;
    }
}
