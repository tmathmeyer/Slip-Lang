package com.tmathmeyer.interp.expr;

import com.tmathmeyer.interp.ast.ASNode;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ast.ASTree;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.EmptyList;
import com.tmathmeyer.interp.values.ImmutableList;

public class Sym implements Expression, Value
{
    private final AST I;

    public Sym(AST s)
    {
        I = s;
    }

    @Override
    public Expression desugar()
    {
        return this;
    }

    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        if (I instanceof ASNode)
        {
            return this;
        }
        ImmutableList<Value> result = new EmptyList<>();
        ASTree tree = (ASTree) I;
        for (AST t : tree.getParts())
        {
            result = result.add(new Sym(t).interp(env));
        }

        return result.reverse();
    }

    @Override
    public String getPrintString()
    {
        return I.toString();
    }

    @Override
    public String toString()
    {
        return getPrintString();
    }

    @Override
    public String getTypeName()
    {
        return "Symbol";
    }

    public Value eval(ImmutableList<Binding> env) throws InterpException
    {
        return I.asExpression().interp(env);
    }
}