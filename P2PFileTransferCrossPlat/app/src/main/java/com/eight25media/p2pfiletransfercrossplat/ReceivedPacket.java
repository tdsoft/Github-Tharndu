package com.eight25media.p2pfiletransfercrossplat;

/**
 * Created by Admin on 10/25/2016.
 */

public class ReceivedPacket extends BasePacket {
    public String packetID;
    public ReceivedPacket(){
        packetType = RECEIVED_PACKET;
    }
}
