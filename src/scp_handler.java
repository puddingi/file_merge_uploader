import java.io.IOException;


//download folder - ok
//scp -i Downloads/webrtc-test.pem -r ubuntu@ec2-18-215-148-178.compute-1.amazonaws.com:/home/ubuntu/record/ Downloads/0517/

//upload file - ok
//scp -i Downloads/webrtc-test.pem Downloads/FileMergeUploaderStop.sh ubuntu@ec2-18-215-148-178.compute-1.amazonaws.com:/home/ubuntu/file_merge_uploader/


public class scp_handler 
{
	public static int DownloadFileFromJanus(String sourcePathName,String sourceFileName, String destinationPathName)
	{	
		//scp -i Downloads/webrtc-test.pem ubuntu@ec2-18-215-148-178.compute-1.amazonaws.com:/home/ubuntu/record/development_fNjtHRDMtg_O48PIdv3wx_interview@camera_1557220200000_1557227400000_1557220085402_thinker_Z9WRFXoyUZ_-audio.mjr Downloads/record_files 
	    //scp -i Downloads/webrtc-test.pem ubuntu@ec2-18-215-148-178.compute-1.amazonaws.com:/home/ubuntu/start Downloads/ 
		int resultCode = 0;
	 		
	    Runtime runTime = Runtime.getRuntime();
	    Process process = null;
	    String commandLine = "scp";
	    commandLine += " ";
	    commandLine += "i";
	    commandLine += " ";
	    commandLine += "Downloads/webrtc-test.pem";
	    commandLine += " ";
	    commandLine += "ubuntu@ec2-18-215-148-178.compute-1.amazonaws.com:";
	    commandLine += sourcePathName;//"/home/ubuntu/record/";
	    commandLine += sourceFileName;//"development_fNjtHRDMtg_O48PIdv3wx_interview@camera_1557220200000_1557227400000_1557220085402_thinker_Z9WRFXoyUZ_-audio.mjr";
	    commandLine += " ";
	    commandLine += destinationPathName;//"Downloads/record_files";
	 		 
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
	    				System.out.println("Thread 1 Status : "+streamReaderThread1.getState());
	    				System.out.println("Thread 2 Status : "+streamReaderThread2.getState());
	    	        
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
	 	   
	 	return resultCode;
	}
	
	
	public static int DownloadFolderFromJanus(String sourcePathName, String destinationPathName)
	{	
		//scp -i Downloads/webrtc-test.pem -r ubuntu@ec2-18-215-148-178.compute-1.amazonaws.com:/home/ubuntu/record/ Downloads/
		int resultCode = 0;
	 		
	    Runtime runTime = Runtime.getRuntime();
	    Process process = null;
	    String commandLine = "scp";
	    commandLine += " ";
	    commandLine += "i";
	    commandLine += " ";
	    commandLine += "Downloads/webrtc-test.pem";
	    commandLine += " -r ";
	    commandLine += "ubuntu@ec2-18-215-148-178.compute-1.amazonaws.com:";
	    commandLine += sourcePathName;//"/home/ubuntu/record/";
	    commandLine += " ";
	    commandLine += destinationPathName;//"Downloads/";
	 		 
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
	    				System.out.println("Thread 1 Status : "+streamReaderThread1.getState());
	    				System.out.println("Thread 2 Status : "+streamReaderThread2.getState());
	    	        
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
	 	   
	 	return resultCode;
	}
	
	
	public static int UploadFileToJanus(String sourcePathName,String sourceFileName, String destinationPathName)
	{	
		//scp -i Downloads/webrtc-test.pem Desktop/pant.txt ubuntu@ec2-18-215-148-178.compute-1.amazonaws.com:/home/ubuntu/file_merge_uploader
	    int resultCode = 0;
	 		
	    Runtime runTime = Runtime.getRuntime();
	    Process process = null;
	    String commandLine = "scp";
	    commandLine += " ";
	    commandLine += "i";
	    commandLine += " ";
	    commandLine += "Downloads/webrtc-test.pem";
	    commandLine += " ";
	    commandLine += "ubuntu@ec2-18-215-148-178.compute-1.amazonaws.com:";
	    commandLine += sourcePathName;//"/home/ubuntu/record/";
	    commandLine += sourceFileName;//"development_fNjtHRDMtg_O48PIdv3wx_interview@camera_1557220200000_1557227400000_1557220085402_thinker_Z9WRFXoyUZ_-audio.mjr";
	    commandLine += " ";
	    commandLine += destinationPathName;//"Downloads/record_files";
	 		 
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
	    				System.out.println("Thread 1 Status : "+streamReaderThread1.getState());
	    				System.out.println("Thread 2 Status : "+streamReaderThread2.getState());
	    	        
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
	 	   
	 	return resultCode;
	}
}
