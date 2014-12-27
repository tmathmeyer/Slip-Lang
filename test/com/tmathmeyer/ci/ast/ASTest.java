package com.tmathmeyer.ci.ast;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.tmathmeyer.ci.Real;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.types.Value;
import com.tmathmeyer.ci.values.Number;

public class ASTest
{
	public static Value asValue(int i)
	{
		return new Number(new Real(i));
	}

	@Test
	public void stringtest()
	{
		assertEquals(new ASTGen().generate("hello"), new AST.ASNode("hello"));
	}

	@Test
	public void treetest()
	{
		assertEquals(new ASTGen().generate("(hello)"), new AST.ASTree(new AST.ASNode("hello")));
	}

	@Test
	public void lambda()
	{
		assertEquals(new ASTGen().generate("(lambda x y)"), new AST.ASTree(new AST.ASNode("lambda"),
		        new AST.ASNode("x"), new AST.ASNode("y")));
	}

	@Test
	public void reallisp()
	{
		AST tree = new ASTGen().generate("(+ 99 (+ 1 (+ 2 (+ 3 (+ 4 (+ 5 6))))))");

		Value v = Expression.run(tree);

		assertEquals(v, new Number(new Real(120)));
	}

	@Test
	public void letTest()
	{
		AST tree = new ASTGen().generate("(let ((x 5) (y 6)) (+ x y))");

		Value v = Expression.run(tree);

		assertEquals(v, new Number(new Real(11)));
	}

	@Test
	public void spaces()
	{
		AST tree = new ASTGen().generate("    1      ");

		Value v = Expression.run(tree);

		assertEquals(v, new Number(new Real(1)));
	}

	@Test
	public void morespaces()
	{
		AST tree = new ASTGen().generate("    (  +      1  2   )   ");

		Value v = Expression.run(tree);

		assertEquals(v, new Number(new Real(3)));
	}

	@Test
	public void fileRead()
	{
		AST tree = new ASTGen(new File("/home/ted/first.ji")).generate();

		Value v = Expression.run(tree);

		assertEquals(v, new Number(new Real(1)));
	}

	@Test
	public void listTests()
	{
		AST tree = new ASTGen().generate("(let ((X (lambda (y) empty))) (print (X 5)))");

		Value v = Expression.run(tree);

		assertEquals(v, asValue(1));
	}

	@Test
	public void morelistTests()
	{
		AST tree = new ASTGen().generate("(cons 5 empty)");

		Value v = Expression.run(tree);

		assertEquals(v.toString(), "[5]");
	}

	@Test
	public void evenMorelistTests()
	{
		AST tree = new ASTGen().generate("(first (rest (cons 1 (cons 4 (cons 99 (cons 5 empty))))))");

		Value v = Expression.run(tree);

		assertEquals(v, asValue(4));
	}

	@Test
	public void evenMorelistTestsLast()
	{
		AST tree = new ASTGen().generate("(rest (cons 1 (cons 4 (cons 99 (cons 5 empty)))))");

		Value v = Expression.run(tree);

		assertEquals(v.toString(), "[4, 99, 5]");
	}

}
