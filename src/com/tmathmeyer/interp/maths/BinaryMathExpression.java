package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.values.ImmutableList;

public class BinaryMathExpression
{
	public static Expression fromAST(ImmutableList<AST> list, String type)
	{
		Expression l = list.first().asExpression();
		Expression r = list.rest().first().asExpression();

		switch (type.charAt(0))
		{
			case '+':
				return new Plus(list.map(A -> A.asExpression()));
			case '-':
				return new Minus(l, r);
			case '*':
				return new Mult(l, r);
			case '/':
				return new Divide(l, r);
			case '>':
				return new GreaterThan(l, r);
			case '<':
				return new LessThan(l, r);
			case '=':
				return new Equals(l, r);
			case '&':
				return new And(list);
			case '!':
				return new Not(l);
		}

		return null;
	}
}
