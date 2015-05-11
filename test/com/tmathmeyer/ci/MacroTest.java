package com.tmathmeyer.ci;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ast.CharacterSequence;
import com.tmathmeyer.interp.use.Language;
import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.interp.values.Number;
import com.tmathmeyer.lex.Builder;
import com.tmathmeyer.lex.Tokenizer;

public class MacroTest
{

	@Test
	public void evals() throws FileNotFoundException
	{
		String input = "(+ 1 2)";
		
		ImmutableList<AST> astcollection = new Builder().fromTokens(Tokenizer.getTokens(CharacterSequence.make(input)));

		Language l = new Language(astcollection);
		
		assertEquals(l.results.first(), new Number(3));
	}

}
