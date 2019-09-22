import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;

//aws configure
//AKIAIKDJCWJ7ZHZWI3TQ
//xtCPfR6oRbSQ5OyYsL8ntZLUW/QUyGUHYjgby/gQ
//ap-northeast-2

//methinks
//interview.videos , methinksfiles
//us-west-2


public class aws_s3_handler
{
	public static ArrayList<String> RetriveFileName(String s3BucketName,String env,String projectId,String requestInterviewId)
	{
		ArrayList<String> retriveFileNameList = null;
		
		Runtime runTime = Runtime.getRuntime();
		Process process = null;
		String commandLine = "aws s3 ls s3://"+s3BucketName+"/"+env+"/"+projectId+"/"+requestInterviewId+"/";
		
		log_handler.WriteConsole("[aws_s3_handler::RetriveFile] - Start : " + commandLine);

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
	    				break;
	    			}
	    		}
	    		
	    		
	    		if(null != streamReaderThread1.Result() && 0 < streamReaderThread1.Result().length())
		    	{
		    		String[] split = streamReaderThread1.Result().split("#@#@#");
		    		for(int countIndex=0; countIndex<split.length; ++countIndex)
		    		{
		    			String[] split2 = split[countIndex].split(" ");
		    			for(int countIndex2=0; countIndex2<split2.length; ++countIndex2)
		    			{
		    				if(split2[countIndex].contains(env))
		    				{
		    					if(null == retriveFileNameList)
		    						retriveFileNameList = new ArrayList<String>();
		    					retriveFileNameList.add(split2[countIndex]);
		    				}
		    				
		    			}
		    			
		    			
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
		
	    log_handler.WriteConsole("-----------------------------------------------------------------------"+System.getProperty("line.separator"));
	    return retriveFileNameList;
	}
	
	
	public static int DownloadFile(String s3BucketName,String env,String projectId,String requestInterviewId,
			ArrayList<String> downloadFileNameList)
	{
		if(null == downloadFileNameList)
			return 1;
		if(0 == downloadFileNameList.size())
			return 1;
		
		for(int countIndex=0; countIndex<downloadFileNameList.size(); ++countIndex)
		{
			
		}
		
		return 0;
	}
	
	
	public static int UploadFile(String s3BucketName,String s3BucketFolderName,String uploadFullPathFileName)
	{
		int resultCode = 0;
		
		Runtime runTime = Runtime.getRuntime();
		Process process = null;
		String commandLine = "aws s3 ls";
		//#copy my-file.txt to the to the "data" directory located in my-s3-bucket 
		commandLine = "aws s3 cp "+uploadFullPathFileName+" s3://"+s3BucketName+"/"+s3BucketFolderName+"/";
		//#copy all files in my-data-dir into the "data" directory located in my-s3-bucket 
		//commandLine = "aws s3 cp apink/ s3://"+s3BucketName+"/data/ --recursive";
		
		//write log file
		File file = null;
		FileOutputStream fileOutputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		try 
		{
			Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();

            long year = calendar.get(Calendar.YEAR);
            long month = calendar.get(Calendar.MONTH)+1;
            long day = calendar.get(Calendar.DAY_OF_MONTH);
            long hour = calendar.get(Calendar.HOUR_OF_DAY);
            long minute = calendar.get(Calendar.MINUTE);
            long second = calendar.get(Calendar.SECOND);
			
            long yearMonthDay = year * 10000 + month * 100 + day;
            long hourMinuteSecond = hour * 10000 + minute * 100 + second;
            String roomid = "roomid";
            
            long nowDateTimeExceptTick = date.getTime() / 1000;
			
			file = new File("result_upload_file_"+yearMonthDay+"_"+hourMinuteSecond+"_"+roomid+".txt");
			fileOutputStream = new FileOutputStream(file,true);
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try 
		{
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8"); 
		} 
		catch (UnsupportedEncodingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		//process
	    try 
	    {
	    	process = runTime.exec(commandLine);
		} 
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
		InputStream inputStream = null;
		BufferedReader bufferReader = null;
			
		//process.getInputStream();
	    inputStream = process.getInputStream();
	    bufferReader = new BufferedReader(new InputStreamReader(inputStream));

	    boolean fileUploadLoop = true;
	    while (true ==  fileUploadLoop) 
	    {
	    	String resultInfo = null;
			try 
			{
				resultInfo = bufferReader.readLine();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	        if(resultInfo == null || resultInfo.equals(""))
	        {
	        	fileUploadLoop = false;
	        	continue;
	        }
	        
	        System.out.println(resultInfo);
	        
	        //upload: apink/apink.mp4 to s3://com.kint.content/data/apink.mp4
	        if(resultInfo.contains("upload:"))
	        {
	        	try 
	        	{
	        		outputStreamWriter.append(resultInfo);
				} 
	        	catch (IOException e) 
	        	{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
	   
	    //
	    try 
	    {
	    	outputStreamWriter.close();
		} 
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return resultCode;
	}
   
	
	public static int UploadMergedFile(merge_work_info mergeWorkInfo)
	{
		String s3BuckName = "";
		String s3BucketFolderName = "";
		if(mergeWorkInfo.GetContent().toLowerCase().contains("interview"))
		{
			s3BuckName = preset.GetInterviewBucketName();
			s3BucketFolderName = 
					mergeWorkInfo.GetEnv()+"/"+mergeWorkInfo.GetProjectID()+"/"+mergeWorkInfo.GetInterviewID();
		}
		else
		{
			s3BuckName = preset.GetAppTestBucketName();
			s3BucketFolderName = 
					mergeWorkInfo.GetEnv()+"/"+mergeWorkInfo.GetProjectID()+"/"+mergeWorkInfo.GetInterviewID();
		}
		
		return UploadFile2(s3BuckName,s3BucketFolderName,
				preset.GetRecordFolderPath() + mergeWorkInfo.GetMergedFileFullName());
	}

	
	public static int UploadConvertedFile(merge_work_info mergeWorkInfo)
	{
		for(int countIndex=0; countIndex<mergeWorkInfo.GetCountRenderMergeInfo(); ++countIndex)
		{
			work_unit workUnit = mergeWorkInfo.GetWorkUnitRenderMergeInfo(countIndex);		
			UploadConvertedFile(workUnit);
		}
		
		return 0;
	}
	
	public static int UploadConvertedFile(work_unit workUnit)
	{
		String s3BuckName = "";
		String s3BucketFolderName = "";
		if(workUnit.Content.toLowerCase().contains("interview"))
		{
			s3BuckName = preset.GetInterviewBucketName();
			s3BucketFolderName = 
					workUnit.Env+"/"+workUnit.Projectid+"/"+workUnit.Interviewid;
		}
		else
		{
			s3BuckName = preset.GetAppTestBucketName();
			s3BucketFolderName = 
					workUnit.Env+"/"+workUnit.Projectid;
		}
		
		if(20L < workUnit.ConvertedVideoFileSize)
		{
			UploadFile2(s3BuckName,s3BucketFolderName,
				preset.GetRecordFolderPath() + workUnit.ConvertedVideoFileFullName);
		}
			
		if(20L < workUnit.ConvertedAudioFileSize)
		{	
			if(true == utility.IsNullOrEmpty(workUnit.ConvertedAudioFileFullName2))
			{
				UploadFile2(s3BuckName,s3BucketFolderName,
						preset.GetRecordFolderPath() + workUnit.ConvertedAudioFileFullName);
			}
			else
			{
				UploadFile2(s3BuckName,s3BucketFolderName,
						preset.GetRecordFolderPath() + workUnit.ConvertedAudioFileFullName2);
			}
		}
		
		return 0;
	}
	
	public static int UploadMuxedFile(work_unit workUnit)
	{
		String s3BuckName = "";
		String s3BucketFolderName = "";
		if(workUnit.Content.toLowerCase().contains("interview"))
		{
			s3BuckName = preset.GetInterviewBucketName();
			s3BucketFolderName = 
					workUnit.Env+"/"+workUnit.Projectid+"/"+workUnit.Interviewid;
		}
		else
		{
			s3BuckName = preset.GetAppTestBucketName();
			s3BucketFolderName = 
					workUnit.Env+"/"+workUnit.Projectid;
		}
		
		UploadFile2(s3BuckName,s3BucketFolderName,
				preset.GetRecordFolderPath() + workUnit.GetMuxedFileFullName());
		
		return 0;
	}
	
	public static int UploadFile2(String s3BucketName,String s3BucketFolderName,String uploadFullPathFileName)
	{
		int resultCode = 0;
		
		Runtime runTime = Runtime.getRuntime();
		Process process = null;
		String commandLine = "aws s3 ls";
		//#copy my-file.txt to the to the "data" directory located in my-s3-bucket 
		
		commandLine = "aws s3 cp "+uploadFullPathFileName+" s3://"+s3BucketName+"/";
		if(null != s3BucketFolderName && 0 < s3BucketFolderName.length())
			commandLine += s3BucketFolderName+"/";
		
		//#copy all files in my-data-dir into the "data" directory located in my-s3-bucket 
		//commandLine = "aws s3 cp apink/ s3://"+s3BucketName+"/data/ --recursive";		
		log_handler.WriteConsole("UploadFile : " + commandLine);

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
		
	    log_handler.WriteConsole("-----------------------------------------------------------------------"+System.getProperty("line.separator"));
	    return resultCode;
	}
	

}
