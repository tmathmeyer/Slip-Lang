package com.tmathmeyer.ci.maths;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;
import com.tmathmeyer.ci.values.Bool;

public class Equals implements Expression
{
    public final Expression L, R;

    public Equals(Expression left, Expression right)
    {
        L = left;
        R = right;
    }

    @Override
    public Expression desugar()
    {
        return new Equals(L.desugar(), R.desugar());
    }

    @Override
    public Value interp(MappingPartial<Binding> env)
    {
        return L.interp(env).equals(R.interp(env)) ? Bool.TRUE : Bool.FALSE;
    }
    
    public String toString()
    {
    	return "("+L+" == "+R+")";
    }
}