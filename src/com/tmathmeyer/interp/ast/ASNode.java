package com.tmathmeyer.interp.ast;

import java.util.LinkedList;
import java.util.List;

import com.tmathmeyer.interp.expr.Function.Pair;
import com.tmathmeyer.interp.expr.ID;
import com.tmathmeyer.interp.expr.Real;
import com.tmathmeyer.interp.expr.Symbol;
import com.tmathmeyer.interp.list.Empty;
import com.tmathmeyer.interp.macro.Macro;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.values.EmptyList;
import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.interp.values.Number;
import com.tmathmeyer.interp.values.Str;

public class ASNode implements AST
{
	public String value;

	public ASNode(String v)
	{
		value = v;
	}

	public String toString()
	{
		return value;
	}

	@Override
	public Expression asExpression()
	{
		if (isStringString(value))
		{
			return new Str(value.subSequence(1, value.length() - 1));
		}
		try
		{
			return new Number(Real.parseReal(value));
		} catch (Exception e)
		{
			if (value.equals("empty"))
			{
				return new Empty();
			}
			return new ID(new Symbol(value));
		}
	}

	private boolean isStringString(String string)
	{
		int length = string.length();
		return length > 0 && string.charAt(0) == '"' && string.charAt(length - 1) == '"';
	}

	@Override
	public String name()
	{
		return toString();
	}

	@Override
	public AST toRepeatingTree()
	{
		return this;
	}

	@Override
	public ImmutableList<ASTBinding> structureCompare(AST t) throws MismatchedRepetitionSizeException
	{
		return t.structureCompare(this);
	}

	@Override
	public ImmutableList<ASTBinding> structureCompare(ASTree t) throws MismatchedRepetitionSizeException
	{
		return new EmptyList<ASTBinding>().add(new ASNodeBinding(this, t));
	}

	@Override
	public ImmutableList<ASTBinding> structureCompare(ASNode t) throws MismatchedRepetitionSizeException
	{
		return new EmptyList<ASTBinding>().add(new ASNodeBinding(this, t));
	}

	@Override
	public ImmutableList<ASTBinding> structureCompare(RepeatingAST t) throws MismatchedRepetitionSizeException
	{
		return new EmptyList<ASTBinding>().add(new ASNodeBinding(this, t));
	}

	@Override
	public ASTBinding bindTo(AST bNext)
	{
		return new ASNodeBinding(this, bNext);
	}

	@Override
	public List<AST> getParts()
	{
		List<AST> res = new LinkedList<>();
		res.add(this);
		return res;
	}

	@Override
	public AST applyBindings(ImmutableList<ASTBinding> comp)
	{
		for (ASTBinding ab : comp)
		{
			if (ab.getClass().equals(ASNodeBinding.class))
			{
				if (((ASNodeBinding) ab).getFrom().toString().equals(value))
				{
					return ((ASNodeBinding) ab).getTo();
				}
				if (((ASNodeBinding) ab).getTo().toString().equals(value))
				{
					return ((ASNodeBinding) ab).getFrom();
				}
			} else
			{
				AST res = this.applyBindings(ab.asList());
				if (!res.toString().equals(this.toString()))
				{
					return res;
				}
			}
		}
		return this;
	}

	@Override
	public Pair<AST, Boolean> applyMacro(Macro macro)
	{
		if (value.equals(macro.getName()))
		{
			return new Pair<>(macro.macrotize(this), true);
		}

		return new Pair<>(this, false);
	}

	@Override
	public AST hasMacro(String name)
	{
		return null;
	}

	@Override
    public boolean isMacro()
    {
	    return value.equals("#");
    }

	@Override
    public int hashCode()
    {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((value == null) ? 0 : value.hashCode());
	    return result;
    }

	@Override
    public boolean equals(Object obj)
    {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    ASNode other = (ASNode) obj;
	    if (value == null)
	    {
		    if (other.value != null)
			    return false;
	    } else if (!value.equals(other.value))
		    return false;
	    return true;
    }
}