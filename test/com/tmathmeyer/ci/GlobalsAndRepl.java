package com.tmathmeyer.ci;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.ast.ASTGen;

public class GlobalsAndRepl
{

	@Test
	public void testGetback()
	{
		AST tree = new ASTGen(new File("/home/ted/global.ji")).generate();
		
		Expression plain = tree.asExpression();
    	Expression sansGlucose = plain.desugar();
    	
    	System.out.println(sansGlucose);
		
	}
	
	

}
