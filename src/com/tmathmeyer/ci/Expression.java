package com.tmathmeyer.ci;

import com.tmathmeyer.ci.Function.Pair;
import com.tmathmeyer.ci.Value.Closure;
import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.ast.AST.ASNode;
import com.tmathmeyer.ci.ast.AST.ASTree;
import com.tmathmeyer.ci.ast.ASTGen;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.ds.mipl.EmptyMappingImmutablePartialList;

import static com.tmathmeyer.ci.ImmutableList.map;
import static com.tmathmeyer.ci.ImmutableList.foldl;

/**
 * Created by ted on 12/19/14.
 */
public interface Expression
{
	public Expression desugar();
    public Value interp(MappingPartial<Binding> env);
    
    public static Value run(AST tree)
    {
    	Expression plain = tree.asExpression();
    	Expression sansGlucose = plain.desugar();
    	return sansGlucose.interp(new EmptyMappingImmutablePartialList<>());
    }
    
    public static Expression fromAST(AST syntax)
    {
    	Expression e =  syntax.asExpression();
    	return e;
    }
    
    public static class Print implements Expression
    {
    	private final ImmutableList<Expression> toPrint;
    	
    	public Print(ImmutableList<Expression> out, int dummy)
    	{
    		toPrint = out;
    	}
    	
	    public Print(ImmutableList<AST> rest)
        {
	        this(map(AST.toExpression(), rest), 0);
        }

		@Override
	    public Expression desugar()
	    {
		    return new Print(map(getDesugarer(), toPrint), 0);
	    }

	    @Override
	    public Value interp(MappingPartial<Binding> env)
	    {
	    	ImmutableList<Expression> copy = toPrint;
	    	int count = 0;
	    	while(!copy.isEmpty())
	    	{
	    		count++;
	    		System.out.println(copy.first().interp(env));
	    		copy = copy.rest();
	    	}
	    	return new Value.Number(new Real(count));
	    }
    }
    
    //TODO: marker
    public static class Def implements Expression
    {
    	final Symbol name;
    	final Application args;
    	final Expression body;
    	
		public Def(ImmutableList<AST> rest)
        {
	        name = new Symbol(((ASNode)rest.first()).value);
	        args = (Application) rest.rest().first().asExpression();
	        body = rest.rest().rest().first().asExpression();
        }

		@Override
        public Expression desugar()
        {
			
			ImmutableList<Expression> arglist = args.args;
			arglist = arglist.reverse().add(args.func).reverse();
			Lambda inner = new Lambda(body.desugar(), map(exprToSymbol(), arglist));
			Lambda outer = new Lambda(inner, name);
			
			Application yca = new Application(Y, outer);
			
			
			return yca.desugar();
        }

		@Override
        public Value interp(MappingPartial<Binding> env)
        {
	        throw new RuntimeException("attempting to interp a #def, please desugar first");
        }
    	
    }
    
    public static class Empty implements Expression
    {
		@Override
        public Expression desugar()
        {
	        return this;
        }

		@Override
        public Value interp(MappingPartial<Binding> env)
        {
	        return new ImmutableList.EmptyList<>();
        }
		
		public String toString()
		{
			return "empty";
		}
    }
    
    public static class Cons implements Expression
    {
    	final Expression app;
    	final Expression list;
    	
    	public Cons(Expression a, Expression l)
    	{
    		app = a;
    		list = l;
    	}
    	
		public Cons(ImmutableList<AST> rest)
        {
	        app = rest.first().asExpression();
	        list = rest.rest().first().asExpression();
        }

		@Override
        public Expression desugar()
        {
	        return new Cons(app.desugar(), list.desugar());
        }

		@SuppressWarnings("unchecked")
        @Override
        public Value interp(MappingPartial<Binding> env)
        {
	        return ((ImmutableList<Value>) list.interp(env)).add(app.interp(env));
        }
		
		public String toString()
		{
			return "(cons " + app + " " + list + ")";
		}
    }
    
    public static class Rest implements Expression
    {
    	final Expression list;
    	
    	public Rest(Expression l)
    	{
    		list = l;
    	}
    	
		public Rest(ImmutableList<AST> rest)
        {
	        list = rest.first().asExpression();
        }

		@Override
        public Expression desugar()
        {
	        return new Rest(list.desugar());
        }

		@SuppressWarnings("unchecked")
        @Override
        public Value interp(MappingPartial<Binding> env)
        {
	        return ((ImmutableList<Value>) list.interp(env)).rest();
        }
    }
    
    public static class First implements Expression
    {
    	final Expression list;
    	
    	public First(Expression l)
    	{
    		list = l;
    	}
    	
		public First(ImmutableList<AST> rest)
        {
	        list = rest.first().asExpression();
        }

		@Override
        public Expression desugar()
        {
	        return new First(list.desugar());
        }

		@SuppressWarnings("unchecked")
        @Override
        public Value interp(MappingPartial<Binding> env)
        {
	        return ((ImmutableList<Value>) list.interp(env)).first();
        }
    }

    public static class Number implements Expression
    {
        public final Real value;

        public Number(int n)
        {
            value = new Real(n, 1);
        }

        public Number(Real r)
        {
            value = r;
        }

        @Override
        public Expression desugar()
        {
            return this;
        }

        @Override
        public Value interp(MappingPartial<Binding> env)
        {
            return new Value.Number(value);
        }
        
        public String toString()
        {
        	return value.toString();
        }
    }

    public static class Plus implements Expression
    {
        public final Expression L, R;

        public Plus(Expression left, Expression right)
        {
            L = left;
            R = right;
        }

        @Override
        public Expression desugar()
        {
            return new Plus(L.desugar(), R.desugar());
        }

        @Override
        public Value interp(MappingPartial<Binding> env)
        {
            Value.Number l = (Value.Number) L.interp(env);
            Value.Number r = (Value.Number) R.interp(env);

            return new Value.Number(l.V.add(r.V));
        }
        
        public String toString()
        {
        	return "("+L+" + "+R+")";
        }
    }
    
    public static class Minus implements Expression
    {
        public final Expression L, R;

        public Minus(Expression left, Expression right)
        {
            L = left;
            R = right;
        }

        @Override
        public Expression desugar()
        {
            return new Minus(L.desugar(), R.desugar());
        }

        @Override
        public Value interp(MappingPartial<Binding> env)
        {
            Value.Number l = (Value.Number) L.interp(env);
            Value.Number r = (Value.Number) R.interp(env);

            return new Value.Number(l.V.subtract(r.V));
        }
        
        public String toString()
        {
        	return "("+L+" - "+R+")";
        }
    }

    public static class Mult implements Expression
    {
        public final Expression L, R;

        public Mult(Expression left, Expression right)
        {
            L = left;
            R = right;
        }

        @Override
        public Expression desugar()
        {
            return new Mult(L.desugar(), R.desugar());
        }

        @Override
        public Value interp(MappingPartial<Binding> env)
        {
            Value.Number l = (Value.Number) L.interp(env);
            Value.Number r = (Value.Number) R.interp(env);

            return new Value.Number(l.V.multiply(r.V));
        }
        
        public String toString()
        {
        	return "("+L+" * "+R+")";
        }
    }

    public static class ID implements Expression
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
        public Value interp(MappingPartial<Binding> env)
        {
        	Binding b = env.findPartial(new Binding(I, null));
        	if (b == null) {
        		env.findPartial(new Binding(I, null));
        		System.out.println(env);
        		throw new RuntimeException("failed to lookup: "+ I);
        	}
            return b.val;
        }
        
        public String toString()
        {
        	return I.toString();
        }
    }

    public static class Application implements Expression
    {
        public final Expression func;
        public final ImmutableList<Expression> args;

        public Application(Expression function, Expression ... arguments)
        {
            func = function;
            ImmutableList<Expression> temp = new ImmutableList.EmptyList<Expression>();
            for(Expression e : arguments) {
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
	        args = map(AST.toExpression(), list.rest());
        }

		@Override
        public Expression desugar()
        {
			ImmutableList<Expression> argstemp = args;
			
            return new Application(func.desugar(), map(getDesugarer(), argstemp));
        }
        
        @Override
        public Value interp(MappingPartial<Binding> env)
        {
            Value.Closure cloj = (Closure) func.interp(env);
            
            ImmutableList<Expression> expTemp = args;
            ImmutableList<Symbol> symTemp = cloj.args;
            
            MappingPartial<Binding> passOn = env;
            
            while(!expTemp.isEmpty())
            {
            	passOn = passOn.add(new Binding(symTemp.first(), expTemp.first().interp(env)));
            	expTemp = expTemp.rest();
            	symTemp = symTemp.rest();
            }
            
            if (!symTemp.isEmpty())
            {
            	throw new RuntimeException("mismatched arg length - "+this);
            }
            
            return cloj.body.interp(passOn);
        }
        
        public String toString()
        {
        	return "(" + func + foldl(new Function<Pair<Expression, String>, String>() {
				@Override
                public String eval(com.tmathmeyer.ci.Function.Pair<Expression, String> in)
                {
	                return in.b + " " + in.a;
                }
        	}, args, "")+")";
        }
    }
    
    public static class If implements Expression
    {
        public final Expression conditional, left, right;

        public If(Expression c, Expression l, Expression r)
        {
            conditional = c;
            left = l;
            right = r;
        }

        public If(ImmutableList<AST> rest)
        {
	        conditional = rest.first().asExpression();
	        left = rest.rest().first().asExpression();
	        right = rest.rest().rest().first().asExpression();
        }

		@Override
        public Expression desugar()
        {
            return new If(conditional.desugar(),
            		      left.desugar(),
            		      right.desugar());
        }
        
        @Override
        public Value interp(MappingPartial<Binding> env)
        {
            if (Real.ZERO.equals(((Value.Number)conditional.interp(env)).V))
            {
            	return left.interp(env);
            }
            return right.interp(env);
        }
        
        public String toString()
        {
        	return "(if ("+conditional+"=0) then::"+left+"  else::"+right+")";
        }
    }

    public static class Lambda implements Expression
    {
        public final Expression body;
        public final ImmutableList<Symbol> args;

        public Lambda(Expression bod, Symbol ... arguments)
        {
            body = bod;
            ImmutableList<Symbol> temp = new ImmutableList.EmptyList<Symbol>();
            for(Symbol e : arguments) {
                temp = temp.add(e);
            }
            args = temp;
        }

        public Lambda(Expression function, ImmutableList<Symbol> arguments)
        {
            body = function;
            args = arguments;
        }

        @Override
        public Expression desugar()
        {
            return new Lambda(body.desugar(), args);
        }
        
        @Override
        public Value interp(MappingPartial<Binding> env)
        {
            return new Value.Closure(env, body, args);
        }

		public static ImmutableList<Symbol> getArgs(AST first)
        {
	        if (first instanceof ASNode)
	        {
	        	throw new RuntimeException("these arent args!!");
	        }
	        
	        ASTree tree = (ASTree)first;
	        
	        return ImmutableList.map(AST.toSymbol(), ImmutableList.fromSTD(tree.parts));
        }
		
		public String toString()
        {
        	return "(lambda (" + args.first() + foldl(new Function<Pair<Symbol, String>, String>() {
				@Override
                public String eval(Pair<Symbol, String> in)
                {
	                return in.b + " " + in.a;
                }
        	}, args.rest(), "") + ") " + body + ")";
        }
    }

    public static class With implements Expression
    {
        public final Expression body;
        public final ImmutableList<Defn> bind;

        public With(Expression bod, Defn... args) {
            body = bod;
            ImmutableList<Defn> temp = new ImmutableList.EmptyList<Defn>();
            for (Defn e : args) {
                temp = temp.add(e);
            }
            bind = temp;
        }

        public With(ImmutableList<AST> rest)
        {
	        body = rest.rest().first().asExpression();
	        bind = map(AST.toDefn(), ImmutableList.fromSTD(((AST.ASTree)rest.first()).parts));
        }

		@Override
        public Expression desugar()
        {
            return new Application(new Lambda(body.desugar(), map(defnToSymbol(), bind)),
            		map(defnToExpr(), bind));
        }
        
        @Override
        public Value interp(MappingPartial<Binding> env)
        {
            throw new RuntimeException("you can't interp a with...");
        }
        
        public String toString()
        {
        	return "evaluating -> "+body+"\n"+foldl(new Function<Pair<Defn, String>, String>() {
				@Override
                public String eval(Pair<Defn, String> in)
                {
	                return in.b + "\n" + in.a;
                }
        	}, bind, "");
        }
    }

    public static class Defn
    {
        public final Expression E;
        public final Symbol S;

        public Defn(Expression exp, Symbol sym)
        {
            S = sym;
            E = exp;
        }

		public Defn(AST in)
        {
	        AST.ASTree tree = (ASTree) in;
	        ImmutableList<AST> parts = ImmutableList.fromSTD(tree.parts);
	        ASNode node = (ASNode)parts.first();
	        
	        S = new Symbol(node.value);
	        E = parts.rest().first().asExpression();
        }
		
		public String toString()
		{
			return "<"+S+"::"+E+">";
		}
    }
    
    public static Function<Expression, Expression> getDesugarer()
    {
    	return new Function<Expression, Expression>() {
			@Override
            public Expression eval(Expression in) {
	            return in.desugar();
            }
    	};
    }
    
    public static Function<Defn, Symbol> defnToSymbol()
    {
    	return new Function<Defn, Symbol>() {
            @Override
            public Symbol eval(Defn in) {
                return in.S;
            }
        };
    }
    
    public static Function<Defn, Expression> defnToExpr()
    {
    	return new Function<Defn, Expression>() {
            @Override
            public Expression eval(Defn in) {
                return in.E.desugar();
            }
        };
    }
    
    public static Function<Expression, Symbol> exprToSymbol()
    {
    	return new Function<Expression, Symbol>() {
            @Override
            public Symbol eval(Expression in) {
                return ((ID)in).I;
            }
        };
    }
    
    public static String Y_source = "(lambda (le) ((lambda (f) (f f)) (lambda (f) (le (lambda (x) ((f f) x))))))";
    public static Expression Y = Expression.fromAST(new ASTGen().generate(Y_source));
}
