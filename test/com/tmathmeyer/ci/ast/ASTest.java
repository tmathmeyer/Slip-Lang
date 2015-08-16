package com.tmathmeyer.ci.ast;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ast.ASTGen;
import com.tmathmeyer.interp.expr.InterpException;
import com.tmathmeyer.interp.expr.Real;
import com.tmathmeyer.interp.types.Expression;
import com.tmathmeyer.interp.types.Value;
import com.tmathmeyer.interp.values.Number;

public class ASTest
{
	public static Value asValue(int i)
	{
		return new Number(new Real(i));
	}

	@Test
	public void reallisp() throws InterpException
	{
		AST tree = new ASTGen().generate("(+ 99 (+ 1 (+ 2 (+ 3 (+ 4 (+ 5 6))))))");

		Value v = Expression.run(tree);

		assertEquals(v, new Number(new Real(120)));
	}

	@Test
	public void letTest() throws InterpException
	{
		AST tree = new ASTGen().generate("(let ((x 5) (y 6)) (+ x y))");

		Value v = Expression.run(tree);

		assertEquals(v, new Number(new Real(11)));
	}

	@Test
	public void spaces() throws InterpException
	{
		AST tree = new ASTGen().generate("    1      ");

		Value v = Expression.run(tree);

		assertEquals(v, new Number(new Real(1)));
	}

	@Test
	public void morespaces() throws InterpException
	{
		AST tree = new ASTGen().generate("    (  +      1  2   )   ");

		Value v = Expression.run(tree);

		assertEquals(v, new Number(new Real(3)));
	}

	@Test
	public void listTests() throws InterpException
	{
		AST tree = new ASTGen().generate("(let ((X (lambda (y) empty))) (print (X 5)))");

		Value v = Expression.run(tree);

		assertEquals(v, asValue(1));
	}

	@Test
	public void morelistTests() throws InterpException
	{
		AST tree = new ASTGen().generate("(cons 5 empty)");

		Value v = Expression.run(tree);

		assertEquals(v.toString(), "[5]");
	}

	@Test
	public void evenMorelistTests() throws InterpException
	{
		AST tree = new ASTGen().generate("(first (rest (cons 1 (cons 4 (cons 99 (cons 5 empty))))))");

		Value v = Expression.run(tree);

		assertEquals(v, asValue(4));
	}

	@Test
	public void evenMorelistTestsLast() throws InterpException
	{
		AST tree = new ASTGen().generate("(rest (cons 1 (cons 4 (cons 99 (cons 5 empty)))))");

		Value v = Expression.run(tree);

		assertEquals(v.toString(), "[4, 99, 5]");
	}

}
