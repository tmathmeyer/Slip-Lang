package com.tmathmeyer.interp.ds.mipl;

import com.tmathmeyer.interp.Function;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.ds.Partial;

public class EmptyMappingImmutablePartialList<E extends Partial<E>> implements MappingImmutablePartialList<E>
{

	@Override
	public MappingPartial<E> add(E elem)
	{
		return new SortedMappingImmutablePartialList<E>(elem, this);
	}

	@Override
	public MappingPartial<E> remove(E elem)
	{
		return this;
	}

	@Override
	public boolean has(E elem)
	{
		return false;
	}

	@Override
	public E findPartial(E elem)
	{
		return null;
	}

	public String toString()
	{
		return "[]";
	}

	@Override
	public String asCSV()
	{
		return "";
	}

	public boolean equals(Object other)
	{
		return other != null && other instanceof EmptyMappingImmutablePartialList;
	}

	@Override
	public MappingImmutablePartialList<E> filter(Function<E, Boolean> fxn)
	{
		return this;
	}
}
