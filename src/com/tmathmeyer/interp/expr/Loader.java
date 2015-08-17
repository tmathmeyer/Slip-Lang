package com.tmathmeyer.interp.expr;

import java.io.File;
import java.io.FileNotFoundException;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.maths.InvalidTypeException;
import com.tmathmeyer.interp.runtime.SlipRuntime;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.interp.values.Str;

public class Loader implements Expression
{
    private final Expression filename;

    public Loader(AST first)
    {
        this(first.asExpression());
    }

    private Loader(Expression expr)
    {
        filename = expr;
    }

    @Override
    public Expression desugar()
    {
        return new Loader(filename.desugar());
    }

    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        Value val = filename.interp(env);

        if (val instanceof Str)
        {
            String v = ((Str) val).value;
            try
            {
                return new SlipRuntime(new File(v), env).evaluate().first();
            }
            catch (FileNotFoundException e)
            {
                throw new LoaderException(v);
            }
        }
        else
        {
            throw new InvalidTypeException(filename, this);
        }
    }

    private static class LoaderException extends InterpException
    {
        private final String s;

        public void printStackTrace()
        {
            System.out.println("Cannot load file: " + s);
        }

        private LoaderException(String file)
        {
            s = file;
        }
    }

}
