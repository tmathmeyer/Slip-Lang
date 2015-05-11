package com.tmathmeyer.interp.struct;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.DefSans;
import com.tmathmeyer.interp.Function;
import com.tmathmeyer.interp.Lambda;
import com.tmathmeyer.interp.Symbol;
import com.tmathmeyer.interp.ast.ASNode;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ds.DefSansSet;
import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
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
		for(AST tree : rest.rest().first().getParts())
		{
			args = args.add( new Symbol(((ASNode)tree).value) );
		}
	    this.args = args.reverse();
    }

	@Override
	public Expression desugar()
	{
		ImmutableList<DefSans> sans = args.map(new Function<Symbol, DefSans>(){
			@Override
            public DefSans eval(Symbol in)
            {
				Symbol fnname = name.append("-").append(in);
	            return new DefSans(fnname, new Lambda(new StructInspector(in), new Symbol("struct")));
            }
		});
		
		sans = sans.add(new DefSans(name, new Lambda(new StructFactory(this), args)));
		
		return new DefSansSet(sans);
		
		
	}

	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		throw new RuntimeException("attempting to interp a #def, please desugar first");
	}
}