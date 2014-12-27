package com.tmathmeyer.ci.maths;

import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.values.ImmutableList;

public class BinaryMathExpression
{
	public static Expression fromAST(ImmutableList<AST> list, String type)
	{
		Expression l = list.first().asExpression();
		Expression r = list.rest().first().asExpression();
		
		switch(type)
		{
			case "+":
				return new Plus(l, r);
			case "-":
				return new Minus(l, r);
			case "*":
				return new Mult(l, r);
			case "/":
				return new Divide(l, r);
		}
		
		return null;
	}
}
