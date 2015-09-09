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
        libraries = libraries.add("(define cons? (x) (= (type x) \"List\"))");
        libraries = libraries.add("(define map (fn l) (if (empty? l) l (cons (fn (first l)) (map fn (rest l)))))");
        libraries = libraries.add("(define fold (fn in ls) (if (empty? ls) in (fold fn (fn in (first ls)) (rest ls)))))");
    }

    public static ImmutableList<AST> getLibraries()
    {
        return libraries.map(S -> new SlipRuntime(S).getSyntaxTree().first());
    }
}
