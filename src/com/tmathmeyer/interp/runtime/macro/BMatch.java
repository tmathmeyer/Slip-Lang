package com.tmathmeyer.interp.runtime.macro;

import com.tmathmeyer.interp.runtime.RuntimeMacro;
import com.tmathmeyer.interp.values.ImmutableList;

public class BMatch extends RuntimeMacro
{
    @Override
    public ImmutableList<String> getSrc()
    {
        return asList("(# (cond (bool expr)) (if bool expr #void))",
                "(# (cond (bool expr) (bools exprs) ...) (if bool expr (cond (bools exprs) ...)))");
    }
}
