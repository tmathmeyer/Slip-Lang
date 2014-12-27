package com.tmathmeyer.ci.ds.mipl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.ds.Partial;

public class MIPLTest
{

	private static class Block implements Partial<Block>
	{
		private final String name;
		private final int value;

		public Block(String n, int v)
		{
			name = n;
			value = v;
		}

		@Override
		public int compareTo(Block o)
		{
			return name.compareTo(o.name);
		}

		public String toString()
		{
			return "(" + name + "::" + value + ")";
		}
	}

	@Test
	public void test()
	{
		EmptyMappingImmutablePartialList<Block> emipl = new EmptyMappingImmutablePartialList<>();

		MappingPartial<Block> ext = emipl.add(new Block("a", 1));
		ext = ext.add(new Block("b", 2));
		ext = ext.add(new Block("c", 3));
		ext = ext.add(new Block("d", 4));
		ext = ext.add(new Block("e", 5));

		assertEquals(ext.findPartial(new Block("d", 0)).value, 4);

		assertTrue(ext.has(new Block("d", 0)));

		System.out.println(ext);

	}

}
