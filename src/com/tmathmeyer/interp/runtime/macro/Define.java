package com.tmathmeyer.interp.runtime.macro;

import com.tmathmeyer.interp.runtime.RuntimeMacro;
import com.tmathmeyer.interp.values.ImmutableList;

public class Define extends RuntimeMacro
{

    @Override
    public ImmutableList<String> getSrc()
    {
        return asList("(# (define name args body)"
                + "  (define name ((lambda (le) ((lambda (f) (f f)) (lambda (f) (le (lambda args (eval (attribute ((lambda (x) (cons (sym (f f)) x))) args))))))) (lambda (name) (lambda args body)))))");
    }

}
