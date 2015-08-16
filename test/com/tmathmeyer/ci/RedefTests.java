package com.tmathmeyer.ci;

import java.io.FileNotFoundException;

import org.junit.Test;

import com.tmathmeyer.interp.InterpException;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.runtime.SlipRuntime;
import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.lex.Builder;
import com.tmathmeyer.lex.Tokenizer;

public class RedefTests
{
	public ImmutableList<AST> fromFile(String file) throws FileNotFoundException
	{
		return new Builder(Tokenizer.getTokens(file)).syntaxTrees();
	}

	@Test
	public void test() throws FileNotFoundException, InterpException
	{
		SlipRuntime.main("example/work.jl");
	}

}
