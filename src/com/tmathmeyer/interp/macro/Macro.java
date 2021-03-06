package com.tmathmeyer.interp.macro;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ast.AST.ASTBinding;
import com.tmathmeyer.interp.ast.MismatchedRepetitionSizeException;
import com.tmathmeyer.interp.ast.Pair;
import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.EmptyList;
import com.tmathmeyer.interp.values.ImmutableList;

public class Macro implements Expression
{
    private final String name;
    private final AST pattern;
    private final AST replace;

    public Macro(AST pattern, AST replace)
    {
        this.name = pattern.name();
        this.pattern = pattern.toRepeatingTree();
        this.replace = replace.toRepeatingTree();
    }

    @Override
    public Expression desugar()
    {
        throw new RuntimeException("MACROS CANNOT BE DESUGARED");
    }

    @Override
    public String toString()
    {
        return name + " :: {{" + pattern + "}} to {{" + replace + "}}";
    }

    @Override
    public Value interp(ImmutableList<Binding> env) throws InterpException
    {
        throw new RuntimeException("MACROS CANNOT BE INTERPRETED");
    }

    public Pair<Boolean, ImmutableList<AST>> replace(ImmutableList<AST> input)
    {
        ImmutableList<AST> result = new EmptyList<>();
        boolean changed = false;

        for (AST asts : input)
        {
            try
            {
                while (pattern.structureCompare(asts.hasMacro(name)) != null)
                {
                    Pair<AST, Boolean> pair = asts.applyMacro(this);
                    asts = pair.a;
                    changed = pair.b;
                }
            }
            catch (MismatchedRepetitionSizeException mrse)
            {

            }
            result = result.add(asts);
        }

        return new Pair<>(changed, result);
    }

    public AST macrotize(AST meBaby)
    {
        ImmutableList<ASTBinding> comp;
        try
        {
            comp = meBaby.structureCompare(pattern);
        }
        catch (MismatchedRepetitionSizeException mrse)
        {
            return meBaby;
        }
        return replace.applyBindings(comp);
    }

    public String getName()
    {
        return name;
    }
}
