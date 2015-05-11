package com.tmathmeyer.interp.use;

import java.io.FileNotFoundException;
import java.util.function.Consumer;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.DefSans;
import com.tmathmeyer.interp.Symbol;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ds.DefSansSet;
import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.ds.mipl.EmptyMappingImmutablePartialList;
import com.tmathmeyer.interp.macro.Macro;
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
		ImmutableList<Macro> macros = input.map(a -> a.asExpression()).filter(in -> in instanceof Macro)
		        .map(in -> (Macro) in);

		for (Macro macro : macros)
		{
			input = macro.replace(input);
		}

		ImmutableList<Expression> nonmacros = input.map(a -> a.asExpression()).filter(a -> !(a instanceof Macro));

		ImmutableList<Expression> los = nonmacros.map(a -> a.desugar());

		ImmutableList<DefSansSet> defs = los.filter(in -> in instanceof DefSansSet).map(in -> (DefSansSet) in);

		ImmutableList<Expression> ndefs = los.filter(in -> !(in instanceof DefSansSet));

		final ListHolder<Binding> binds = new ListHolder<>();
		binds.add(new Binding(new Symbol("#void"), Maybe.NOTHING));

		defs.forEach(new Consumer<DefSansSet>() {
			@Override
			public void accept(DefSansSet t)
			{
				t.doWithCopy(new Consumer<DefSans>() {
					@Override
					public void accept(DefSans t)
					{
						binds.add((Binding) t.interp(new EmptyMappingImmutablePartialList<>()));
					}
				});
			}
		});

		MappingPartial<Binding> bindings = MappingPartial.fromImmutableList(binds.get());

		while (!ndefs.isEmpty())
		{
			results = results.add(ndefs.first().interp(bindings));
			ndefs = ndefs.rest();
		}
	}

	public static void main(String... args) throws FileNotFoundException
	{
		String filepath = args[0];

		// String filepath = "/home/ted/pattern.lisp";

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
