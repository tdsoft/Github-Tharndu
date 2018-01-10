package com.eight25media.p2pfiletransfercrossplat;

/**
 * Created by Admin on 10/25/2016.
 */

public class ResendPacket extends BasePacket {
    public ResendPacket(){
        packetType = RESEND_PACKET;
    }
}
