package com.tmathmeyer.interp.expr;

import com.tmathmeyer.interp.ast.ASNode;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.values.ImmutableList;

public class Defn
{
    public final Expression E;
    public final Symbol S;

    public Defn(Expression exp, Symbol sym)
    {
        S = sym;
        E = exp;
    }

    public Defn(AST in)
    {
        ImmutableList<AST> parts = ImmutableList.fromSTD(in.getParts());
        ASNode node = (ASNode) parts.first();

        S = new Symbol(node.value);
        E = parts.rest().first().asExpression();
    }

    public String toString()
    {
        return "<" + S + "::" + E + ">";
    }
}