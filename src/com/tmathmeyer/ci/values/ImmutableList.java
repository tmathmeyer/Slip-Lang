package com.tmathmeyer.ci.values;

import com.tmathmeyer.ci.Defn;
import com.tmathmeyer.ci.Function;
import com.tmathmeyer.ci.ID;
import com.tmathmeyer.ci.Symbol;
import com.tmathmeyer.ci.Function.Pair;
import com.tmathmeyer.ci.ds.EmptyList;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;

/**
 * Created by ted on 12/19/14.
 */
public abstract class ImmutableList<T> implements Value
{
	public abstract T first();

	public abstract ImmutableList<T> rest();

	public abstract ImmutableList<T> add(T t);

	public abstract ImmutableList<T> reverse();

	public abstract ImmutableList<T> addEnd(T t);

	public abstract boolean isEmpty();

	public abstract String asCSV();
	
	public abstract boolean contains(T name);
	
	public <O> ImmutableList<O> map(Function<T, O> fxn)
	{
		return map(fxn, this);
	}
	
	public ImmutableList<T> filter(Function<T, Boolean> fxn)
	{
		return filter(fxn, this);
	}

	public static <T> ImmutableList<T> fromSTD(java.util.List<T> list)
	{
		ImmutableList<T> result = new EmptyList<>();
		for (int i = list.size(); i > 0; i--)
		{
			result = result.add(list.get(i - 1));
		}
		return result;
	}

	public static <I, O> ImmutableList<O> map(Function<I, O> func, ImmutableList<I> in)
	{
		if (in.isEmpty())
		{
			return new EmptyList<O>();
		}
		return map(func, in.rest()).add(func.eval(in.first()));
	}

	public static <I> ImmutableList<I> filter(Function<I, Boolean> func, ImmutableList<I> in)
	{
		if (in.isEmpty())
		{
			return new EmptyList<I>();
		}
		I i = in.first();
		if (func.eval(i))
		{
			return filter(func, in.rest()).add(i);
		}
		return filter(func, in.rest());
	}

	public static <I, O> O foldl(Function<Pair<I, O>, O> func, ImmutableList<I> in, O start)
	{
		if (in.isEmpty())
		{
			return start;
		}
		return foldl(func, in.rest(), func.eval(new Function.Pair<>(in.first(), start)));
	}

	public static Function<Expression, Expression> getDesugarer()
	{
		return new Function<Expression, Expression>() {
			@Override
			public Expression eval(Expression in)
			{
				return in.desugar();
			}
		};
	}

	public static Function<Defn, Symbol> defnToSymbol()
	{
		return new Function<Defn, Symbol>() {
			@Override
			public Symbol eval(Defn in)
			{
				return in.S;
			}
		};
	}

	public static Function<Defn, Expression> defnToExpr()
	{
		return new Function<Defn, Expression>() {
			@Override
			public Expression eval(Defn in)
			{
				return in.E.desugar();
			}
		};
	}

	public static Function<Expression, Symbol> exprToSymbol()
	{
		return new Function<Expression, Symbol>() {
			@Override
			public Symbol eval(Expression in)
			{
				return ((ID) in).I;
			}
		};
	}

}
