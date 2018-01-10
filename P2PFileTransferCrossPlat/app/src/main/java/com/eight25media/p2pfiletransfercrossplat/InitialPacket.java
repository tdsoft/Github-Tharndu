package com.eight25media.p2pfiletransfercrossplat;

/**
 * Created by Admin on 10/25/2016.
 */

public class InitialPacket extends BasePacket {
    public String fileName;
    public long fileSize;
    public String packetOrder;


    public InitialPacket(){
        packetType = INITIAL_PACKET;
    }

}
