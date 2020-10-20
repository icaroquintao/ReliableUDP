/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Packet;

import java.io.Serializable;

/**
 *
 * @author icaroquintao
 */
public class Packet implements Serializable, Comparable{
    int[] flags;
    String body;
    int number;
    
    public Packet(int number){
        this.number = number;
        this.flags = new int[Constants.NUMBER_OF_FLAGS];
        for(int i=0; i <flags.length;i++)
            flags[i] = 0;
    }
    
    public Packet(int number, int flag){
        this.number = number;
        this.flags = new int[Constants.NUMBER_OF_FLAGS];
        for(int i=0; i <flags.length;i++)
            flags[i] = 0;
        flags[flag] = 1;
    }
    
    public Packet (String packet){
        this.body = packet.split("}")[1];
        this.flags = new int[Constants.NUMBER_OF_FLAGS];
        String flag = packet.split("}")[0];
        for(int i=0; i <flags.length;i++)
            flags[i] = Integer.parseInt(flag.split(" ")[i]);
        
        try {
            number = Integer.parseInt(packet.split("}")[2]);
        } catch (Exception e) {
            number = 0;
        }
    }

    public int[] getFlags() {
        return flags;
    }

    public void setFlags(int[] flags) {
        this.flags = flags;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getNumber() {
        return this.number;
    }
    
    @Override
    public String toString(){
        String retorno ="";
        for(int i=0; i< flags.length;i++){
            retorno+= flags[i];
            retorno +=" ";
        }
        return retorno + "}" +body + "}"+number;
    }
    
    public boolean equalsTo(Packet anotherPacket){
        return this.number == anotherPacket.getNumber();
    }

    @Override
    public int compareTo(Object o) {
            Packet another = (Packet)o;
            Integer x = this.number;
            Integer y = another.getNumber();
            return x.compareTo(y);
    }


}
