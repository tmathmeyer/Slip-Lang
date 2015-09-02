package com.tmathmeyer.interp.runtime;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.runtime.macro.BMatch;
import com.tmathmeyer.interp.runtime.macro.Define;
import com.tmathmeyer.interp.runtime.macro.EmptyHuh;
import com.tmathmeyer.interp.runtime.macro.Let;
import com.tmathmeyer.interp.runtime.macro.List;
import com.tmathmeyer.interp.runtime.macro.Struct;
import com.tmathmeyer.interp.values.EmptyList;
import com.tmathmeyer.interp.values.ImmutableList;

public abstract class RuntimeMacro
{
    private static ImmutableList<String> macros = new EmptyList<>();

    public abstract ImmutableList<String> getSrc();

    static
    {
        load(new BMatch());
        load(new EmptyHuh());
        load(new Define());
        load(new Let());
        load(new List());

        load(new Struct());
    }
    
    private static void load(RuntimeMacro macro)
    {
        macros = macros.append(macro.getSrc());
    }
    

    protected static ImmutableList<String> asList(String... str)
    {
        ImmutableList<String> result = new EmptyList<>();
        for (String s : str)
        {
            result = result.add(s);
        }
        return result;
    }

    static ImmutableList<AST> getMacros()
    {
        return macros.map(S -> new SlipRuntime(S).getSyntaxTree().first());
    }
}
