package com.tmathmeyer.ci.ast;

import java.util.LinkedList;
import java.util.List;

import com.tmathmeyer.ci.Expression;
import com.tmathmeyer.ci.Function;
import com.tmathmeyer.ci.ImmutableList;
import com.tmathmeyer.ci.Real;
import com.tmathmeyer.ci.Symbol;
import com.tmathmeyer.ci.Expression.Defn;

import static com.tmathmeyer.ci.ImmutableList.foldl;

public interface AST
{
	
	Expression asExpression();
	
	public class ASTree implements AST
	{
		public List<AST> parts = new LinkedList<AST>();

		@Override
        public int hashCode()
        {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + ((parts == null) ? 0 : parts.hashCode());
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
	        ASTree other = (ASTree) obj;
	        if (parts == null)
	        {
		        if (other.parts != null)
			        return false;
	        } else if (!parts.equals(other.parts))
		        return false;
	        return true;
        }
		
		public ASTree(AST ... parts)
		{
			for(AST tt : parts)
			{
				this.parts.add(tt);
			}
		}
		
		public String toString()
		{
			return foldl(new Function<Function.Pair<AST, String>, String>(){

				@Override
                public String eval(Function.Pair<AST, String> in)
                {
					return in.b + " " + in.a.toString();
                }
				
			}, ImmutableList.fromSTD(parts), "(")+")";
		}

		@Override
        public Expression asExpression()
        {
			ImmutableList<AST> list = ImmutableList.fromSTD(parts);
			if (list.first() instanceof ASNode)
			{
				ASNode node = (ASNode) list.first();
				
				switch(node.value)
				{
					case "lambda":
						return new Expression.Lambda(list.rest().rest().first().asExpression(), Expression.Lambda.getArgs(list.rest().first()));
					case "+":
						return new Expression.Plus(list.rest().first().asExpression(), list.rest().rest().first().asExpression());
					case "-":
						return new Expression.Minus(list.rest().first().asExpression(), list.rest().rest().first().asExpression());
					case "*":
						return new Expression.Mult(list.rest().first().asExpression(), list.rest().rest().first().asExpression());
					case "let":
						return new Expression.With(list.rest());
					case "if":
						return new Expression.If(list.rest());
					case "print":
						return new Expression.Print(list.rest());
					case "cons":
						return new Expression.Cons(list.rest());
					case "first":
						return new Expression.First(list.rest());
					case "rest":
						return new Expression.Rest(list.rest());
					case "#def":
						return new Expression.Def(list.rest());
					default:
						return new Expression.Application(list);
						
				}
			}
			
			return new Expression.Application(list);
        }

		/*
		@Override
        public AST macrOperate()
        {
			ImmutableList<AST> list = ImmutableList.fromSTD(parts);
			if (list.first() instanceof ASNode)
			{
				ASNode node = (ASNode) list.first();
				
				switch(node.value)
				{
					case "#def":
						return makeLambda(list.rest().first(), list.rest().rest());
						
				}
			}
			
			return null;
        }
        

		private AST makeLambda(AST first, ImmutableList<AST> rest)
        {
	        String name = ((ASNode) first).value;
	        ASTree arglist = (ASTree)rest.first();
	        AST body = rest.rest().first();
	        
	        ASTree result = new ASTree();
	        result.parts.add(new ASNode("lambda"));
	        while(body.hasNode(name))
	        {
	        	name = "_"+name;
	        }
	        arglist.parts.add(new ASNode(name));
	        
        }
		*/
	}
	
	public class ASNode implements AST
	{
		public String value;
		
		public ASNode(String v) {
			value = v;
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
		
		public String toString()
		{
			return value;
		}

		@Override
        public Expression asExpression()
        {
			try
			{
				return new Expression.Number(Real.parseReal(value));
			}
			catch(Exception e)
			{
				switch(value)
				{
					case "empty":
						return new Expression.Empty();
					default:
						return new Expression.ID(new Symbol(value));
				}
			}
        }
		
		public static final ASNode LAMBDA = new ASNode("lambda");
		public static final ASNode LET = new ASNode("let");
		public static final ASNode IF = new ASNode("if");
		public static final ASNode PLUS = new ASNode("+");
		public static final ASNode MuLT = new ASNode("*");
	}

	static public Function<AST, Expression> toExpression()
	{
		return new Function<AST, Expression>() {

			@Override
            public Expression eval(AST in)
            {
	            return in.asExpression();
            }
			
		};
	}
	
	static public Function<AST, Symbol> toSymbol()
	{
		return new Function<AST, Symbol>(){
			@Override
            public Symbol eval(AST in)
            {
                if (in instanceof ASNode)
                {
                	return new Symbol(((ASNode)in).value);
                }
                throw new RuntimeException("not an arg!");
            }
        };
	}
	
	static public Function<AST, Defn> toDefn()
	{
		return new Function<AST, Defn>(){

			@Override
            public Defn eval(AST in)
            {
                return new Defn(in);
            }
        	
        };
	}
}
