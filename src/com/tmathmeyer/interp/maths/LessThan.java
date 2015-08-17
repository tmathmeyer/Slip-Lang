package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.values.ImmutableList;

class LessThan extends GreaterThan
{
    LessThan(ImmutableList<Expression> exprs)
    {
        super(exprs.reverse());
    }
}