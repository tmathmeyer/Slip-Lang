package com.tmathmeyer.interp.ast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.expr.Application;
import com.tmathmeyer.interp.expr.Def;
import com.tmathmeyer.interp.expr.Function.Pair;
import com.tmathmeyer.interp.expr.If;
import com.tmathmeyer.interp.expr.Lambda;
import com.tmathmeyer.interp.expr.Loader;
import com.tmathmeyer.interp.expr.Print;
import com.tmathmeyer.interp.expr.Rest;
import com.tmathmeyer.interp.expr.Type;
import com.tmathmeyer.interp.list.Cons;
import com.tmathmeyer.interp.list.First;
import com.tmathmeyer.interp.macro.Macro;
import com.tmathmeyer.interp.maths.BinaryMathExpression;
import com.tmathmeyer.interp.struct.StructDefn;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.values.ImmutableList;

public class ASTree implements AST
{
	private final List<AST> parts;

	ASTree(List<AST> newlist)
	{
		parts = newlist;
	}

	public ASTree()
	{
		this(new LinkedList<>());
	}

	public String toString()
	{
		return parts.toString();
	}

	@Override
	public Expression asExpression()
	{
		ImmutableList<AST> list = ImmutableList.fromSTD(parts);
		if (list.first() instanceof ASNode)
		{
			ASNode node = (ASNode) list.first();
			int switchval = SymbolLookupTable.lookup(node.value);

			switch (switchval)
			{
				case 0:
					return BinaryMathExpression.fromAST(list.rest(), node.value);
				case 1:
					return new Lambda(list.rest().rest().first().asExpression(), Lambda.getArgs(list.rest().first()));
				case 3:
					return new If(list.rest());
				case 4:
					return new Print(list.rest());
				case 5:
					return new Cons(list.rest());
				case 6:
					return new First(list.rest());
				case 7:
					return new Rest(list.rest());
				case 8:
					return Def.getDefn(list.rest());
				case 9:
					return new StructDefn(list.rest());
				case 10:
					return new Type(list.rest());
				case 11:
					return new Macro(list.rest().first(), list.rest().rest().first());
				case 13:
					return new Loader(list.rest().first());
				default:
					return new Application(list);

			}
		}

		return new Application(list);
	}

	@Override
	public String name()
	{
		if (parts.size() > 0)
		{
			return parts.get(0).name();
		}
		return null;
	}

	@Override
	public AST toRepeatingTree()
	{
		List<AST> trees = new LinkedList<>();
		parts.stream().map(a -> a.toRepeatingTree()).forEach(a -> trees.add(a));
		if (trees.get(trees.size() - 1).toString().equals("..."))
		{
			trees.remove(trees.size() - 1);
			return new RepeatingAST(trees);
		}
		ASTree tree = new ASTree();
		tree.parts.addAll(trees);
		return tree;
	}

	@Override
	public ImmutableList<ASTBinding> structureCompare(AST t) throws MismatchedRepetitionSizeException
	{
		return t==null? null : t.structureCompare(this);
	}

	@Override
	public ImmutableList<ASTBinding> structureCompare(ASTree t) throws MismatchedRepetitionSizeException
	{
		if (t.parts.size() != parts.size())
		{
			return null;
		}
		Iterator<AST> a = parts.iterator();
		Iterator<AST> b = t.parts.iterator();

		ImmutableList<ASTBinding> tmp = new EmptyList<>();
		while (a.hasNext())
		{
			tmp = tmp.append(a.next().structureCompare(b.next()));
		}
		return tmp;
	}

	@Override
	public ImmutableList<ASTBinding> structureCompare(ASNode t) throws MismatchedRepetitionSizeException
	{
		if (parts.get(0).equals(t))
		{
			throw new MismatchedRepetitionSizeException();
		}
		return t.structureCompare(this);
	}

	@Override
	public ImmutableList<ASTBinding> structureCompare(RepeatingAST t) throws MismatchedRepetitionSizeException
	{
		return t.structureCompare(this);
	}

	@Override
	public ASTBinding bindTo(AST bNext) throws MismatchedRepetitionSizeException
	{
		List<AST> next = bNext.getParts();

		if (next.size() != parts.size())
		{
			throw new RuntimeException("AST mismatch :: " + this + " :: " + bNext);
		}
		Iterator<AST> iNext = next.iterator();
		Iterator<AST> iPart = parts.iterator();

		ImmutableList<ASTBinding> ilast = new EmptyList<>();
		while (iNext.hasNext())
		{
			ilast = ilast.add(iPart.next().bindTo(iNext.next()));
		}

		return new ASTreeBinding(ilast);
	}

	@Override
	public List<AST> getParts()
	{
		return parts;
	}

	@Override
	public AST applyBindings(ImmutableList<ASTBinding> comp)
	{
		List<AST> newlist = new LinkedList<>();
		for (AST t : parts)
		{
			newlist.add(t.applyBindings(comp));
		}
		return new ASTree(newlist);
	}

	@Override
	public Pair<AST, Boolean> applyMacro(Macro macro)
	{
		List<AST> asts = new LinkedList<>();
		boolean changed = false;
		for (AST t : parts)
		{
			Pair<AST, Boolean> tt = t.applyMacro(macro);
			asts.add(tt.a);
			changed |= tt.b;
		}
		AST tree = new ASTree(asts);
		
		if (asts.size() > 0)
		{
			if (asts.get(0).toString().equals(macro.getName()))
			{
				tree = macro.macrotize(tree);
				changed = true;
			}
		}
		
		return new Pair<>(tree, changed);
	}

	@Override
	public AST hasMacro(String name)
	{
		if (parts.size() == 0)
		{
			return null;
		}
		if (parts.get(0).toString().equals(name))
		{
			return this;
		}
		if (parts.get(0).toString().equals("#"))
		{
			return null;
		}

		for (AST t : parts)
		{
			t = t.hasMacro(name);
			if (t != null)
			{
				return t;
			}
		}

		return null;
	}

	@Override
    public boolean isMacro()
    {
	    return parts.size() > 0 && parts.get(0).isMacro();
    }
}