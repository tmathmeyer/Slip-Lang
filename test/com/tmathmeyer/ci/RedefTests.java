package com.tmathmeyer.ci;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.use.Language;
import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.lex.Builder;
import com.tmathmeyer.lex.Token;
import com.tmathmeyer.lex.Tokenizer;

public class RedefTests
{
	public ImmutableList<AST> fromFile(String file) throws FileNotFoundException {
		List<Token> toks = Tokenizer.getTokens(file);
		return new Builder().fromTokens(toks);
	}
	
	
	@Test
	public void test() throws FileNotFoundException
	{
		ImmutableList<AST> asts = fromFile("example/redef.jl");
		Language l = new Language(asts);
		
		System.out.println(l.results);
	}
	
	@Test
	public void test2() throws FileNotFoundException
	{
		ImmutableList<AST> asts = fromFile("example/adv_macro.ji");
		Language l = new Language(asts);
		
		System.out.println(l.results);
	}

}
