package com.tmathmeyer.interp.values;

import com.tmathmeyer.interp.types.Value;

public enum Bool implements Value
{
    TRUE(), FALSE();

    public Value other()
    {
        if (this == TRUE)
        {
            return FALSE;
        }
        return TRUE;
    }

    @Override
    public String getTypeName()
    {
        return "bool";
    }

    @Override
    public String toString()
    {
        return getPrintString();
    }

    @Override
    public String getPrintString()
    {
        return this == TRUE ? "true" : "false";
    }
}
