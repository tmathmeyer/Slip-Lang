package com.tmathmeyer.interp.runtime;

import com.tmathmeyer.interp.values.ImmutableList;

public class List extends RuntimeMacro
{
	@Override
    public ImmutableList<String> getSrc()
    {
		return asList(
				"(# (list item) (cons item empty))",
				"(# (list item items ...) (cons item (list items ...)))");
    }
}
