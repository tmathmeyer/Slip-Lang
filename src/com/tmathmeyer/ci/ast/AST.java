package com.tmathmeyer.ci.ast;

import java.util.LinkedList;
import java.util.List;

import com.tmathmeyer.ci.Application;
import com.tmathmeyer.ci.Cons;
import com.tmathmeyer.ci.Def;
import com.tmathmeyer.ci.Defn;
import com.tmathmeyer.ci.Empty;
import com.tmathmeyer.ci.First;
import com.tmathmeyer.ci.Function;
import com.tmathmeyer.ci.ID;
import com.tmathmeyer.ci.If;
import com.tmathmeyer.ci.Lambda;
import com.tmathmeyer.ci.Print;
import com.tmathmeyer.ci.Real;
import com.tmathmeyer.ci.Rest;
import com.tmathmeyer.ci.Symbol;
import com.tmathmeyer.ci.With;
import com.tmathmeyer.ci.maths.BinaryMathExpression;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.values.ImmutableList;
import com.tmathmeyer.ci.values.Number;

import static com.tmathmeyer.ci.values.ImmutableList.foldl;

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
						return new Lambda(list.rest().rest().first().asExpression(), Lambda.getArgs(list.rest().first()));
					case "+":
					case "-":
					case "*":
					case "/":
						return BinaryMathExpression.fromAST(list.rest(), node.value);
					case "let":
						return new With(list.rest());
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
					default:
						return new Application(list);
						
				}
			}
			
			return new Application(list);
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
				return new Number(Real.parseReal(value));
			}
			catch(Exception e)
			{
				switch(value)
				{
					case "empty":
						return new Empty();
					default:
						return new ID(new Symbol(value));
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
