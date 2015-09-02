package com.tmathmeyer.interp.values;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.Symbol;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;

public class Closure implements Value
{
    public final ImmutableList<Binding> environment;
    public final Expression body;
    public final ImmutableList<Symbol> args;

    public Closure(ImmutableList<Binding> env, Expression bod, ImmutableList<Symbol> arg)
    {
        environment = env;
        body = bod;
        args = arg;
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
        Closure other = (Closure) obj;
        if (args == null)
        {
            if (other.args != null)
                return false;
        } else if (!args.equals(other.args))
            return false;
        if (body == null)
        {
            if (other.body != null)
                return false;
        } else if (!body.equals(other.body))
            return false;
        if (environment == null)
        {
            if (other.environment != null)
                return false;
        } else if (!environment.equals(other.environment))
            return false;
        return true;
    }

    @Override
    public String getPrintString()
    {
        return "#Closure";
    }

    @Override
    public String toString()
    {
    	return getPrintString();
    }

    @Override
    public String getTypeName()
    {
        return "closure";
    }
}