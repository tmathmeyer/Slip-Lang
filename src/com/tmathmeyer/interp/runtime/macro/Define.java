package com.tmathmeyer.interp.runtime.macro;

import com.tmathmeyer.interp.runtime.RuntimeMacro;
import com.tmathmeyer.interp.values.ImmutableList;

public class Define extends RuntimeMacro
{

    @Override
    public ImmutableList<String> getSrc()
    {
        return asList("(# (#def name args body)"
                + "  (#def name ((lambda (le) ((lambda (f) (f f)) (lambda (f) (le (lambda (x) ((f f) x)))))) (lambda (name) (lambda args body)))))");
    }

}
