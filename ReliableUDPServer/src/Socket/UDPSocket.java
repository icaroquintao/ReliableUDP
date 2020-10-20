/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Socket;

import Packet.Constants;
import Packet.Packet;
import Support.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

/**
 *
 * @author icaroquintao
 */
public class UDPSocket {

    DatagramSocket ds;
    DatagramPacket dp;
    InetAddress ip;
    int port;
    boolean isRecieve;
    boolean isAck;

    public UDPSocket(boolean isRecieve, boolean isAck) {
        boolean worked = false;
        while (!worked) {
            try {
                this.isRecieve = isRecieve;
                this.isAck = isAck;
                if (isAck) {
                    port = 3000;
                } else {
                    port = 1996;
                }
                if (isRecieve) {
                    ds = new DatagramSocket(port);
                } else {
                    ds = new DatagramSocket();
                }
                ip = InetAddress.getByName("192.168.1.108");
                //192.168.1.108 ou 127.0.0.1
                worked = true;
            } catch (UnknownHostException | SocketException ex) {
                worked = false;
            }
        }
    }

    public void send(Packet pkt) {
        boolean flag = true;
        UDPSocket ack = null;
        if (!isRecieve) {
            while (flag) {

                try {

                    //Thread.sleep(50);
                    Random loss = new Random();
                    if (loss.nextFloat() > Constants.LOSS) {
                        //Envia o pacote
                        dp = new DatagramPacket(pkt.toString().getBytes(), pkt.toString().length(), ip, 1996);
                        ds.send(dp);
                        Log.d("Packet " + pkt.getNumber() + " enviado.");

                    } else {
                        Log.d("Packet " + pkt.getNumber() + " perdido.");
                    }
                    //Espera a confirmação (ack)
                    ack = new UDPSocket(true, true);
                    ack.setTimeout(1000);
                    Packet pktAck = ack.recieveAck();

                    if (pktAck == null) {
                        continue;
                    }
                    if (pktAck.getNumber() == pkt.getNumber() || pktAck.getNumber() == 0) {
                        flag = false;
                    }

                } catch (IOException ex) {

                } finally {
                    ack.close();

                }
            }
        } else {
            return;
        }

    }

    public void sendAck(int number) {
        Packet pkt = new Packet(number, Constants.ACK);
        if (isRecieve) {
            return;
        }

        if (!isAck) {
            return;
        }
        boolean flag = true;
        while (flag) {
            try {
                //Thread.sleep(50);
                //Envia a Confirmação (ack)
                Random loss = new Random();
                if (loss.nextFloat() > Constants.LOSS) {
                    dp = new DatagramPacket(pkt.toString().getBytes(), pkt.toString().length(), ip, 3000);
                    ds.send(dp);
                    Log.d("Ack " + pkt.getNumber() + " enviado.");
                    flag = false;
                } else {
                    Log.d("Ack " + pkt.getNumber() + " perdido.");
                }

            } catch (IOException ex) {
                Log.d(ex.toString());

            }
        }

    }

    public Packet recieve() {
        Packet pkt = null;
        if (!isRecieve) {
            return null;
        }

        try {
            byte[] buf = new byte[1500];
            dp = new DatagramPacket(buf, 1500);
            ds.receive(dp);
            String str = new String(dp.getData(), 0, dp.getLength());
            pkt = new Packet(str);
            if (port == 1996) {
                Log.d("Packet " + pkt.getNumber() + " recebido.");
            } else {
                Log.d("Ack " + pkt.getNumber() + "recebido.");
            }

            UDPSocket sendAck = new UDPSocket(false, true);
            sendAck.sendAck(pkt.getNumber());
            sendAck.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return pkt;
    }

    public Packet recieveAck() {
        Packet pkt = null;

        if (!isRecieve) {
            return null;
        }
        while (pkt == null) {
            try {
                byte[] buf = new byte[1460];
                dp = new DatagramPacket(buf, 1460);
                ds.receive(dp);
                String str = new String(dp.getData(), 0, dp.getLength());
                pkt = new Packet(str);
            } catch (IOException ex) {
                Log.d("Ack não recebido, reenviando packet.");
                break;
            }
        }
        return pkt;
    }

    public void setTimeout(int time) {

        try {
            ds.setSoTimeout(time);
        } catch (SocketException ex) {
            Log.d(ex.toString());
        }

    }

    public void close() {
        ds.close();
    }

    public DatagramSocket getDs() {
        return ds;
    }

    public void setDs(DatagramSocket ds) {
        this.ds = ds;
    }

    public DatagramPacket getDp() {
        return dp;
    }

    public void setDp(DatagramPacket dp) {
        this.dp = dp;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isIsRecieve() {
        return isRecieve;
    }

    public void setIsRecieve(boolean isRecieve) {
        this.isRecieve = isRecieve;
    }

    public boolean isIsAck() {
        return isAck;
    }

    public void setIsAck(boolean isAck) {
        this.isAck = isAck;
    }

}
