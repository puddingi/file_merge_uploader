import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class utility 
{	
	public static ArrayList<File> SearchedFileList;
	
	
	public static void WriteFileTest2()
	{
		try
        {
			final File file = new File("sample_text_file.txt");
			final FileWriter  writer = new FileWriter(file, false);
		
			Timer workTimer = new Timer();
			workTimer.scheduleAtFixedRate(new TimerTask() 
			{
				@Override
				public void run() 
				{
    		  
					try
					{
						writer.write("This is a sample message.\n");
						//writer.flush();	//update file create time 
    	            
						System.out.println("done");
					} 
					catch(IOException e) 
					{
						e.printStackTrace();
					} 
					finally 
					{
    	           
					}
    		  
				}
			}, 0,1000);
        }
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static void CheckFileClosed2()
	{
		final File file = new File("sample_text_file.txt");
			
		Timer workTimer = new Timer();
		workTimer.scheduleAtFixedRate(new TimerTask() 
		{
			@Override
			public void run() 
			{
				System.out.println("FileClosed : "+utility.IsFileClosed(file.getAbsolutePath()));
			}
		}, 5000,1000);
    }
	
	
	
	public static void WriteFileTest(String fullPathFileName,String message,boolean appendMode)
    {
    	if(null == fullPathFileName || 0 == fullPathFileName.length())
    		fullPathFileName = "sample_text_file.txt";
    	
    	if(null == message || 0 == message.length())
    		message = "This is a sample message.\n";
    
        File file = new File(fullPathFileName);
        FileWriter writer = null;
        
        try
        {
            writer = new FileWriter(file, appendMode);
            writer.write(message);
            writer.flush();
            
            System.out.println("done");
        } 
        catch(IOException e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            try 
            {
                if(writer != null) 
                	writer.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        }
   }
	
	public static int RenameFile(String oldFileFullPathName,String newFileFullPathName)
	{
		//mv old-file-name new-file-name
		
		int resultCode = 0;
		
		Runtime runTime = Runtime.getRuntime();
		Process process = null;
		String commandLine = "mv";
		commandLine += " ";
		commandLine += oldFileFullPathName;
		commandLine += " ";
		commandLine += newFileFullPathName;
		
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
	    				//Sy/stem.out.println("Thread 2 Status : "+streamReaderThread2.getState());
	    	        
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
			resultCode = -1;
		}
	    catch (InterruptedException e)
	    {
	    	e.printStackTrace();
	    	resultCode = -2;
	    }
	    finally
	    {
	    	if(process != null) 
	    		process.destroy();
	    }
		
		return resultCode;
	}
	
	
	public static int DeleteFolderOrFile(String destinationPathFullName,boolean folder)
    {
		int resultCode = 0;
		
		Runtime runTime = Runtime.getRuntime();
		Process process = null;
		String commandLine = "rm";
		commandLine += " ";
		
		if(true == folder)
			commandLine += "-r";
		else
			commandLine += "-f";
		
		commandLine += " ";
		commandLine += destinationPathFullName;
		
		if(true == folder)
			commandLine += "/";
		
		
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
	    				//Sy/stem.out.println("Thread 2 Status : "+streamReaderThread2.getState());
	    	        
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
			resultCode = -1;
		}
	    catch (InterruptedException e)
	    {
	    	e.printStackTrace();
	    	resultCode = -2;
	    }
	    finally
	    {
	    	if(process != null) 
	    		process.destroy();
	    }
		
		return resultCode;
    }
	
	public static ArrayList<File> GetListFilesForFolder(String findPath,String[] filterExtends) 
	{
		boolean writeLog = false;
		boolean writeConsole = false;
		
		//System.out.println("GetListFilesForFolder - findPath : "+findPath);
		
		final ArrayList<File> searchedFileList = new ArrayList<File>();
		final File searchFolder = new File(findPath);
		_GetListFilesForFolder(searchedFileList,searchFolder,filterExtends);
		
		if(null != searchedFileList)
		{
			log_handler.WriteConsole("[utility::GetListFilesForFolder] - Count : "+searchedFileList.size());
			
			if(true == writeLog)
			{
				for(int countIndex=0; countIndex<searchedFileList.size(); ++countIndex)
				{
					File searchFile = (File)searchedFileList.get(countIndex);
					String searchFileAbsolutePath = searchFile.getAbsolutePath();
					String searchFileName = searchFile.getName();
					String searchFilePath = searchFile.getPath();
				
					log_handler.WriteConsole("GetListFile["+countIndex+"/"+searchedFileList.size()+"] - "+
							"AbsolutePath : "+searchFileAbsolutePath+" - " + 
							"Name : "+searchFileName+" - "+
							"Path : "+searchFilePath);
				}
			}
		}
		
		return searchedFileList;
	}
	
	
	static void _GetListFilesForFolder(final ArrayList<File> searchedFileList,final File folder,String [] filterExtends) 
	{
		File[] files = folder.listFiles();
		if(null == files || 0 == files.length)
		{
			//System.out.println("_GetListFilesForFolder - null == files");
			return;
		}
		
	    for (int countIndex=0; countIndex<files.length; ++ countIndex) 
	    {
	    	final File file = files[countIndex];
	        if (file.isDirectory()) 
	        {
	        	_GetListFilesForFolder(searchedFileList,file,filterExtends);
	        } 
	        else 
	        {	
	        	for(int countIndex2 = 0; countIndex2 < filterExtends.length; ++ countIndex2)
	        	{
	        		if(true == file.getName().toLowerCase().contains(filterExtends[countIndex2]))
	        			searchedFileList.add(file);
	        	}
	        }
	    }
	}
	
	public static long GetFileCreateDateTime(String fullPathFileName)
    {
		// stat -c %y a.mjr
   	 	long unixTimeStamp = 0L;
	 		
	    Runtime runTime = Runtime.getRuntime();
	    Process process = null;
	    String commandLine = "stat";
	    commandLine += " ";
	    commandLine += "-c %y";
	    commandLine += " ";
	    commandLine += fullPathFileName;
	 		 
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
	    				
	    				String dateTime = streamReaderThread1.Result().replace("#@#@#", "");
	    				String [] splits = dateTime.split(" ");
	    				log_handler.WriteConsole("GetFileCreateDateTime : "+dateTime);
	    				
	    				String [] times = splits[1].split("\\.");
	    				
	    				String gmtTime = splits[2];
	    				gmtTime = gmtTime.substring(0, 3) + ":" + gmtTime.substring(3, gmtTime.length());
	    				
	    				
	    				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    				dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"+gmtTime));
	    				
	    				try 
	    				{
	    					unixTimeStamp = dateFormat.parse(splits[0]+" "+times[0]).getTime();
						} 
	    				catch (ParseException e) 
	    				{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    				
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
	 	   
	    return unixTimeStamp;
    }
	
	
	public static long GetFileSize(String fullPathFileName)
    {
		log_handler.WriteConsole("[utility::GetFileSize] - Start : "+fullPathFileName);
		
		// stat -f %z  a.mjr
   	 	long fileSize = 0L;
	 		
	    Runtime runTime = Runtime.getRuntime();
	    Process process = null;
	    String commandLine = "stat";
	    commandLine += " ";
	    commandLine += "-c %s";
	    commandLine += " ";
	    commandLine += fullPathFileName;
	 		 
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
	    				
	    				log_handler.WriteConsole("[utility::GetFileSize] - result : "+streamReaderThread1.Result());
	    				
	    				String fileSizeResult = streamReaderThread1.Result().replace("#@#@#", "");
	    				String [] splits = fileSizeResult.split(" ");
	    				log_handler.WriteConsole("[utility::GetFileSize] - filesize "+fileSizeResult);
	    				
	    				if(!fileSizeResult.equals("N/A") && !fileSizeResult.equals("n/a"))
	    					fileSize = Long.parseLong(fileSizeResult);
	    				
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
	 	   
	    log_handler.WriteConsole("[utility::GetFileSize] - Finish : "+fullPathFileName);
	    log_handler.WriteConsole("--------------------------------------------------------------------------------------------------------------------");
	    return fileSize;
    }
	
	
	public static long IsFileClosedGetFileSize(String filePathFullName) 
	{
		if(true == IsFileClosed(filePathFullName))
			return GetFileSize(filePathFullName);
		
		return 0L;
	}
	
	
	public static boolean IsFileClosed(String fileFullPathName)
	{
		try 
    	{
			Thread.sleep(3000);
		} 
    	catch (InterruptedException e1)
    	{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Process process = null;
		BufferedReader bufferedReader = null;
		
	    try 
	    {
	    	process = new ProcessBuilder(new String[]{"lsof", "|", "grep", fileFullPathName}).start();
	    	bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        String line;
	        while((line=bufferedReader.readLine())!=null) 
	        {
	            if(line.contains(fileFullPathName)) 
	            {                            
	            	bufferedReader.close();
	                process.destroy();
	                return false;
	            }
	        }
	    } catch(Exception ex)
	    {
	        ex.printStackTrace();
	    }
	    
	    
	    if(null != bufferedReader)
		try 
	    {
			bufferedReader.close();
		}
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    if(null != process)
	    	process.destroy();
	    
	    return true;
	}
	
	public static boolean IsNullOrEmpty(String checkString)
	{
		if(null != checkString && 0 < checkString.length())
			return false;
		return true;
	}
	
	
	public static long GetDurationSecondsTime(long startEpochMilli,long finishEpochMilli)
	{
		long startEpochSecond = Instant.ofEpochMilli(startEpochMilli).getEpochSecond();
		long finishEpochSecond = Instant.ofEpochMilli(finishEpochMilli).getEpochSecond();
		long durationSecondsTime = finishEpochSecond - startEpochSecond;
		
		return durationSecondsTime;
	}
	
	
	public static long GetFinishEpochMilli(long startEpochMilli,long durationSecondsTime)
	{
		if(0L == durationSecondsTime)
			return 0L;
		
		Instant finishEpochMilliInstant =
				Instant.ofEpochMilli(startEpochMilli).plusSeconds(durationSecondsTime);
		return finishEpochMilliInstant.toEpochMilli();
	}
	
	static int _GetWidthHeightRatio(int width,int height) 
	{
	    int war = 0;
	    int har = 0;
		int gcd = 0;
		int temp = 0;
		int max = 0;
		int min = 0;
		int rwidth = width; 
		int rheight = height;      
		
		if(rwidth < rheight)
		{    
			max = rwidth;           
			min = rheight; 
		}
		else
		{ 
			max = rheight;
			min = rwidth; 
		}
		
		while(max % min != 0)
		{   
			temp = max % min;    
			max = min;        
			min = temp;      
			gcd = min;      
			
			 war = rwidth/gcd;      
			 har = rheight/gcd;     
		}
		
		return 0;
	}
	
	public static String CurrentExecutablePath()
	{
		return System.getProperty("user.dir");
	}
	
	public static String NewLine()
	{
		return System.getProperty("line.separator");
	}
	
	public static String CurrentOS()
	{
		return System.getProperty("os.name").toLowerCase();
	}
	
	
	public static void CreateDirectory(String directoryPath)
	{
		File directory = new File(directoryPath);
	    if (!directory.exists())
	    {
	        directory.mkdir();
	    }
	}
	
	
}
