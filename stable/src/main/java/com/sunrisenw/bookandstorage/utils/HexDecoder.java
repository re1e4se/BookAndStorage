package com.sunrisenw.bookandstorage.utils;

import java.nio.ByteBuffer;

public class HexDecoder {

    public static byte[] decode(String hex){

        String[] list=hex.split("(?<=\\G.{2})");
        ByteBuffer buffer= ByteBuffer.allocate(list.length);
        System.out.println(list.length);
        for(String str: list)
            buffer.put(Byte.parseByte(str,16));

        return buffer.array();

    }

}