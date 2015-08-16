package com.tmathmeyer.interp.struct;

import com.tmathmeyer.interp.ast.ASNode;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.Function;
import com.tmathmeyer.interp.expr.FunctionMapping;
import com.tmathmeyer.interp.expr.FunctionMappingCollection;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.expr.Lambda;
import com.tmathmeyer.interp.expr.Symbol;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.EmptyList;
import com.tmathmeyer.interp.values.ImmutableList;

public class StructDefn implements Expression
{
	final Symbol name;
	final ImmutableList<Symbol> args;

	public StructDefn(Symbol name2, ImmutableList<Symbol> args2)
	{
		name = name2;
		args = args2;
	}

	public StructDefn(ImmutableList<AST> rest)
	{
		name = new Symbol(((ASNode) rest.first()).value);

		ImmutableList<Symbol> args = new EmptyList<>();
		for (AST tree : rest.rest().first().getParts())
		{
			args = args.add(new Symbol(((ASNode) tree).value));
		}
		this.args = args.reverse();
	}

	@Override
	public Expression desugar()
	{
		ImmutableList<FunctionMapping> sans = args.map(new Function<Symbol, FunctionMapping>() {
			@Override
			public FunctionMapping eval(Symbol in)
			{
				Symbol fnname = name.append("-").append(in);
				return new FunctionMapping(fnname, new Lambda(new StructInspector(in), new Symbol("struct")));
			}
		});

		sans = sans.add(new FunctionMapping(name, new Lambda(new StructFactory(this), args)));

		return new FunctionMappingCollection(sans);

	}

	@Override
	public Value interp(ImmutableList<Binding> env) throws InterpException
	{
		throw new RuntimeException("attempting to interp a #def, please desugar first");
	}
}