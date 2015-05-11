package com.tmathmeyer.interp.runtime;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ast.ASTGen;
import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.values.ImmutableList;

public abstract class RuntimeMacro
{
	private static ImmutableList<RuntimeMacro> macros = new EmptyList<>();
	
	public abstract String getSrc();
	
	static
	{
		macros = macros.add(new Cond());
		macros = macros.add(new EmptyHuh());
	}
	
	public static ImmutableList<AST> getMacros()
	{
		return macros.map(a -> a.getSrc()).map(a -> new ASTGen().generate(a));
	}
}
