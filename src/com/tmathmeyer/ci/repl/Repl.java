package com.tmathmeyer.ci.repl;

import java.util.Scanner;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.ast.ASTGen;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.ds.mipl.EmptyMappingImmutablePartialList;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;

public class Repl
{
	public static void main(String ... args)
	{
		new Repl();
	}
	
	
	public Repl()
	{
		MappingPartial<Binding> saved = new EmptyMappingImmutablePartialList<>();

		@SuppressWarnings("resource")
        Scanner s = new Scanner(System.in);
		while(true) {
			System.out.print("\n> ");
			
			AST tree = new ASTGen().generate(s.nextLine());
			
			Expression plain = tree.asExpression();
	    	Expression sansGlucose = plain.desugar();
	    	Value v =  sansGlucose.interp(saved);
			
			System.out.println(v);
			
			if (v instanceof Binding)
			{
				saved = saved.add((Binding) v);
			}
		}
	}
}
