package com.tmathmeyer.asm;

public abstract class Operation
{
	public abstract void writeToBytes(byte[] bytes, short indx);

	public abstract int getByteSize();
}
