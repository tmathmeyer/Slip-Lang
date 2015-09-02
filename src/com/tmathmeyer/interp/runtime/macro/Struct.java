package com.tmathmeyer.interp.runtime.macro;

import com.tmathmeyer.interp.runtime.RuntimeMacro;
import com.tmathmeyer.interp.values.ImmutableList;

public class Struct extends RuntimeMacro
{
    @Override
    public ImmutableList<String> getSrc()
    {
        return asList("(# (struct name (args ...)) (lambda (args ...) (list (sym name) (list (list (sym args) args) ...))))");
    
    
    }
}
