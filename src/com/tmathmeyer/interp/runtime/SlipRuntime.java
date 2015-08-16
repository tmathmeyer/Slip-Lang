package com.tmathmeyer.interp.runtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.Function.Pair;
import com.tmathmeyer.interp.FunctionMapping;
import com.tmathmeyer.interp.InterpException;
import com.tmathmeyer.interp.Symbol;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ast.CharacterSequence;
import com.tmathmeyer.interp.ds.FunctionMappingCollection;
import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.macro.Macro;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.interp.values.Maybe;
import com.tmathmeyer.lex.Builder;

public class SlipRuntime
{
	private final ImmutableList<AST> program;
	
	public SlipRuntime(String source)
	{
		this(CharacterSequence.make(source));
	}
	
	public SlipRuntime(BufferedReader file)
	{
		this(CharacterSequence.make(file));
	}
	
	private SlipRuntime(CharacterSequence raw)
	{
		program = new Builder(raw.asTokens()).syntaxTrees().append(RuntimeMacro.getMacros());
	}
	
	private ImmutableList<AST> runMacros()
	{
		ImmutableList<Macro> macros = program.filter(A -> A.isMacro())
											 .map(A -> (Macro)A.asExpression());
		ImmutableList<AST> source = program;
		boolean continuation;
		do
		{
			continuation = false;
			for(Macro m : macros)
			{
				Pair<Boolean, ImmutableList<AST>> result = m.replace(source);
				source = result.b;
				continuation |= result.a;
			}
		} while(continuation);
		
		return source.filter(A -> !A.isMacro());
	}
	
	public ImmutableList<Value> evaluate()
	{
		ImmutableList<Binding> binds = new EmptyList<>();
		ImmutableList<Expression> ndefs = new EmptyList<>();
		
		ImmutableList<Expression> exprs = runMacros().map(a -> a.asExpression().desugar());
		binds.add(new Binding(new Symbol("#void"), Maybe.NOTHING));
		
		for(Expression e : exprs)
		{
			if (e instanceof FunctionMappingCollection)
			{
				binds = binds.append(((FunctionMappingCollection)e).getFunctions().map(F -> F.interp()));
			}
			else if (e instanceof FunctionMapping)
			{
				binds = binds.add(((FunctionMapping)e).interp());
			}
			else
			{
				ndefs = ndefs.add(e);
			}
		}
		
		MappingPartial<Binding> bindings = MappingPartial.fromImmutableList(binds);
		ImmutableList<Value> results = new EmptyList<>();
		for(Expression N : ndefs)
		{
			try
			{
				results = results.add(N.interp(bindings));
			}
			catch(InterpException ie)
			{
				ie.printStackTrace();
			}
		}
		
		return results;
	}
	
	public static void main(String... args) throws FileNotFoundException, InterpException
	{
		String filepath = args[0];

		ImmutableList<Value> values = new SlipRuntime(new BufferedReader(new FileReader(new File(filepath)))).evaluate();
		
		values.isEmpty();
	}
}
