package com.tmathmeyer.interp.expr;

import com.tmathmeyer.interp.ast.ASNode;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.expr.FunctionMapping;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public abstract class Def implements Expression
{
    public static class ValueDefinition extends Def
    {
        public final Symbol name;
        public final Expression expr;

        public ValueDefinition(Symbol name, Expression asExpression)
        {
            this.name = name;
            this.expr = asExpression;
        }

        @Override
        public Expression desugar()
        {
            return new FunctionMapping(name, expr.desugar());
        }

        @Override
        public Value interp(ImmutableList<Binding> env) throws InterpException
        {
            throw new RuntimeException("attempting to interp a #def, please desugar first");
        }
    }

    public static Def getDefn(ImmutableList<AST> rest)
    {
        Symbol name = new Symbol(((ASNode) rest.first()).value);
        return new ValueDefinition(name, rest.rest().first().asExpression());
    }

}