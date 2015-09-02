package com.tmathmeyer.interp.ast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.tmathmeyer.interp.expr.Application;
import com.tmathmeyer.interp.expr.Def;
import com.tmathmeyer.interp.expr.Eval;
import com.tmathmeyer.interp.expr.Function.Pair;
import com.tmathmeyer.interp.expr.If;
import com.tmathmeyer.interp.expr.Lambda;
import com.tmathmeyer.interp.expr.Loader;
import com.tmathmeyer.interp.expr.Print;
import com.tmathmeyer.interp.expr.Rest;
import com.tmathmeyer.interp.expr.Sym;
import com.tmathmeyer.interp.expr.Type;
import com.tmathmeyer.interp.list.Cons;
import com.tmathmeyer.interp.list.First;
import com.tmathmeyer.interp.macro.Macro;
import com.tmathmeyer.interp.maths.MathExpression;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.values.EmptyList;
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
    	StringBuilder sb = new StringBuilder("(");
    	boolean first = true;
    	for(AST t : parts)
    	{
    		if (!first)
    		{
    			sb.append(" ");
    		}
    		first = false;
    		sb.append(t.toString());
    	}
        return sb.append(")").toString();
    }

    public Expression createExpression(ImmutableList<AST> list)
    {
    	if (list.first() instanceof ASNode)
        {
            ASNode node = (ASNode) list.first();

            switch (node.value)
            {
                case "+":
                case "-":
                case "/":
                case "*":
                case ">":
                case "<":
                case "&":
                case "=":
                case "!":
                    return MathExpression.fromAST(list.rest(), node.value);
                case "lambda":
                    return new Lambda(list.rest().rest().first().asExpression(), Lambda.getArgs(list.rest().first()));
                case "if":
                    return new If(list.rest());
                case "print":
                    return new Print(list.rest());
                case "cons":
                    return new Cons(list.rest());
                case "first":
                    return new First(list.rest());
                case "rest":
                    return new Rest(list.rest());
                case "#def":
                    return Def.getDefn(list.rest());
                case "type":
                    return new Type(list.rest());
                case "#":
                    return new Macro(list.rest().first(), list.rest().rest().first());
                case "load":
                    return new Loader(list.rest().first());
                case "eval":
                    return new Eval(list.rest().first());
                case "sym":
                	return new Sym(list.rest().first());
                default:
                    return new Application(list);

            }
        }

        return new Application(list);
    }
    
    @Override
    public Expression asExpression()
    {
        return createExpression(ImmutableList.fromSTD(parts));
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
        return t == null ? null : t.structureCompare(this);
    }

    @Override
    public ImmutableList<ASTBinding> structureCompare(ASTree t) throws MismatchedRepetitionSizeException
    {
        if (t.parts.size() != parts.size())
        {
            throw new MismatchedRepetitionSizeException();
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
            	AST oldtree = tree;
                tree = macro.macrotize(tree);
                changed |= !tree.equals(oldtree);
            }
        }
        
        boolean tst = changed;
        while(tst)
        {
        	Pair<AST, Boolean> n = tree.applyMacro(macro);
        	tst = n.b;
        	tree = n.a;
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