package com.tmathmeyer.interp.struct;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.Symbol;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

class Struct implements Value
{
    final ImmutableList<Binding> values;
    private final Symbol name;

    Struct(Symbol sname, ImmutableList<Binding> intersect)
    {
        values = intersect;
        name = sname;
    }

    @Override
    public String toString()
    {
        return name + "" + values;
    }

    @Override
    public String getTypeName()
    {
        return "@" + name;
    }
}
