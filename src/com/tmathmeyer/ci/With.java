package com.tmathmeyer.ci;

import static com.tmathmeyer.ci.values.ImmutableList.foldl;
import static com.tmathmeyer.ci.values.ImmutableList.map;

import com.tmathmeyer.ci.Function.Pair;
import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;
import com.tmathmeyer.ci.values.ImmutableList;

public class With implements Expression
{
    public final Expression body;
    public final ImmutableList<Defn> bind;

    public With(Expression bod, Defn... args) {
        body = bod;
        ImmutableList<Defn> temp = new EmptyList<Defn>();
        for (Defn e : args) {
            temp = temp.add(e);
        }
        bind = temp;
    }

    public With(ImmutableList<AST> rest)
    {
        body = rest.rest().first().asExpression();
        bind = map(AST.toDefn(), ImmutableList.fromSTD(((AST.ASTree)rest.first()).parts));
    }

	@Override
    public Expression desugar()
    {
        return new Application(new Lambda(body.desugar(),
        		map(ImmutableList.defnToSymbol(), bind)),
        		map(ImmutableList.defnToExpr(), bind));
    }
    
    @Override
    public Value interp(MappingPartial<Binding> env)
    {
        throw new RuntimeException("you can't interp a with...");
    }
    
    public String toString()
    {
    	return "evaluating -> "+body+"\n"+foldl(new Function<Pair<Defn, String>, String>() {
			@Override
            public String eval(Pair<Defn, String> in)
            {
                return in.b + "\n" + in.a;
            }
    	}, bind, "");
    }
}