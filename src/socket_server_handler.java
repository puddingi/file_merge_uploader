import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


public class socket_server_handler implements Runnable
{
	public static final int PACKET_TYPE_QUIT_APPLICATION = 0;
	
	private ServerSocket _socket;
	private int _portNumber = 45000;
	private long _sleepTickTime = 2000;
	
	@Override
	public void run()
	{	
		try 
		{
			boolean loop = true;
			_socket = new ServerSocket(_portNumber);
			log_handler.WriteConsole("Start Socket Server");
			
			while (true == loop) 
	        {
	        	Socket listenSocket = _socket.accept();
	        	if(null != listenSocket)
	        	{
	        		InputStream inputStream = listenSocket.getInputStream();
	        		OutputStream outputStream = listenSocket.getOutputStream();
	                 
	        		byte [] readBuffers = new byte[1];
	        		inputStream.read(readBuffers,0,1);
	        		byte packetType = readBuffers[0];
	        		
	        		switch(packetType)
	        		{
	        		case PACKET_TYPE_QUIT_APPLICATION:
	        		{
	        			byte [] writeBuffers = new byte[1];
	        			writeBuffers[0] = PACKET_TYPE_QUIT_APPLICATION;
	        			outputStream.write(writeBuffers);
	        			
	        			work_message_quit_thread workMessageQuitThread = 
	        					new work_message_quit_thread();
	        			
	        			try 
	        			{
	        				work_message.QueueCheck.put(workMessageQuitThread);
	        			} 
	        			catch (InterruptedException e) 
	        			{
	        				// TODO Auto-generated catch block
	        				e.printStackTrace();
	        			}
	        			
	        			loop = false;
	        			
	        		}
	        		break;
	        		}
	        		 
	        		inputStream.close();
	        		outputStream.close();
	                 
	        	}
	        	
				Thread.sleep(_sleepTickTime);
			}
			
			if(null != _socket)
 			{
 				_socket.close();
 				_socket = null;
 			}
			
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		log_handler.WriteConsole("[socket_server_handler::run] - finished");
	}	
}

