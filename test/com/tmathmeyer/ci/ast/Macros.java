package com.tmathmeyer.ci.ast;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.tmathmeyer.ci.Expression;
import com.tmathmeyer.ci.Real;
import com.tmathmeyer.ci.Value;

public class Macros
{

	@Test
	public void test()
	{
		AST tree = new ASTGen(new File("/home/ted/macro.ji")).generate();
		
		Value v = Expression.run(tree);
		
		assertEquals(v, new Value.Number(new Real(5)));
	}
	
	@Test
	public void macrolists()
	{
		AST tree = new ASTGen(new File("/home/ted/macrolists.ji")).generate();
		
		Value v = Expression.run(tree);
		
		assertEquals(v.toString(), "[10, 9, 8, 7, 6, 5, 4, 3, 2, 1]");
	}

}
