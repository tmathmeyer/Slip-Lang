package com.tmathmeyer.reparse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import com.tmathmeyer.interp.values.ImmutableList;
import com.tmathmeyer.lex.Token;
import com.tmathmeyer.lex.Tokenizer;

public interface CharacterSequence
{
    static class BasicCharacterSequence implements CharacterSequence
    {
        private char[] seq;
        private int index;

        private BasicCharacterSequence(String s)
        {
            seq = s.toCharArray();
            index = 0;
        }

        @Override
        public void incr()
        {
            index++;
        }

        @Override
        public char get()
        {
            return seq[index];
        }

        @Override
        public char pop()
        {
            return seq[index++];
        }

        @Override
        public boolean has()
        {
            return index < seq.length;
        }

        @Override
        public int getPosition()
        {
            return index;
        }
    }

    static class FileCharacterSequence implements CharacterSequence
    {
        private Reader r;
        private char[] buffer = new char[128];
        private int bufferpos = 0;
        private int max = 0;
        private int total = 0;

        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            for (int i = bufferpos; i < 128; i++)
            {
                sb.append(buffer[i]);
            }
            sb.append("...");
            return sb.toString();
        }

        private FileCharacterSequence(BufferedReader reader)
        {
            r = reader;
            try
            {
                max = r.read(buffer);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void incr()
        {
            total++;
            bufferpos++;
            if (bufferpos >= max && max >= 0)
            {
                bufferpos = 0;
                try
                {
                    max = r.read(buffer);
                    if (max > 0)
                    {
                        for (int i = max; i < 128; i++)
                        {
                            buffer[i] = 0;
                        }
                    } else
                    {
                        for (int i = 0; i < 128; i++)
                        {
                            buffer[i] = 0;
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean has()
        {
            return bufferpos >= 0 && bufferpos < max;
        }

        @Override
        public char pop()
        {
            char c = buffer[bufferpos];
            incr();
            return c;
        }

        @Override
        public char get()
        {
            return buffer[bufferpos];
        }

        @Override
        public int getPosition()
        {
            return total;
        }

    }

    void incr();

    boolean has();

    char pop();

    char get();
    
    int getPosition();

    public static CharacterSequence make(String string)
    {
        return new BasicCharacterSequence(string);
    }

    public static CharacterSequence make(BufferedReader reader)
    {
        return new FileCharacterSequence(reader);
    }

    public default ImmutableList<Token> asTokens()
    {
        return Tokenizer.getTokens(this);
    }
}
