package com.tmathmeyer.interp.runtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ast.CharacterSequence;
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
import com.tmathmeyer.lex.Builder;

public class SlipRuntime
{
	private final ImmutableList<AST> program;
	private ImmutableList<Binding> runtime;
	
	public SlipRuntime(File file) throws FileNotFoundException
	{
		this(file, new EmptyList<>());
	}
	
	public SlipRuntime(File file, ImmutableList<Binding> runtime) throws FileNotFoundException
	{
		this(new BufferedReader(new FileReader(file)), runtime);
	}
	
	public SlipRuntime(String source)
	{
		this(source, new EmptyList<>());
	}
	
	public SlipRuntime(String source, ImmutableList<Binding> runtime)
	{
		this(CharacterSequence.make(source), runtime);
	}
	
	public SlipRuntime(BufferedReader file)
	{
		this(file, new EmptyList<>());
	}
	
	public SlipRuntime(BufferedReader file, ImmutableList<Binding> runtime)
	{
		this(CharacterSequence.make(file), runtime);
	}
	
	private SlipRuntime(CharacterSequence raw, ImmutableList<Binding> runtime)
	{
		this.runtime = runtime;
		program = new Builder(raw.asTokens()).syntaxTrees().append(RuntimeMacro.getMacros());
	}
	
	public ImmutableList<AST> runMacros()
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
		ImmutableList<Binding> bindings = runtime;
		ImmutableList<Expression> ndefs = new EmptyList<>();
		
		ImmutableList<Expression> exprs = runMacros().map(a -> a.asExpression().desugar());
		bindings = bindings.add(new Binding(new Symbol("#void"), Maybe.NOTHING));
		
		for(Expression e : exprs)
		{
			if (e instanceof FunctionMappingCollection)
			{
				bindings = bindings.append(((FunctionMappingCollection)e).getFunctions().map(F -> F.interp()));
			}
			else if (e instanceof FunctionMapping)
			{
				bindings = bindings.add(((FunctionMapping)e).interp());
			}
			else
			{
				ndefs = ndefs.add(e);
			}
		}
		
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
		
		runtime = bindings;
		return results;
	}
	
	public ImmutableList<Binding> getBindings()
	{
		return runtime;
	}
	
	public static void main(String... args) throws FileNotFoundException, InterpException
	{
		if (args.length == 0)
		{
			ImmutableList<Binding> saved = new EmptyList<>();

			@SuppressWarnings("resource")
			Scanner s = new Scanner(System.in);
			while (true)
			{
				System.out.print("\n> ");

				SlipRuntime slip = new SlipRuntime(s.nextLine(), saved);
				Value v = slip.evaluate().first();
				System.out.println(v);
				saved = slip.getBindings();
				if (v.toString().equals("exit"))
				{
					break;
				}
			}
			
			return;
		}
		
		String filepath = args[0];

		ImmutableList<Value> values = new SlipRuntime(new BufferedReader(new FileReader(new File(filepath)))).evaluate();
		
		values.isEmpty();
	}
}
