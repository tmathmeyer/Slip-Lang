package com.tmathmeyer.interp.expr;

/**
 * Created by ted on 12/19/14.
 */
public class Symbol implements Comparable<Symbol>
{
    private final String value;

    public Symbol(String value)
    {
        this.value = value;
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == null)
        {
            return false;
        }
        if (other == this)
        {
            return true;
        }
        if (other instanceof Symbol)
        {
            return ((Symbol) other).value.equals(value);
        }
        return false;
    }

    @Override
    public int compareTo(Symbol symbol)
    {
        return value.compareTo(symbol.value);
    }

    public String toString()
    {
        return value;
    }

    public Symbol append(String string)
    {
        return new Symbol(value + string);
    }

    public Symbol append(Symbol in)
    {
        return new Symbol(value + in.value);
    }
}
