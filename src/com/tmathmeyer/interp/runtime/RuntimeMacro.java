package com.tmathmeyer.interp.runtime;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ast.ASTGen;
import com.tmathmeyer.interp.values.EmptyList;
import com.tmathmeyer.interp.values.ImmutableList;

public abstract class RuntimeMacro
{
	private static ImmutableList<RuntimeMacro> macros = new EmptyList<>();
	
	public abstract ImmutableList<String> getSrc();
	
	static
	{
		macros = macros.add(new BMatch());
		macros = macros.add(new EmptyHuh());
		macros = macros.add(new Define());
		macros = macros.add(new Let());
		macros = macros.add(new List());
	}
	
	protected static ImmutableList<String> asList(String... str)
	{
		ImmutableList<String> result = new EmptyList<>();
		for(String s : str) {
			result = result.add(s);
		}
		return result;
	}
	
	public static ImmutableList<AST> getMacros()
	{
		return ImmutableList.collapse(macros.map(M -> M.getSrc().map(S -> new ASTGen().generate(S))));
	}
}
