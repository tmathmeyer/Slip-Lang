package com.tmathmeyer.interp.runtime.macro;

import com.tmathmeyer.interp.runtime.RuntimeMacro;
import com.tmathmeyer.interp.values.ImmutableList;

public class EmptyHuh extends RuntimeMacro
{
    @Override
    public ImmutableList<String> getSrc()
    {
        return asList("(# (empty? x)" + "   (= (type x) \"EmptyList\"))");
    }
}
