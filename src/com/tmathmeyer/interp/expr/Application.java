package com.tmathmeyer.interp.expr;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Closure;
import com.tmathmeyer.interp.values.EmptyList;
import com.tmathmeyer.interp.values.ImmutableList;

public class Application implements Expression
{
    private final Expression func;
    private final ImmutableList<Expression> args;

    private Application(Expression function, ImmutableList<Expression> arguments)
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

            ImmutableList<Binding> passOn = new EmptyList<>();

            while (!expTemp.isEmpty())
            {
                try
                {
                    passOn = passOn.add(new Binding(symTemp.first(), expTemp.first().interp(env)));
                }
                catch (RuntimeException re)
                {
                    throw new RuntimeException("failed to add binding in context: [" + this + "] " + cloj.args, re);
                }
                expTemp = expTemp.rest();
                symTemp = symTemp.rest();
            }

            if (!symTemp.isEmpty())
            {
                throw new InvalidArgumentCountException(cloj.args, args);
            }

            return cloj.body.interp(passOn.append(cloj.environment));
        }
        catch (InterpException ide)
        {
            throw new ApplicationEvaluationException(ide, this);
        }
        catch (ClassCastException cce)
        {
            throw new ApplicationEvaluationException(new InvalidFunctionException(func, func.interp(env)), this);
        }
    }

    public String toString()
    {
        String result = "(" + func.toString();
        for (Expression e : args)
        {
            result += (" " + e);
        }
        return result + ")";
    }

    private static class ApplicationEvaluationException extends InterpException
    {
		private static final long serialVersionUID = 1L;
		private final InterpException ide;
        private final Application app;

        private ApplicationEvaluationException(InterpException ide, Application app)
        {
            this.ide = ide;
            this.app = app;
        }

        public void printStackTrace()
        {
            ide.printStackTrace();
            System.out.println("in " + app);
        }
    }
    
    private static class InvalidArgumentCountException extends InterpException
    {
		private static final long serialVersionUID = 1L;
		private final ImmutableList<Symbol> syms;
		private final ImmutableList<Expression> expr;

        private InvalidArgumentCountException(ImmutableList<Symbol> syms, ImmutableList<Expression> expr)
        {
            this.syms = syms;
            this.expr = expr;
        }

        public void printStackTrace()
        {
            System.out.println("expected arguments: "+syms);
            System.out.println("provided arguments: "+expr);
        }
    }

    private static class InvalidFunctionException extends InterpException
    {
		private static final long serialVersionUID = 1L;
		private final Value val;
        private final Expression exp;

        private InvalidFunctionException(Expression exp, Value val)
        {
            this.val = val;
            this.exp = exp;
        }

        public void printStackTrace()
        {
            System.out.println(exp + " is of type " + val.getTypeName() + " and cannot be evaluated");
        }
    }
}