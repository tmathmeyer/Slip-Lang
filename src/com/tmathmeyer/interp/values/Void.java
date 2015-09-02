package com.tmathmeyer.interp.values;

import com.tmathmeyer.interp.types.Value;

public class Void implements Value
{
    private final Value v;

    private Void(Value v)
    {
        this.v = v;
    }

    public static final Void NOTHING = new Void(null);

    @Override
    public String getPrintString()
    {
        return v+"";
    }

    @Override
    public String getTypeName()
    {
        return "maybe";
    }

    @Override
    public String toString()
    {
    	return getPrintString();
    }

}
