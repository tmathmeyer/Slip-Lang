package com.tmathmeyer.ci;

import static com.tmathmeyer.ci.values.ImmutableList.foldl;
import static com.tmathmeyer.ci.values.ImmutableList.map;

import com.tmathmeyer.ci.Function.Pair;
import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.ds.EmptyList;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;
import com.tmathmeyer.ci.values.Closure;
import com.tmathmeyer.ci.values.ImmutableList;

public class Application implements Expression
{
    public final Expression func;
    public final ImmutableList<Expression> args;

    public Application(Expression function, Expression... arguments)
    {
        func = function;
        ImmutableList<Expression> temp = new EmptyList<Expression>();
        for (Expression e : arguments)
        {
            temp = temp.add(e);
        }
        args = temp;
    }

    public Application(Expression function, ImmutableList<Expression> arguments)
    {
        func = function;
        args = arguments;
    }

    public Application(ImmutableList<AST> list)
    {
        func = list.first().asExpression();
        args = map(AST.toExpression(), list.rest());
    }

    @Override
    public Expression desugar()
    {
        ImmutableList<Expression> argstemp = args;

        return new Application(func.desugar(), map(ImmutableList.getDesugarer(), argstemp));
    }

    @Override
    public Value interp(MappingPartial<Binding> env)
    {
        Closure cloj = (Closure) func.interp(env);

        ImmutableList<Expression> expTemp = args;
        ImmutableList<Symbol> symTemp = cloj.args;

        MappingPartial<Binding> passOn = env;

        while (!expTemp.isEmpty())
        {
            passOn = passOn.add(new Binding(symTemp.first(), expTemp.first().interp(env)));
            expTemp = expTemp.rest();
            symTemp = symTemp.rest();
        }

        if (!symTemp.isEmpty())
        {
            throw new RuntimeException("mismatched arg length - " + this);
        }

        return cloj.body.interp(passOn);
    }

    public String toString()
    {
        return "(" + func + foldl(new Function<Pair<Expression, String>, String>() {
            @Override
            public String eval(com.tmathmeyer.ci.Function.Pair<Expression, String> in)
            {
                return in.b + " " + in.a;
            }
        }, args, "") + ")";
    }
}