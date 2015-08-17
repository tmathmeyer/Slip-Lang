package com.tmathmeyer.interp.expr;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public class Rest implements Expression
{
    private final Expression list;

    private Rest(Expression l)
    {
        list = l;
    }

    public Rest(ImmutableList<AST> rest)
    {
        list = rest.first().asExpression();
    }

    @Override
    public Expression desugar()
    {
        return new Rest(list.desugar());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        return ((ImmutableList<Value>) list.interp(env)).rest();
    }
}