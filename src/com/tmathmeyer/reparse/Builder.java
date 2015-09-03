package com.tmathmeyer.reparse;

import java.util.ArrayList;
import java.util.List;

import com.tmathmeyer.interp.ast.ASNode;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ast.ASTree;
import com.tmathmeyer.interp.ast.Pair;
import com.tmathmeyer.interp.values.ImmutableList;

public class Builder
{
    private final ImmutableList<AST> tree;

    public Builder(ImmutableList<Token> tokens)
    {
        tree = fromTokens(tokens);
    }

    public ImmutableList<AST> syntaxTrees()
    {
        return tree;
    }

    private Pair<AST, ImmutableList<Token>> singleFromTokens(ImmutableList<Token> tokens)
    {
        switch (tokens.first().toString().charAt(0))
        {
            case '(':
                Token last = tokens.first();
                tokens = tokens.rest();
                ASTree tree = new ASTree();
                while ((!tokens.isEmpty()) && !tokens.first().toString().equals(")"))
                {
                    last = tokens.first();
                    Pair<AST, ImmutableList<Token>> res = singleFromTokens(tokens);
                    tree.getParts().add(res.a);
                    tokens = res.b;
                }
                if (tokens.isEmpty())
                {
                    throw new RuntimeException("expected a ) at col:" + last.charPos() + ", row:" + last.lineNumber()
                            + " but found nothing");
                }
                return new Pair<AST, ImmutableList<Token>>(tree, tokens.rest());
            case ')':
                System.out.println("error, encountered an unexpected \")\" at col:" + tokens.first().charPos()
                        + ", row:" + tokens.first().lineNumber());
                break;
            default:
                return new Pair<AST, ImmutableList<Token>>(new ASNode(tokens.first().toString()), tokens.rest());
        }

        return new Pair<AST, ImmutableList<Token>>(null, tokens.rest());
    }

    private ImmutableList<AST> fromTokens(ImmutableList<Token> tokens)
    {
        List<AST> result = new ArrayList<AST>();
        while (!tokens.isEmpty())
        {
            Pair<AST, ImmutableList<Token>> res = singleFromTokens(tokens);
            if (res.a != null)
            {
                result.add(res.a);
                tokens = res.b;
            } else
            {
                throw new RuntimeException("error: malformed tree");
            }

        }

        return ImmutableList.fromSTD(result);
    }
}