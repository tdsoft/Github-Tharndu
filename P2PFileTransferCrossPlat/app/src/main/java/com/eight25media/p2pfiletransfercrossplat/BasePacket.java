package com.eight25media.p2pfiletransfercrossplat;

import android.support.annotation.IntDef;

/**
 * Created by Admin on 10/25/2016.
 */

public class BasePacket {

    public static final int NONE = -1;
    public static final int INITIAL_PACKET = 0;
    public static final int FILE_PACKET = 1;
    public static final int FINISHED_PACKET = 2;
    public static final int RECEIVED_PACKET = 3;
    public static final int RESEND_PACKET = 4;


    public int packetType = NONE;

}
