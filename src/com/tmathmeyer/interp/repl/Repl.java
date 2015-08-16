package com.tmathmeyer.interp.repl;

import java.util.Scanner;

import com.tmathmeyer.interp.expr.Binding;
import com.tmathmeyer.interp.runtime.SlipRuntime;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.EmptyList;
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

			SlipRuntime slip = new SlipRuntime(s.nextLine(), saved);
			Value v = slip.evaluate().first();
			System.out.println(v);
			saved = slip.getBindings();
		}
	}
}
