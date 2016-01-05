package server.pri;
 
import java.net.Socket;
 
public class ClientInfo
{
    public String mKind="Creator";
    public String playerName = "noName";
    public Socket mSocket = null;
    public ClientListener mClientListener = null;
    public ClientSender mClientSender = null;
}