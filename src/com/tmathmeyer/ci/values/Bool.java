package com.tmathmeyer.ci.values;

import com.tmathmeyer.ci.types.Value;

public enum Bool implements Value
{
	TRUE("true"), FALSE("false");

	private final String type;

	private Bool(String name)
	{
		type = name;
	}

	public String toString()
	{
		return type;
	}
}
