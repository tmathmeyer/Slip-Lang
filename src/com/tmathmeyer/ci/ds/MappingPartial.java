package com.tmathmeyer.ci.ds;


public interface MappingPartial<E extends Partial<E>>
{
	MappingPartial<E> add(E elem);
	MappingPartial<E> remove(E elem);
	boolean has(E elem);
	E findPartial(E elem);
	String asCSV();
	
}
