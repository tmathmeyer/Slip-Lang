package com.tmathmeyer.ci.ds.mipl;

import com.tmathmeyer.ci.ds.MappingPartial;
import com.tmathmeyer.ci.ds.Partial;

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
		switch(unit(elem.compareTo(this.elem)))
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
		switch(unit(elem.compareTo(this.elem)))
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
		switch(unit(elem.compareTo(this.elem)))
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
		switch(unit(elem.compareTo(this.elem)))
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
		return "["+elem+rest.asCSV()+"]";
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
	    return ", "+elem+rest.asCSV();
    }

}
