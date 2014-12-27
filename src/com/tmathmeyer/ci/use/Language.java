package com.tmathmeyer.ci.use;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.tmathmeyer.ci.Binding;
import com.tmathmeyer.ci.DefSans;
import com.tmathmeyer.ci.Function;
import com.tmathmeyer.ci.ast.AST;
import com.tmathmeyer.ci.ast.ASTGen;
import com.tmathmeyer.ci.ast.CharacterSequence;
import com.tmathmeyer.ci.ds.EmptyList;
import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.ds.mipl.EmptyMappingImmutablePartialList;
import com.tmathmeyer.ci.types.Expression;
import com.tmathmeyer.ci.values.ImmutableList;

public class Language
{
	public static void main(String... args) throws FileNotFoundException
	{
		//String filepath = "example/global.ji"; // TODO make this read from args
		String filepath = "example/fib.ji"; // TODO make this read from args

		BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
		CharacterSequence charsec = CharacterSequence.make(reader);

		ImmutableList<AST> trees = new EmptyList<AST>();
		AST next;

		while ((next = new ASTGen().stringToAST(charsec)) != null)
		{
			trees = trees.add(next);
		}

		ImmutableList<Expression> los = ImmutableList.map(new Function<AST, Expression>() {
			@Override
			public Expression eval(AST in)
			{
				return in.asExpression().desugar();
			}
		}, trees);

		ImmutableList<Expression> defs = ImmutableList.filter(new Function<Expression, Boolean>() {

			@Override
			public Boolean eval(Expression in)
			{
				return in instanceof DefSans;
			}

		}, los);

		ImmutableList<Expression> ndefs = ImmutableList.filter(new Function<Expression, Boolean>() {

			@Override
			public Boolean eval(Expression in)
			{
				return !(in instanceof DefSans);
			}

		}, los);

		ImmutableList<Binding> binds = ImmutableList.map(new Function<Expression, Binding>() {

			@Override
			public Binding eval(Expression in)
			{
				Binding bi = (Binding) in.interp(new EmptyMappingImmutablePartialList<>());
				return bi;
			}

		}, defs);

		MappingPartial<Binding> bindings = MappingPartial.fromImmutableList(binds);

		while (!ndefs.isEmpty())
		{
			ndefs.first().interp(bindings);
			ndefs = ndefs.rest();
		}

	}
}
