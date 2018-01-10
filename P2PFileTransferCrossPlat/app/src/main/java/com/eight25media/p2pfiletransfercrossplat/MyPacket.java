package com.eight25media.p2pfiletransfercrossplat;

/**
 * Created by Admin on 10/25/2016.
 */

public class MyPacket  extends BasePacket {
    public String packetId;
    public byte[] packetData;

    public MyPacket(){
        packetType = FILE_PACKET;
    }
}
