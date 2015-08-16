package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.InterpException;
import com.tmathmeyer.interp.types.Expression;

public class NANException extends InterpException
{
	private final Expression expr;
	private final Expression in;
	
	public NANException(Expression l, Expression plus)
    {
	    expr = l;
	    in = plus;
    }
	
	public void printStackTrace()
	{
		System.out.println(expr + " is not a number");
		System.out.println("in "+in);
	}
	
}
