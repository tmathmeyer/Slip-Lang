package com.tmathmeyer.reparse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.tmathmeyer.interp.values.EmptyList;
import com.tmathmeyer.interp.values.ImmutableList;

public class FileParser
{
    private final BufferedReader file; 
    
    public FileParser(File file) throws FileNotFoundException
    {
        this.file = new BufferedReader(new FileReader(file));
    }
    
    public ImmutableList<LineToken> getLines() throws IOException
    {
        ImmutableList<LineToken> tokens = new EmptyList<>();
        String temp;
        while((temp = file.readLine()) != null)
        {
            tokens = tokens.add(new LineToken(temp, tokens.size()+1));
        }
        return tokens.reverse();
    }
    
    public ImmutableList<Token> getTokens() throws IOException
    {
        ImmutableList<Token> tokens = new EmptyList<>();
        for(ImmutableList<Token> eachLine : getLines().map(LT -> getTokens(LT)))
        {
            tokens = tokens.append(eachLine);
        }
        return tokens;
    }
    
    private ImmutableList<Token> getTokens(LineToken lt)
    {
        ImmutableList<Token> tokens = new EmptyList<>();
        CharacterSequence charseq = CharacterSequence.make(lt.getValue());
        
        boolean inString = false;
        Token next = null;
        while(charseq.has())
        {
            char c = charseq.pop();
            switch(c)
            {
                case '"':
                    inString = !inString;
                    next = craftToken(next, c, charseq.getPosition(), lt.getLineNum());
                    break;
                default:
                    if (inString)
                    {
                        if (c == '\\')
                        {
                            next = craftToken(next, escape(charseq.pop()), charseq.getPosition(), lt.getLineNum());
                        }
                        else
                        {
                            next = craftToken(next, c, charseq.getPosition(), lt.getLineNum());
                        }
                    }
                    else
                    {
                        switch(c)
                        {
                            case ')':
                            case '(':
                                if (notEmpty(next))
                                {
                                    tokens = tokens.add(next);
                                    next = null;
                                }
                                tokens = tokens.add(craftToken(null, c, charseq.getPosition(), lt.getLineNum()));
                                break;
                            case ' ':
                            case '\t':
                                if (notEmpty(next))
                                {
                                    tokens = tokens.add(next);
                                    next = null;
                                }
                                break;
                            default:
                                next = craftToken(next, c, charseq.getPosition(), lt.getLineNum());
                                break;
                                
                        }
                    }
            }
        }
        
        return tokens.reverse();
    }
    
    private char escape(char pop)
    {
        switch(pop)
        {
            case 'n':
                return '\n';
            case 't':
                return '\t';
            case '\\':
                return '\\';
        }
        throw new RuntimeException("cant find escape sequence: '"+pop+"'");
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
    
    
    
    public static class LineToken
    {
        private final String line;
        private final int lineNum;
        
        public LineToken(String line, int num)
        {
            this.line = line;
            this.lineNum = num;
        }
        
        public String getValue()
        {
            return line;
        }
        
        public int getLineNum()
        {
            return lineNum;
        }
    }
    
    
}
