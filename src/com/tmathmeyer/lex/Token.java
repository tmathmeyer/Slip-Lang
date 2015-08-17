package com.tmathmeyer.lex;

public class Token
{
    final String piece;
    final int line_num;
    final int col_nom;

    Token(String tok, int line, int col)
    {
        piece = tok;
        line_num = line;
        col_nom = col;
    }

    boolean notEmpty()
    {
        return col_nom > -1 && line_num > -1;
    }

    Token append(char c, int l, int co)
    {
        return new Token(piece + c, line_num > -1 ? line_num : l, col_nom > -1 ? col_nom : co);
    }
}
