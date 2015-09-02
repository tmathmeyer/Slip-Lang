package com.tmathmeyer.interp.expr;

import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public class ID implements Expression
{
    private final Symbol I;

    public ID(Symbol s)
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
        Binding b = env.filter(B -> B.name != null && B.name.equals(I)).first(); //yikes
        if (b == null)
        {
            throw new IDLookUpException(I);
        }
        return b.val;
    }

    public String toString()
    {
        return I.toString();
    }

    private static class IDLookUpException extends InterpException
    {
    	static final long serialVersionUID = 1L;
		private final Symbol s;

        public void printStackTrace()
        {
            System.out.println("Cannot find Symbol: " + s);
        }

        private IDLookUpException(Symbol i)
        {
            s = i;
        }
    }
}