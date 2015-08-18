package com.tmathmeyer.lex;

import java.util.ArrayList;
import java.util.List;

import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.reparse.CharacterSequence;

public class Tokenizer
{
    public static ImmutableList<Token> getTokens(CharacterSequence charsec)
    {
        List<Token> result = new ArrayList<>();

        int col = 0, row = 0;
        Token temp = new Token("", -1, -1);
        boolean instring = false;

        while (charsec.has())
        {
            char c = charsec.pop();
            
            switch(c)
            {
                case '"':
                    instring = !instring;
                    temp = temp.append(c, row, col);
                    break;
                default:
                    if (instring)
                    {
                        temp = temp.append(c, row, col);
                    }
                    else
                    {
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
                    }
            }
            col++;
        }
        if (temp.notEmpty())
        {
            result.add(temp);
        }
        return ImmutableList.fromSTD(result);
    }
}
