package com.tmathmeyer.interp.expr;

import com.tmathmeyer.interp.ast.ASNode;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Closure;
import com.tmathmeyer.interp.values.EmptyList;
import com.tmathmeyer.interp.values.ImmutableList;

public class Lambda implements Expression
{
    private final Expression body;
    private final ImmutableList<Symbol> args;

    public Lambda(Expression bod, Symbol arguments)
    {
        this(bod, new EmptyList<Symbol>().add(arguments));
    }

    public Lambda(Expression function, ImmutableList<Symbol> arguments)
    {
        body = function;
        args = arguments;
    }

    @Override
    public Expression desugar()
    {
        return new Lambda(body.desugar(), args);
    }

    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        return new Closure(env, body, args);
    }

    public static ImmutableList<Symbol> getArgs(AST first)
    {
        if (first instanceof ASNode)
        {
            throw new RuntimeException("these arent args!!");
        }

        return ImmutableList.fromSTD(first.getParts()).map(in -> new Symbol(((ASNode) in).value));
    }

    public String toString()
    {
        String result = "(lambda (";
        for (Symbol s : args)
        {
            result += (" " + s);
        }
        return result + ") " + body + ")";
    }
}