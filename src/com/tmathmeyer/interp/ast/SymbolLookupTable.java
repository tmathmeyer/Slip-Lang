package com.tmathmeyer.interp.ast;

import java.util.HashMap;

public class SymbolLookupTable
{
	private final static HashMap<String, Integer> table = new HashMap<>();

	public static int lookup(String symbol)
	{
		Integer val = table.get(symbol);
		if (val == null)
		{
			return -1;
		}
		return val;
	}

	public static void registerSymbol(String symbol, int loc)
	{
		table.put(symbol, loc);
	}

	static
	{
		registerSymbol("+", 0);
		registerSymbol("-", 0);
		registerSymbol("/", 0);
		registerSymbol("*", 0);
		registerSymbol(">", 0);
		registerSymbol("<", 0);
		registerSymbol("&", 0);
		registerSymbol("=", 0);
		registerSymbol("!", 0);

		registerSymbol("lambda", 1);
		registerSymbol("let", 2);
		registerSymbol("if", 3);
		registerSymbol("print", 4);
		registerSymbol("cons", 5);
		registerSymbol("first", 6);
		registerSymbol("rest", 7);
		registerSymbol("#def", 8);
		registerSymbol("#sdef", 9);
		registerSymbol("type", 10);
		registerSymbol("#redef", 11);
		registerSymbol("list", 12);
		registerSymbol("match", 13);
		registerSymbol("cond", 14);
	}
}
