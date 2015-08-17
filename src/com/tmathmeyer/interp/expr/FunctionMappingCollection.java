package com.tmathmeyer.interp.expr;

import java.util.HashSet;
import java.util.Set;

import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public class FunctionMappingCollection implements Expression
{
    private final Set<FunctionMapping> backing = new HashSet<>();
    private final ImmutableList<FunctionMapping> functions;

    public FunctionMappingCollection(ImmutableList<FunctionMapping> sans)
    {
        functions = sans;
        while (!sans.isEmpty())
        {
            backing.add(sans.first());
            sans = sans.rest();
        }
    }

    public ImmutableList<FunctionMapping> getFunctions()
    {
        return functions;
    }

    @Override
    public Expression desugar()
    {
        throw new RuntimeException("can't desugar a DefSansSet");
    }

    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        Value last = null;
        for (FunctionMapping ds : backing)
        {
            last = ds.interp(env);
        }
        return last;
    }
}
