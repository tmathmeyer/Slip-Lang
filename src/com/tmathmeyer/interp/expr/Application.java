package com.tmathmeyer.interp.expr;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Closure;
import com.tmathmeyer.interp.values.EmptyList;
import com.tmathmeyer.interp.values.ImmutableList;

public class Application implements Expression
{
	public final Expression func;
	public final ImmutableList<Expression> args;

	public Application(Expression function, Expression... arguments)
	{
		func = function;
		ImmutableList<Expression> temp = new EmptyList<Expression>();
		for (Expression e : arguments)
		{
			temp = temp.add(e);
		}
		args = temp;
	}

	public Application(Expression function, ImmutableList<Expression> arguments)
	{
		func = function;
		args = arguments;
	}

	public Application(ImmutableList<AST> list)
	{
		func = list.first().asExpression();
		args = list.rest().map(a -> a.asExpression());
	}

	@Override
	public Expression desugar()
	{
		return new Application(func.desugar(), args.map(a -> a.desugar()));
	}

	@Override
	public Value interp(ImmutableList<Binding> env) throws InterpException
	{
		try
		{
			Closure cloj = (Closure) func.interp(env);
			ImmutableList<Expression> expTemp = args;
			ImmutableList<Symbol> symTemp = cloj.args;

			ImmutableList<Binding> passOn = env;

			while (!expTemp.isEmpty())
			{
				try
				{
					passOn = passOn.add(new Binding(symTemp.first(), expTemp.first().interp(env)));
				} catch (RuntimeException re)
				{
					throw new RuntimeException("failed to add binding in context: [" + this + "] " + cloj.args, re);
				}
				expTemp = expTemp.rest();
				symTemp = symTemp.rest();
			}

			if (!symTemp.isEmpty())
			{
				throw new RuntimeException("mismatched arg length - " + this);
			}

			return cloj.body.interp(passOn);
		}
		catch(InterpException ide)
		{
			throw new ApplicationEvaluationException(ide, this);
		}
		catch(ClassCastException cce)
		{
			throw new ApplicationEvaluationException(new InvalidFunctionException(func, func.interp(env)), this);
		}
	}

	public String toString()
	{
		String result = "("+func.toString();
		for(Expression e : args)
		{
			result+=(" " + e);
		}
		return result+")";
	}
	
	public static class ApplicationEvaluationException extends InterpException
	{
		private final InterpException ide;
		private final Application app;

		public ApplicationEvaluationException(InterpException ide, Application app)
        {
	        this.ide = ide;
	        this.app = app;
        }

		public void printStackTrace()
		{
			ide.printStackTrace();
			System.out.println("in "+app);
		}
	}
	
	public static class InvalidFunctionException extends InterpException
	{
		private final Value val;
		private final Expression exp;

		public InvalidFunctionException(Expression exp, Value val)
        {
	        this.val = val;
	        this.exp = exp;
        }

		public void printStackTrace()
		{
			System.out.println(exp+" is of type "+val.getTypeName()+" and cannot be evaluated");
		}
	}
}