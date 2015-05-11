package com.tmathmeyer.ci;

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
	public ImmutableList<AST> fromFile(String file) throws FileNotFoundException
	{
		List<Token> toks = Tokenizer.getTokens(file);
		return new Builder().fromTokens(toks);
	}

	@Test
	public void test() throws FileNotFoundException
	{
		ImmutableList<AST> asts = fromFile("example/lists.ji");
		new Language(asts);
	}

}
