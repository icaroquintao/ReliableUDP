/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Packet;

/**
 *
 * @author icaroquintao
 */
public class Constants {
    public static final int WINDOW_SIZE = 5;
    public static final int NUMBER_OF_FLAGS = 8;
    public static final int ACK = 0; 
    public static final int DATA = 1;
    public static final int GIVE_ME_LIST = 2;
    public static final int PASSWORD = 3;
    public static final int GIVE_ME_ARCHIVE = 4;
    public static final int SEND_ARCHIVE = 5;
    public static final int END_ARCHIVE = 6;
    public static final int USER_VALIDATED = 7;
    public static final double LOSS = 0.05;
}
