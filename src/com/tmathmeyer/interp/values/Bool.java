package com.tmathmeyer.interp.values;

import com.tmathmeyer.interp.types.Value;

public enum Bool implements Value
{
	TRUE(), FALSE();

	public String toString()
	{
		return this == TRUE ? "true" : "false";
	}

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
}
