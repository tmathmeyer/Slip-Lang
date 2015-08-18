package com.tmathmeyer.interp.runtime;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.FunctionMapping;
import com.tmathmeyer.interp.expr.FunctionMappingCollection;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.expr.Symbol;
import com.tmathmeyer.interp.expr.Function.Pair;
import com.tmathmeyer.interp.macro.Macro;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.EmptyList;
import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.interp.values.Maybe;
import com.tmathmeyer.reparse.Builder;
import com.tmathmeyer.reparse.StreamParser;

public class SlipRuntime
{
    public static final ImmutableList<Binding> JUST_VOID = new EmptyList<Binding>().add(new Binding(new Symbol("#void"), Maybe.NOTHING));
    private final ImmutableList<AST> program;
    private ImmutableList<Binding> runtime;

    public SlipRuntime(File file) throws FileNotFoundException
    {
        this(file, JUST_VOID);
    }

    public SlipRuntime(File file, ImmutableList<Binding> runtime) throws FileNotFoundException
    {
        this(new FileInputStream(file), runtime, true);
    }
    
    public SlipRuntime(String string, boolean emulateFile)
    {
        this(string, JUST_VOID, emulateFile);
    }
    
    public SlipRuntime(String string)
    {
        this(string, JUST_VOID, false);
    }
    
    public SlipRuntime(String string, ImmutableList<Binding> runtime, boolean emulateFile)
    {
        this(new ByteArrayInputStream(string.getBytes()), runtime, emulateFile);
    }
    
    
    

    private SlipRuntime(InputStream source, ImmutableList<Binding> runtime, boolean isFile)
    {
        this.runtime = runtime;
        program = new Builder(new StreamParser(source, isFile).getTokens()).syntaxTrees();
    }

    public ImmutableList<AST> runMacros()
    {
        ImmutableList<Macro> macros = program.append(RuntimeMacro.getMacros()).filter(A -> A.isMacro()).map(A -> (Macro) A.asExpression());
        ImmutableList<AST> source = program;
        boolean continuation;
        do
        {
            continuation = false;
            for (Macro m : macros)
            {
                Pair<Boolean, ImmutableList<AST>> result = m.replace(source);
                source = result.b;
                continuation |= result.a;
            }
        } while (continuation);

        return source.filter(A -> !A.isMacro());
    }

    public ImmutableList<Expression> getProgramData()
    {
        return runMacros().map(a -> a.asExpression().desugar());
    }
    
    public ImmutableList<AST> getSyntaxTree()
    {
        return program;
    }
    
    public ImmutableList<Value> evaluate()
    {
        ImmutableList<Binding> bindings = runtime;
        ImmutableList<Expression> ndefs = new EmptyList<>();

        ImmutableList<Expression> exprs = getProgramData();

        for (Expression e : exprs)
        {
            if (e instanceof FunctionMappingCollection)
            {
                final ImmutableList<Binding> binds = bindings;
                bindings = bindings.append(((FunctionMappingCollection) e).getFunctions()
                        .map(F -> F.catchInterp(binds)));
            }
            else if (e instanceof FunctionMapping)
            {
                bindings = bindings.add(((FunctionMapping) e).catchInterp(bindings));
            }
            else
            {
                ndefs = ndefs.add(e);
            }
        }

        ImmutableList<Value> results = new EmptyList<>();
        for (Expression N : ndefs)
        {
            try
            {
                results = results.add(N.interp(bindings));
            }
            catch (InterpException ie)
            {
                ie.printStackTrace();
            }
        }

        runtime = bindings;
        return results;
    }

    public ImmutableList<Binding> getBindings()
    {
        return runtime;
    }

    public static void main(String... args) throws InterpException, IOException
    {
        if (args.length == 0)
        {
            ImmutableList<Binding> saved = new EmptyList<>();

            while (true)
            {
                System.out.print("\n> ");

                SlipRuntime slip = new SlipRuntime(System.in, saved, false);
                Value v = slip.evaluate().first();
                System.out.println(v);
                saved = slip.getBindings();
                if (v != null && v.toString().equals("exit"))
                {
                    break;
                }
            }

            return;
        }

        String filepath = args[0];

        ImmutableList<Value> values = new SlipRuntime(new File(filepath))
                .evaluate();

        values.isEmpty();
    }
}
