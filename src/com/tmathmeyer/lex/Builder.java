package com.tmathmeyer.lex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.tmathmeyer.interp.Function.Pair;
import com.tmathmeyer.interp.ast.ASNode;
import com.tmathmeyer.interp.ast.AST;
import com.tmathmeyer.interp.ast.ASTree;
import com.tmathmeyer.interp.ds.EmptyList;
import com.tmathmeyer.interp.macro.Macro;
import com.tmathmeyer.interp.values.ImmutableList;

public class Builder
{
	public Pair<AST, ImmutableList<Token>> fromTokens(ImmutableList<Token> tokens)
	{
		switch (tokens.first().piece.charAt(0))
		{
			case '(':
				Token last = tokens.first();
				tokens = tokens.rest();
				ASTree tree = new ASTree();
				while ((!tokens.isEmpty()) && !tokens.first().piece.equals(")"))
				{
					last = tokens.first();
					Pair<AST, ImmutableList<Token>> res = fromTokens(tokens);
					tree.getParts().add(res.a);
					tokens = res.b;
				}
				if (tokens.isEmpty())
				{
					throw new RuntimeException("expected a ) at col:" + last.col_nom + ", row:" + last.line_num
					        + " but found nothing");
				}
				return new Pair<AST, ImmutableList<Token>>(tree, tokens.rest());
			case ')':
				System.out.println("error, encountered an unexpected \")\" at col:" + tokens.first().col_nom + ", row:"
				        + tokens.first().line_num);
				break;
			default:
				return new Pair<AST, ImmutableList<Token>>(new ASNode(tokens.first().piece), tokens.rest());
		}

		return new Pair<AST, ImmutableList<Token>>(null, tokens.rest());
	}

	public ImmutableList<AST> runMacros(List<AST> trees)
	{
		ImmutableList<AST> code = new EmptyList<>();

		List<Macro> macros = new LinkedList<>();
		for (AST t : trees)
		{
			if (t.asExpression().getClass().equals(Macro.class))
			{
				macros.add((Macro) t.asExpression());
			} else
			{
				code = code.add(t);
			}
		}

		ImmutableList<AST> codecmp = code;

		do
		{
			code = codecmp;
			for (Macro m : macros)
			{
				codecmp = m.replace(codecmp);
			}
		} while (!code.toString().equals(codecmp.toString()));

		return code;
	}

	public ImmutableList<AST> fromTokens(List<Token> tokens)
	{
		List<AST> result = new ArrayList<AST>();
		ImmutableList<Token> inp = ImmutableList.fromSTD(tokens);
		while (!inp.isEmpty())
		{
			Pair<AST, ImmutableList<Token>> res = fromTokens(inp);
			if (res.a != null)
			{
				result.add(res.a);
				inp = res.b;
			} else
			{
				throw new RuntimeException("error: malformed tree");
			}

		}

		return runMacros(result);
	}
}