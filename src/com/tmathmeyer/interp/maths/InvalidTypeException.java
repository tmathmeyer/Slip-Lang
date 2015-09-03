package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.types.Expression;

public class InvalidTypeException extends InterpException
{
    private static final long serialVersionUID = 1L;
    private final Expression expr;
    private final Expression in;

    public InvalidTypeException(Expression l, Expression plus)
    {
        expr = l;
        in = plus;
    }

    public void printStackTrace()
    {
        System.out.println(expr + " is not a number");
        System.out.println("in " + in);
    }

}
