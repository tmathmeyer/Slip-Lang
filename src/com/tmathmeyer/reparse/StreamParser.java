package com.tmathmeyer.reparse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.tmathmeyer.interp.values.EmptyList;
import com.tmathmeyer.interp.values.ImmutableList;

public class StreamParser
{
    private final BufferedReader br;
    private final boolean isFile;
    
    public StreamParser(InputStream input, boolean isFile)
    {
        this.isFile = isFile;
        this.br = new BufferedReader(new InputStreamReader(input));
    }
    
    public ImmutableList<Token> getTokens()
    {
        ImmutableList<Token> result = new EmptyList<>();
        try
        {
            int line = 0;
            int khar = 0;
            int parens = 0;
            boolean inString = false;
            Token next = null;
            boolean first = true;
            
            int i = 0;
            
            while(  (((i=br.read())!=-1) && isFile)  ||  parens != 0  ||  first)
            {
                char c = (char) i;
                khar++;
                if (!Character.isWhitespace(c))
                {
                    first = false;
                }
                switch(c)
                {
                    case '"':
                        inString = !inString;
                        next = craftToken(next, c, khar, line);
                        break;
                    default:
                        if (inString)
                        {
                            if (c == '\\')
                            {
                                next = craftToken(next, escape(br.read()), khar, line);
                            }
                            else
                            {
                                next = craftToken(next, c, khar, line);
                            }
                        }
                        else
                        {
                            switch(c)
                            {
                                case ')':
                                    parens -=2; // intentional fall through
                                case '(':
                                    parens++;
                                    if (notEmpty(next))
                                    {
                                        result = result.add(next);
                                        next = null;
                                    }
                                    result = result.add(craftToken(null, c, khar, line));
                                    break;
                                case '\n':
                                    line++;
                                    khar=0; // intentional fall through
                                case ' ':
                                case '\t':
                                    if (notEmpty(next))
                                    {
                                        result = result.add(next);
                                        next = null;
                                    }
                                    break;
                                default:
                                    next = craftToken(next, c, khar, line);
                                    break;
                                    
                            }
                        }
                }
            }
            if (notEmpty(next))
            {
                result = result.add(next);
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
            throw new RuntimeException("fuck");
        }
        return result.reverse();
    }

    private char escape(int i)
    {
        switch(i)
        {
            case 'n':
                return '\n';
            case 't':
                return '\t';
            case '\\':
                return '\\';
        }
        throw new RuntimeException("cant find escape sequence: '"+i+"'");
    }

    private Token craftToken(Token t, char c, int pos, int ln)
    {
        if (t == null)
        {
            return new Token(c+"", pos, ln);
        }
        return new Token(t.value+c, t.charPos, t.lineNum);
    }
    
    private boolean notEmpty(Token t)
    {
        return t != null && t.notEmpty();
    }
}
