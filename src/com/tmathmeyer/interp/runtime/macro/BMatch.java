package com.tmathmeyer.interp.runtime.macro;

import com.tmathmeyer.interp.runtime.RuntimeMacro;
import com.tmathmeyer.interp.values.ImmutableList;

public class BMatch extends RuntimeMacro
{
    @Override
    public ImmutableList<String> getSrc()
    {
        return asList("(# (bmatch (ftest fexp) (test exp) ...)" + "        (if ftest fexp"
                + "            (bmatch (test exp) ...)))", "(# (bmatch (ftest fexp))"
                + "        (if ftest fexp #void))");
    }
}
