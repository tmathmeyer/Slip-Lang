package com.tmathmeyer.lex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.tmathmeyer.interp.ast.CharacterSequence;
import com.tmathmeyer.interp.values.ImmutableList;

public class Tokenizer
{
	public static ImmutableList<Token> getTokens(String filepath) throws FileNotFoundException
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
		CharacterSequence charsec = CharacterSequence.make(reader);

		return getTokens(charsec);
	}

	public static ImmutableList<Token> getTokens(CharacterSequence charsec)
	{
		List<Token> result = new ArrayList<>();

		int col = 0, row = 0;
		Token temp = new Token("", -1, -1);
		boolean instring = false;

		while (charsec.has())
		{
			char c = charsec.pop();

			switch (c)
			{
				case '\\':
					if (instring)
					{
						temp = temp.append(charsec.pop(), row, col);
						break;
					}
					throw new RuntimeException("encountered \\ outside of string context");
				case '\n':
					col = 0;
					row++;
					break;
				case '"':
					instring = !instring;
					temp = temp.append('"', row, col);
					break;
				case '(':
				case ')':
					if (temp.notEmpty())
					{
						result.add(temp);
						temp = new Token("", -1, -1);
					}
					result.add(new Token("" + c, row, col));
					break;
				case ' ':
				case '\t':
					if (instring)
					{
						temp = temp.append(c, row, col);
						break;
					}
					if (temp.notEmpty())
					{
						result.add(temp);
						temp = new Token("", -1, -1);
					}
					break;
				default:
					temp = temp.append(c, row, col);
			}
			col++;
		}
		return ImmutableList.fromSTD(result);
	}
}
