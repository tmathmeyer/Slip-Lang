package com.tmathmeyer.asm;

import java.io.File;
import java.io.FileNotFoundException;

import com.tmathmeyer.interp.runtime.SlipRuntime;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.values.ImmutableList;

public class SlipCompiler // NO_UCD (unused code)
{
    private final ImmutableList<Expression> exprs;
    
    private SlipCompiler(ImmutableList<Expression> exprs)
    {
        this.exprs = exprs;
    }
    
    public SlipCompiler(File f) throws FileNotFoundException
    {
        this(new SlipRuntime(f).getProgramData());
    }
    
    public SlipCompiler(String s)
    {
        this(new SlipRuntime(s).getProgramData());
    }
    
    public void buildASM()
    {
        //ImmutableList<Binding> bindings = new EmptyList<>();
        //ImmutableList<Expression> ndefs = new EmptyList<>();
        
        for(Expression e : exprs)
        {
            System.out.println(e);
        }
    }
    
    public static void main(String ... args)
    {
        new SlipCompiler("(#def x 2) (#def cc (x) (+ x 2)) (+ 1 2) (cc x)").buildASM();
    }
}
