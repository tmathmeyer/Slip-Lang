package com.tmathmeyer.ci.struct;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.DefSans;
import com.tmathmeyer.ci.Function;
import com.tmathmeyer.ci.Lambda;
import com.tmathmeyer.ci.Symbol;
import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.ast.AST.ASNode;
import com.tmathmeyer.ci.ast.AST.ASTree;
import com.tmathmeyer.ci.ds.DefSansSet;
import com.tmathmeyer.ci.ds.EmptyList;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;
import com.tmathmeyer.ci.values.ImmutableList;

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
		for(AST tree : ((ASTree) rest.rest().first()).parts)
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