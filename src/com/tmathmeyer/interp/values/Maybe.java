package com.tmathmeyer.interp.values;

import com.tmathmeyer.interp.types.Value;

public class Maybe implements Value
{
    private final Value v;

    private Maybe(Value v)
    {
        this.v = v;
    }

    public static final Maybe NOTHING = new Maybe(null);

    public String toString()
    {
        return v.toString();
    }

    @Override
    public String getTypeName()
    {
        return "maybe";
    }

}
