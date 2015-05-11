package com.tmathmeyer.interp.runtime;

public class EmptyHuh extends RuntimeMacro
{
	@Override
    public String getSrc()
    {
		return "(#redef (empty? x)"
			  +"   (= (type x) \"EmptyList\"))";
    }
}
