package com.tmathmeyer.interp.runtime.lib;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.runtime.SlipRuntime;
import com.tmathmeyer.interp.values.EmptyList;
import com.tmathmeyer.interp.values.ImmutableList;

public class RuntimeLibraries
{
	public static ImmutableList<String> libraries = new EmptyList<>();
	
	static
	{
		libraries = libraries.add("(#def cons? (x) (= (type x) \"List\"))");
		libraries = libraries.add("(#def map (fn l) (if (empty? l) l (cons (fn (first l)) (map fn (rest l)))))");
	}
	
	public static ImmutableList<AST> getLibraries()
    {
        return libraries.map(S -> new SlipRuntime(S).getSyntaxTree().first());
    }
}
