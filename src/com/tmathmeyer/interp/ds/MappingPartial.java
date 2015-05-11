package com.tmathmeyer.interp.ds;

import com.tmathmeyer.interp.Function;
import com.tmathmeyer.interp.ds.mipl.EmptyMappingImmutablePartialList;
import com.tmathmeyer.interp.values.ImmutableList;

public interface MappingPartial<E extends Partial<E>>
{
	MappingPartial<E> add(E elem);

	MappingPartial<E> remove(E elem);

	boolean has(E elem);

	E findPartial(E elem);

	String asCSV();
	
	MappingPartial<E> filter(Function<E, Boolean> fxn);

	public static <E extends Partial<E>> MappingPartial<E> fromImmutableList(ImmutableList<E> list)
	{
		MappingPartial<E> result = new EmptyMappingImmutablePartialList<E>();
		list = list.reverse();

		while (!list.isEmpty())
		{
			result = result.add(list.first());
			list = list.rest();
		}

		return result;
	}
}
