package com.tmathmeyer.interp.types;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.values.EmptyList;
import com.tmathmeyer.interp.values.ImmutableList;

/**
 * Created by ted on 12/19/14.
 */
public interface Expression
{
    public Expression desugar();

    public Value interp(ImmutableList<Binding> env) throws InterpException;

    public static Value run(AST tree) throws InterpException
    {
        Expression plain = tree.asExpression();
        Expression sansGlucose = plain.desugar();
        return sansGlucose.interp(new EmptyList<>());
    }

    public static Expression fromAST(AST syntax)
    {
        Expression e = syntax.asExpression();
        return e;
    }
}
