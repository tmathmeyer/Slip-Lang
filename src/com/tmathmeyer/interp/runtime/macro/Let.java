package com.tmathmeyer.interp.runtime.macro;

import com.tmathmeyer.interp.runtime.RuntimeMacro;
import com.tmathmeyer.interp.values.ImmutableList;

public class Let extends RuntimeMacro
{
    @Override
    public ImmutableList<String> getSrc()
    {
        return asList("(# (let ((iname iexpr)) evalme) ((lambda (iname) evalme) iexpr))",
                "(# (let ((iname iexpr) (name expr) ...) evalme) ((lambda (iname) (let ((name expr) ...) evalme)) iexpr))");
    }
}
