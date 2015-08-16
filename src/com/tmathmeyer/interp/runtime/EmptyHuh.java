package com.tmathmeyer.interp.runtime;

import com.tmathmeyer.interp.values.ImmutableList;

public class EmptyHuh extends RuntimeMacro
{
	@Override
    public ImmutableList<String> getSrc()
    {
		return asList("(# (empty? x)"
					 +"   (= (type x) \"EmptyList\"))");
    }
}
