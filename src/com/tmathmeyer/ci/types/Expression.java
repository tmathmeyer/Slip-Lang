package com.tmathmeyer.ci.types;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.ast.ASTGen;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.ds.mipl.EmptyMappingImmutablePartialList;

/**
 * Created by ted on 12/19/14.
 */
public interface Expression
{
	public Expression desugar();

	public Value interp(MappingPartial<Binding> env);

	public static Value run(AST tree)
	{
		Expression plain = tree.asExpression();
		Expression sansGlucose = plain.desugar();
		return sansGlucose.interp(new EmptyMappingImmutablePartialList<>());
	}

	public static Expression fromAST(AST syntax)
	{
		Expression e = syntax.asExpression();
		return e;
	}

	public static String Y_source = "(lambda (le) ((lambda (f) (f f)) (lambda (f) (le (lambda (x) ((f f) x))))))";
	public static Expression Y = Expression.fromAST(new ASTGen().generate(Y_source));
}
