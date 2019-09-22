import java.io.IOException;

public class ffprobe_handler
{
	public int FileResolutionWidth;
	public int FileResolutionHeight;
	
    public boolean GetFileResolution(String fileFullPathName)
    {	
        //ffprobe -v error -select_streams v:0 -show_entries stream=width,height -of default=nw=1 input.mp4
    	boolean result = false;
    	
    	Runtime runTime = Runtime.getRuntime();
        Process process = null;
        
        String commandLine = utility.CurrentExecutablePath()+"/ffprobe";
        commandLine += " ";
        commandLine += "-v error -select_streams v:0 -show_entries stream=width,height -of default=nw=1";
        commandLine += " ";
        commandLine += fileFullPathName;

        //process
	    try 
	    {
	    	process = runTime.exec(commandLine);
	    	
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
	    	        break;
	    	    }
	    	}
	    	
	    	if(null != streamReaderThread1.Result() && 0 < streamReaderThread1.Result().length())
	    	{
	    		String[] split = streamReaderThread1.Result().split("#@#@#");
	    		for(int countIndex=0; countIndex<split.length; ++countIndex)
	    		{
	    			if(null == split[countIndex] || 0 == split[countIndex].length())
	    				continue;
	    			
	    			String[] split2 = split[countIndex].split("=");
	                switch (split2[0])
	                {
	                    case "width":
	                        {
	                        	FileResolutionWidth = Integer.parseInt(split2[1]);
	                        }
	                        break;

	                    case "height":
	                        {
	                            FileResolutionHeight = Integer.parseInt(split2[1]);
	                        }
	                        break;
	                }	
	    		}
	    		
	    		result = true;
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

        return result; 
    }
    

    public static float GetFileDurationSecondTime(String fileFullPathName)
    {
        //ffprobe a1280.mp4 -show_entries format=duration -of compact=p=0:nk=1 -v 0    	
        float fileDurationSecondTime = 0.0f;
        
        Runtime runTime = Runtime.getRuntime();
        Process process = null;
        //String commandLine = "/media/psf/Home/file_merge_uploader/ffprobe";
        
        String commandLine = utility.CurrentExecutablePath()+"/ffprobe";
        commandLine += " ";
        
        commandLine += fileFullPathName;
        commandLine += " ";
        commandLine += "-show_entries format=duration -of compact=p=0:nk=1 -v 0";
        
        //process
	    try 
	    {
	    	process = runTime.exec(commandLine);
	    	
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
	    	        break;
	    	    }
	    	}
	    	
	    	if(null != streamReaderThread1.Result() && 0 < streamReaderThread1.Result().length())
	    	{
	    		log_handler.WriteConsole("[ffprobe_handler::GetFileDurationSecondTime] - "+fileFullPathName+" : "+streamReaderThread1.Result());
	    		
	    		String[] split = streamReaderThread1.Result().split("#@#@#");
	    		for(int countIndex=0; countIndex<split.length; ++countIndex)
	    		{
	    			if(null == split[countIndex] || 0 == split[countIndex].length())
	    				continue;
	    			
	    			if(!split[countIndex].equals("N/A") && !split[countIndex].equals("n/a"))
	    			{
	    				fileDurationSecondTime = Float.parseFloat(split[countIndex]);
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
        
        return fileDurationSecondTime;
    }
 }
