package com.tmathmeyer.interp.runtime;

import com.tmathmeyer.interp.values.ImmutableList;

class Let extends RuntimeMacro
{
    @Override
    public ImmutableList<String> getSrc()
    {
        return asList("(# (let ((iname iexpr)) evalme) ((lambda (iname) evalme) iexpr))",
                "(# (let ((iname iexpr) (name expr) ...) evalme) ((lambda (iname) (let ((name expr) ...) evalme)) iexpr))");
    }
}
