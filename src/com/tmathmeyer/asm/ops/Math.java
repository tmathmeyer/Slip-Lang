package com.tmathmeyer.asm.ops;

import com.tmathmeyer.asm.Operation;

public class Math extends Operation
{
    public static enum MathOP
    {
        ADD, SUBTRACT, MULTIPLE, DIVIDE;
    }

    @Override
    public void writeToBytes(byte[] bytes, short indx)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public int getByteSize()
    {
        return 5;
    }
}
