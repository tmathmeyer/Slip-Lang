package com.tmathmeyer.interp;

import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public class ID implements Expression
{
	public final Symbol I;

	public ID(Symbol s)
	{
		I = s;
	}

	@Override
	public Expression desugar()
	{
		return this;
	}

	@Override
	public Value interp(ImmutableList<Binding> env) throws InterpException
	{
		Binding b =  env.filter(B -> B.name.equals(I)).first();
		if (b == null)
		{
			throw new IDLookUpException(I);
		}
		return b.val;
	}

	public String toString()
	{
		return I.toString();
	}
	
	public static class IDLookUpException extends InterpException
	{
		private final Symbol s;
		public void printStackTrace()
		{
			System.out.println("Cannot find Symbol: "+s);
		}
		
		public IDLookUpException(Symbol i)
		{
			s = i;
		}
	}
}