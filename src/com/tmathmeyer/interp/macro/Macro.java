package com.tmathmeyer.interp.macro;

import com.tmathmeyer.interp.Binding;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ast.AST.ASTBinding;
import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
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
	public String toString() {
		return name + " :: {{" + pattern + "}} to {{" + replace + "}}";  
	}

	@Override
	public Value interp(MappingPartial<Binding> env)
	{
		throw new RuntimeException("MACROS CANNOT BE INTERPRETED");
	}

	public ImmutableList<AST> replace(ImmutableList<AST> input)
    {
		ImmutableList<AST> result = new EmptyList<>();
		
	    for(AST asts : input)
	    {
	    	result = result.add(asts.applyMacro(this));	    	
	    }
	    
	    return result;
    }
	
	public AST macrotize(AST meBaby)
	{
		ImmutableList<ASTBinding> comp = meBaby.structureCompare(pattern);
		return replace.applyBindings(comp);
	}
	
	public AST getPattern()
	{
		return pattern;
	}
	
	public AST getReplacement()
	{
		return replace;
	}

	public String getName()
    {
	    return name;
    }
}