package com.tmathmeyer.interp.repl;

import java.util.Scanner;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ast.ASTGen;
import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.ImmutableList;

public class Repl
{
	public static void main(String... args)
	{
		new Repl();
	}

	public Repl()
	{
		ImmutableList<Binding> saved = new EmptyList<>();

		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		while (true)
		{
			System.out.print("\n> ");

			try 
			{
				AST tree = new ASTGen().generate(s.nextLine());
				if (tree != null)
				{
					Expression plain = tree.asExpression();
					Expression sansGlucose = plain.desugar();
					Value v = sansGlucose.interp(saved);
	
					System.out.println(v);
	
					if (v instanceof Binding)
					{
						saved = saved.add((Binding) v);
					}
				}
			}
			catch(InterpException e)
			{
				e.printStackTrace();
			}
			
		}
	}
}
