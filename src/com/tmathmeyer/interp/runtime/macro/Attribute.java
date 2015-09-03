package com.tmathmeyer.interp.runtime.macro;

import com.tmathmeyer.interp.runtime.RuntimeMacro;
import com.tmathmeyer.interp.values.ImmutableList;

public class Attribute extends RuntimeMacro
{
    @Override
    public ImmutableList<String> getSrc()
    {
        return asList("(# (attribute (fname) function) (fname (sym function)))",
                "(# (attribute (fname fns ...) function) (fname (attribute (fns ...) function)))");

    }
}
