package com.tmathmeyer.interp.use;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.Symbol;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ds.DefSansSet;
import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.ds.mipl.EmptyMappingImmutablePartialList;
import com.tmathmeyer.interp.macro.Macro;
import com.tmathmeyer.interp.runtime.RuntimeMacro;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.interp.values.Maybe;
import com.tmathmeyer.lex.Builder;
import com.tmathmeyer.lex.Tokenizer;

public class Language
{
	public ImmutableList<Value> results = new EmptyList<>();

	public Language(ImmutableList<AST> input)
	{
		results = runLanguage(input);
	}
	
	public static ImmutableList<AST> runMacros(ImmutableList<AST> trees)
	{
		ImmutableList<AST> code = new EmptyList<>();
		List<Macro> macros = new LinkedList<>();
		
		for (AST t : trees)
		{
			if (t.asExpression().getClass().equals(Macro.class))
			{
				macros.add((Macro) t.asExpression());
			}
			else
			{
				code = code.add(t);
			}
		}

		ImmutableList<AST> codecmp = code;

		do
		{
			code = codecmp;
			for (Macro m : macros)
			{
				codecmp = m.replace(codecmp);
			}
		}
		while (!code.toString().equals(codecmp.toString()) && !code.reverse().toString().equals(codecmp.toString()));

		return code;
	}
	
	public static ImmutableList<Value> runLanguage(ImmutableList<AST> src)
	{
		src = src.append(RuntimeMacro.getMacros());
		src = runMacros(src);
		
		ListHolder<Binding> binds = new ListHolder<>();
		ImmutableList<Value> results = new EmptyList<>();
		ImmutableList<DefSansSet> defs = new EmptyList<>();
		ImmutableList<Expression> ndefs = new EmptyList<>();
		
		for(Expression e : src.map(a -> a.asExpression().desugar()))
		{
			if (e instanceof DefSansSet)
			{
				defs = defs.add((DefSansSet) e);
			}
			else
			{
				ndefs = ndefs.add(e);
			}
		}
		
		
		binds.add(new Binding(new Symbol("#void"), Maybe.NOTHING));
		defs.forEach(a -> a.doWithCopy(b -> binds.add((Binding) b.interp(new EmptyMappingImmutablePartialList<>()))));
		MappingPartial<Binding> bindings = MappingPartial.fromImmutableList(binds.get());

		while (!ndefs.isEmpty())
		{
			results = results.add(ndefs.first().interp(bindings));
			ndefs = ndefs.rest();
		}
		
		return results;
	}
	

	public static void main(String... args) throws FileNotFoundException
	{
		String filepath = args[0];

		ImmutableList<AST> astcollection = new Builder().fromTokens(Tokenizer.getTokens(filepath));

		new Language(astcollection);
	}

	private static class ListHolder<T>
	{
		private ImmutableList<T> inner = new EmptyList<>();

		public void add(T t)
		{
			inner = inner.add(t);
		}

		public ImmutableList<T> get()
		{
			return inner;
		}
	}
}
