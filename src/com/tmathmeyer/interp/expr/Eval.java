package com.tmathmeyer.interp.expr;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.maths.InvalidTypeException;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public class Eval implements Expression
{
    private final Expression source;

    public Eval(AST first)
    {
        this(first.asExpression());
    }

    private Eval(Expression expr)
    {
        source = expr;
    }

    @Override
    public Expression desugar()
    {
        return new Eval(source.desugar());
    }

    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        Value val = source.interp(env);

        if (val instanceof Sym)
        {
            return ((Sym) val).interp(env);
        }
        else if (val instanceof ImmutableList)
        {
            return AST.fromILA(val).asExpression().interp(env);
        }
        else
        {
            throw new InvalidTypeException(source, this);
        }
    }
}
