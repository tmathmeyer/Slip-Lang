package com.tmathmeyer.reparse;

public class Token
{
    final String value;
    final int charPos;
    final int lineNum;
    
    public Token(String value, int cp, int ln)
    {
        this.value = value;
        this.charPos = cp;
        this.lineNum = ln;
    }

    public boolean notEmpty()
    {
        return value.length() > 0;
    }
    
    public String toString()
    {
        return value;
    }

    public int lineNumber()
    {
        return lineNum;
    }

    public int charPos()
    {
        return charPos;
    }
}