package com.tmathmeyer.interp.expr;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.interp.values.Str;

public class Type implements Expression
{
    private final Expression of;

    private Type(Expression l)
    {
        of = l;
    }

    public Type(ImmutableList<AST> rest)
    {
        of = rest.first().asExpression();
    }

    @Override
    public Expression desugar()
    {
        return new Type(of.desugar());
    }

    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        return new Str(of.interp(env).getClass().getSimpleName());
    }
    
    public String toString()
    {
        return "(type " + of + ")";
    }
}