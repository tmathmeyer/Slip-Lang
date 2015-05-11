package com.tmathmeyer.interp.ds.mipl;

import com.tmathmeyer.interp.Function;
import com.tmathmeyer.interp.ds.MappingPartial;
import com.tmathmeyer.interp.ds.Partial;

public class SortedMappingImmutablePartialList<E extends Partial<E>> implements MappingImmutablePartialList<E>
{
	private final E elem;
	private final MappingPartial<E> rest;

	public SortedMappingImmutablePartialList(E elem, MappingPartial<E> mappingPartial)
	{
		this.elem = elem;
		this.rest = mappingPartial;
	}

	@Override
	public MappingImmutablePartialList<E> add(E elem)
	{
		switch (unit(elem.compareTo(this.elem)))
		{
			case 1:
				return new SortedMappingImmutablePartialList<E>(elem, rest.add(this.elem));
			case -1:
				return new SortedMappingImmutablePartialList<E>(this.elem, rest.add(elem));
			case 0:
				return new SortedMappingImmutablePartialList<E>(elem, rest);
			default:
				throw new RuntimeException("comparitor is broken!");
		}
	}

	@Override
	public MappingPartial<E> remove(E elem)
	{
		switch (unit(elem.compareTo(this.elem)))
		{
			case 1:
				return new SortedMappingImmutablePartialList<E>(elem, rest.remove(this.elem));
			case -1:
				return new SortedMappingImmutablePartialList<E>(this.elem, rest.remove(elem));
			case 0:
				return rest;
			default:
				throw new RuntimeException("comparitor is broken!");
		}
	}

	@Override
	public boolean has(E elem)
	{
		switch (unit(elem.compareTo(this.elem)))
		{
			case -1:
				return rest.has(elem);
			case 1:
				return false;
			case 0:
				return true;
			default:
				throw new RuntimeException("comparitor is broken!");
		}
	}

	@Override
	public E findPartial(E elem)
	{
		switch (unit(elem.compareTo(this.elem)))
		{
			case -1:
				return rest.findPartial(elem);
			case 1:
				return null;
			case 0:
				return this.elem;
			default:
				throw new RuntimeException("comparitor is broken!");
		}
	}

	public String toString()
	{
		return "[" + elem + rest.asCSV() + "]";
	}

	private int unit(int i)
	{
		if (i < 0)
			return -1;
		if (i > 0)
			return 1;
		return 0;
	}

	@Override
	public String asCSV()
	{
		return ", " + elem + rest.asCSV();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elem == null) ? 0 : elem.hashCode());
		result = prime * result + ((rest == null) ? 0 : rest.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SortedMappingImmutablePartialList<?> other = (SortedMappingImmutablePartialList<?>) obj;
		if (elem == null)
		{
			if (other.elem != null)
				return false;
		} else if (!elem.equals(other.elem))
			return false;
		if (rest == null)
		{
			if (other.rest != null)
				return false;
		} else if (!rest.equals(other.rest))
			return false;
		return true;
	}

	@Override
	public MappingPartial<E> filter(Function<E, Boolean> fxn)
	{
		if (fxn.eval(elem))
		{
			return new SortedMappingImmutablePartialList<E>(elem, rest.filter(fxn));
		}
		return rest.filter(fxn);
	}
}
