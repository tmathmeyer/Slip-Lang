package com.tmathmeyer.interp.ast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.macro.Macro;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.values.ImmutableList;

public class RepeatingAST implements AST
{
	private final List<AST> tree;
	
	public RepeatingAST(List<AST> tree)
    {
	    this.tree = tree;
    }
	
	@Override
	public Expression asExpression()
	{
		throw new RuntimeException("cannot convert a repeating AST to an expression!");
	}

	@Override
	public String name()
	{
		if (tree.size() > 0)
        {
        	return tree.get(0).name();
        }
        return null;
	}

	@Override
    public AST toRepeatingTree()
    {
	    return this;
    }
	
	@Override
	public String toString()
	{
		return tree.toString();
	}

	@Override
    public ImmutableList<ASTBinding> structureCompare(AST t)
    {
	    return t.structureCompare(this);
    }

	@Override
    public ImmutableList<ASTBinding> structureCompare(ASTree t)
    {
		if (tree.size() > t.getParts().size())
		{
			return new EmptyList<>();
		}
	    Iterator<AST> a = tree.iterator();
	    Iterator<AST> b = t.getParts().iterator();
	    AST myLast = null;
	    ImmutableList<ASTBinding> binds = new EmptyList<>();
	    while(b.hasNext())
	    {
	    	if (a.hasNext())
	    	{
	    		myLast = a.next();
	    	}
	    	
	    	AST bNext = b.next();
	    	
	    	if (myLast.structureCompare(bNext) == null)
	    	{
	    		return new EmptyList<>();
	    	}
	    	
	    	binds = binds.add(myLast.bindTo(bNext).setRepeatingTag());
	    }
	    return binds;
	    
    }

	@Override
    public ImmutableList<ASTBinding> structureCompare(ASNode t)
    {
	    return null;
    }

	@Override
    public ImmutableList<ASTBinding> structureCompare(RepeatingAST t)
    {
	    throw new RuntimeException("macroing a macro is not macro-d");
    }

	@Override
    public ASTBinding bindTo(AST bNext)
    {
		ImmutableList<ASTBinding> binds = this.structureCompare(bNext);
		return new ASTreeBinding(binds);
    }

	@Override
    public List<AST> getParts()
    {
	    return tree;
    }

	@Override
    public AST applyBindings(ImmutableList<ASTBinding> comp)
    {
		List<AST> newAST = new LinkedList<>();
		Iterator<AST> iTree = tree.iterator();
		int size = tree.size();
		while(size --> 1)
		{
			newAST.add(iTree.next().applyBindings(comp.filter(a -> !a.isRepeating())));
		}
		AST repetition = iTree.next();
		
	    for(ASTBinding ab : comp.filter(a -> a.isRepeating()))
	    {
	    	newAST.add(repetition.applyBindings(ab.asList()));
	    }
	    
	    return new ASTree(newAST);
    }

	@Override
    public AST applyMacro(Macro macro)
    {
		if (tree.size() > 0)
	    {
	    	if (tree.get(0).toString().equals(macro.getName()))
	    	{
	    		return macro.macrotize(this);
	    	}
	    }
	    List<AST> asts = new LinkedList<>();
	    for(AST t : tree)
	    {
	    	asts.add(t.applyMacro(macro));
	    }
	    return new ASTree(asts);
    }
}
