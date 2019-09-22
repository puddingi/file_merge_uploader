import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class janus_pp_rec_handler 
{
	public static String Check(String sourcePathFileFullName)
	{
		log_handler.WriteConsole("[janus_pp_rec_handler::Check]- Start : "+sourcePathFileFullName);
		try 
    	{
			Thread.sleep(3000);
		} 
    	catch (InterruptedException e1)
    	{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		//janus-pp-rec --header sourcePathFileFullName 
	    String result = "";
	 		
	    Runtime runTime = Runtime.getRuntime();
	    Process process = null;
	    String commandLine = preset.GetJanusPPRecFilePath()+" --header";
	    commandLine += " ";
	    commandLine += sourcePathFileFullName;
	    	 
	  //process
	    try 
	    {
	    	process = runTime.exec(commandLine);
	    	if(null != process)
	    	{
	    		stream_reader_thread streamReaderThread1 = new stream_reader_thread(process.getInputStream());
	    		stream_reader_thread streamReaderThread2 = new stream_reader_thread(process.getErrorStream());
	    		streamReaderThread1.start();
	    		streamReaderThread2.start();
	    	   
	    		while (true)
	    		{
	    			if (!streamReaderThread1.isAlive() && !streamReaderThread2.isAlive()) 
	    			{  
	    				//System.out.println("Thread 1 Status : "+streamReaderThread1.getState());
	    				//System.out.println("Thread 2 Status : "+streamReaderThread2.getState());
	    	        
	    				process.waitFor();
	    				
	    				
	    				result = streamReaderThread1.Result();
	    				
	    				
	    				log_handler.WriteConsole("[janus_pp_rec_handler::Check] - Process : "+result);
	    				
	    				break;
	    			}
	    		}
	    	}
	    } 
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    catch (InterruptedException e)
	    {
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	if(process != null) 
	    		process.destroy();
	    }
	 	   
	    log_handler.WriteConsole("[janus_pp_rec_handler::Check]- Finish : "+sourcePathFileFullName);
	    log_handler.WriteConsole("---------------------------------------------------------------------");
	 	return result;
	}
	
	
	//
	public static int Convert(String sourcePathFileFullName, String destinationPathFileFullName)
	{
		log_handler.WriteConsole("[janus_pp_rec_handler::Convert]- Start : "+sourcePathFileFullName);
		
		try 
    	{
			Thread.sleep(1000);
		} 
    	catch (InterruptedException e1)
    	{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//janus-pp-rec sourcePathFileFullName destinationPathFileFullName 
	    int resultCode = 0;
	 		
	    Runtime runTime = Runtime.getRuntime();
	    Process process = null;
	    String commandLine = preset.GetJanusPPRecFilePath();
	    commandLine += " ";
	    commandLine += sourcePathFileFullName;
	    commandLine += " ";
	    commandLine += destinationPathFileFullName;
	 		 
	  //process
	    try 
	    {
	    	process = runTime.exec(commandLine);
	    	if(null != process)
	    	{
	    		stream_reader_thread streamReaderThread1 = new stream_reader_thread(process.getInputStream());
	    		stream_reader_thread streamReaderThread2 = new stream_reader_thread(process.getErrorStream());
	    		streamReaderThread1.start();
	    		streamReaderThread2.start();
	    	   
	    		while (true)
	    		{
	    			if (!streamReaderThread1.isAlive() && !streamReaderThread2.isAlive()) 
	    			{  
	    				//System.out.println("Thread 1 Status : "+streamReaderThread1.getState());
	    				//System.out.println("Thread 2 Status : "+streamReaderThread2.getState());
	    				
	    				log_handler.WriteConsole("[janus_pp_rec_handler::Convert] - Process : "+streamReaderThread1.Result());
	    	        
	    				process.waitFor();
	    				resultCode = 1;
	    				break;
	    			}
	    		}
	    	}
	    } 
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    catch (InterruptedException e)
	    {
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	if(process != null) 
	    		process.destroy();
	    }
	 	   
	    log_handler.WriteConsole("[janus_pp_rec_handler::Convert] - Finish : "+sourcePathFileFullName);
	    log_handler.WriteConsole("------------------------------------------------------------------------");
	 	return resultCode;
	}
	
	
}
