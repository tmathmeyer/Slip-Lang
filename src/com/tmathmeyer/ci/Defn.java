package com.tmathmeyer.ci;

import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.ast.AST.ASNode;
import com.tmathmeyer.ci.ast.AST.ASTree;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.values.ImmutableList;

public class Defn
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