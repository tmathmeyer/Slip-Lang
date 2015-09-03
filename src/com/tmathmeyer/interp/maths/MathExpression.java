package com.tmathmeyer.interp.maths;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.values.ImmutableList;

public class MathExpression
{
    public static Expression fromAST(ImmutableList<AST> list, String type)
    {
        ImmutableList<Expression> exprs = list.map(A -> A.asExpression());

        switch (type.charAt(0))
        {
            case '+':
                return new Plus(exprs);
            case '-':
                return new Minus(exprs);
            case '*':
                return new Mult(exprs);
            case '/':
                return new Divide(exprs);
            case '>':
                return new GreaterThan(exprs);
            case '<':
                return new LessThan(exprs);
            case '=':
                return new Equals(exprs);
            case '&':
                return new And(exprs);
            case '!':
                return new Not(exprs.first());
        }

        return null;
    }
}
