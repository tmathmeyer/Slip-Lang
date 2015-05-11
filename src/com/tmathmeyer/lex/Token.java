package com.tmathmeyer.lex;

public class Token
{
	public final String piece;
	public final int line_num;
	public final int col_nom;

	public Token(String tok, int line, int col)
	{
		piece = tok;
		line_num = line;
		col_nom = col;
	}

	public boolean notEmpty()
	{
		return col_nom > -1 && line_num > -1;
	}

	public Token append(char c, int l, int co)
	{
		return new Token(piece + c, line_num > -1 ? line_num : l, col_nom > -1 ? col_nom : co);
	}

	@Override
	public String toString()
	{
		return piece;
	}
}
