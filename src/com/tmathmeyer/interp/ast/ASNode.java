package com.tmathmeyer.interp.ast;

import java.util.LinkedList;
import java.util.List;

import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.expr.ID;
import com.tmathmeyer.interp.expr.Real;
import com.tmathmeyer.interp.expr.Symbol;
import com.tmathmeyer.interp.list.Empty;
import com.tmathmeyer.interp.macro.Macro;
import com.tmathmeyer.interp.types.Expression;
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
	public AST applyMacro(Macro macro)
	{
		if (value.equals(macro.getName()))
		{
			return macro.macrotize(this);
		}

		return this;
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
}