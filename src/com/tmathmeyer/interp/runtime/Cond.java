package com.tmathmeyer.interp.runtime;

public class Cond extends RuntimeMacro
{
	@Override
    public String getSrc()
    {
		return "(#redef (bmatch (ftest fexp) (test exp) ...)"
				+"        (if ftest fexp"
				+"            (bmatch (test exp) ...)))"
				+"(#redef (bmatch (ftest fexp))"
				+"        (if ftest fexp #void))";
    }
}
