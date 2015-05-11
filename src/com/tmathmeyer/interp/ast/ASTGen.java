package com.tmathmeyer.interp.ast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ASTGen
{
	public ASTGen(File file)
	{
		inFile = file;
	}

	public ASTGen()
	{
		this(null);
	}

	private final File inFile;

	public AST generate(String... string)
	{
		if (string.length > 0)
		{
			return stringToAST(CharacterSequence.make(string[0]));
		}
		if (inFile != null)
		{
			BufferedReader reader;
			try
			{
				reader = new BufferedReader(new FileReader(inFile));
				return stringToAST(CharacterSequence.make(reader));
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		return new ASTree();
	}

	public AST stringToAST(CharacterSequence cs)
	{
		while (whitespace(cs))
		{
			cs.pop();
		}
		if (cs.has())
		{
			switch (cs.get())
			{
				case '(':
					cs.pop();
					return stringToASTree(cs);
				default:
					return stringToASTNode(cs);
			}
		}
		return null;
	}

	AST stringToASTree(CharacterSequence cs)
	{
		ASTree tree = new ASTree();

		while (cs.has() && cs.get() != ')')
		{
			tree.getParts().add(stringToAST(cs));
			while (whitespace(cs))
			{
				cs.pop();
			}
		}
		cs.pop();
		return tree;
	}

	AST stringToASTNode(CharacterSequence cs)
	{
		StringBuilder sb = new StringBuilder();
		while (cs.has() && !whitespace(cs) && cs.get() != ')')
		{
			sb.append(cs.pop());
		}
		return new ASNode(sb.toString());
	}

	boolean whitespace(CharacterSequence cs)
	{
		if (cs.has())
		{
			char c = cs.get();
			return c == ' ' || c == '\n' || c == '\t';
		}
		return false;
	}
}
