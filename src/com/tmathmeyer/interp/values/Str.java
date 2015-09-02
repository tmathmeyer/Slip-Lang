package com.tmathmeyer.interp.values;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;

public class Str implements Expression, Value
{
    public final String value;

    public Str(CharSequence charSequence)
    {
        value = charSequence.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Str other = (Str) obj;
        if (value == null)
        {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
    	return getPrintString();
    }

    @Override
    public Expression desugar()
    {
        return this;
    }

    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        return new Str(value);
    }

    @Override
    public String getPrintString()
    {
        return value;
    }

    @Override
    public String getTypeName()
    {
        return "string";
    }
}