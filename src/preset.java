
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class preset 
{
	public static final int SERVICE_TYPE_NONE = -1;
	public static final int SERVICE_TYPE_CONVERT = 0;
	public static final int SERVICE_TYPE_CONVERT2 = 1;
	public static final int SERVICE_TYPE_CONVERT_MUX = 2;
	public static final int SERVICE_TYPE_CONVERT_MERGE = 3;
	public static final int SERVICE_TYPE_MUX = 4;
	public static final int SERVICE_TYPE_MERGE_TOPDOWN = 5;
	
	public static int ServiceType = SERVICE_TYPE_CONVERT;
	
	static int __waitCheckFileTickTime = 10000;
	static String __s3InterviewBucketName = "";
	static String __s3AppTestBucketName = "";
	static String __recordFolderPath = "";
	static String __janusPPRecPath = "";
	
	public static boolean Read()
	{
		log_handler.WriteConsole("[preset::Read] - CurrentExecutablePath : " + utility.CurrentExecutablePath());
		
		String presetJsonString = "";
		try
		{
			File file = new File(utility.CurrentExecutablePath()+"/preset.txt");
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine())
            {
            	presetJsonString += scanner.nextLine();
                //System.out.println(scanner.nextLine());
            }
            //System.out.println(scan.useDelimiter("\\z").next());
            scanner.close();
        }
		catch (FileNotFoundException e)
		{
            e.printStackTrace();
            return false;
        }

		log_handler.WriteConsole("[preset::Read] - presetJsonString : "+presetJsonString);
		
		JSONParser presetJsonParser = new JSONParser();
        JSONObject presetJsonObject = null;
		try 
		{
			presetJsonObject = (JSONObject) presetJsonParser.parse(presetJsonString);
		} 
		catch (ParseException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        
        if(presetJsonObject.containsKey("s3_interview_bucket_name"))
        	__s3InterviewBucketName = (String)presetJsonObject.get("s3_interview_bucket_name");
        if("" == __s3InterviewBucketName)
        	return false;
        log_handler.WriteConsole("[preset::Read] - s3_interview_bucket_name : "+__s3InterviewBucketName);
        
        if(presetJsonObject.containsKey("s3_apptest_bucket_name"))
        	__s3AppTestBucketName = (String)presetJsonObject.get("s3_apptest_bucket_name");
        if("" == __s3AppTestBucketName)
        	return false;
        log_handler.WriteConsole("[preset::Read] - s3_apptest_bucket_name : "+__s3AppTestBucketName);

        
        String serviceType = "";
        if(presetJsonObject.containsKey("service_type"))
        	serviceType = (String)presetJsonObject.get("service_type");
        if("" == serviceType)
        	return false;
        log_handler.WriteConsole("[preset::Read] - service_type : "+serviceType);
		
        switch(serviceType)
		{
		case "convert":
		{
			ServiceType = preset.SERVICE_TYPE_CONVERT;
		}
		break;
		
		case "convert2":
		{
			ServiceType = preset.SERVICE_TYPE_CONVERT2;
		}
		break;
		
		case "convert_mux":
		{
			ServiceType = preset.SERVICE_TYPE_CONVERT_MUX;
		}
		break;
		
		case "convert_merge":
		{
			ServiceType = preset.SERVICE_TYPE_CONVERT_MERGE;
		}
		break;
		
		case "mux":
		{
			ServiceType = preset.SERVICE_TYPE_MUX;
		}
		break;
		
		case "merge":
		{
			ServiceType = preset.SERVICE_TYPE_MERGE_TOPDOWN;
		}
		break;
		
		}
        
        if(presetJsonObject.containsKey("record_folder_path"))
        	__recordFolderPath = (String)presetJsonObject.get("record_folder_path");
        if("" == __recordFolderPath)
        	return false;
        log_handler.WriteConsole("[preset::Read] - record_folder_path : "+__recordFolderPath);
        
        
        if(presetJsonObject.containsKey("janus_pp_rec_path"))
        	__janusPPRecPath = (String)presetJsonObject.get("janus_pp_rec_path");
        if("" == __janusPPRecPath)
        	return false;
        log_handler.WriteConsole("[preset::Read] - janus_pp_rec_path : "+__janusPPRecPath);
     
        if(presetJsonObject.containsKey("wait_check_file_tick_time"))
        	__waitCheckFileTickTime = (int)(long)presetJsonObject.get("wait_check_file_tick_time");
        if(0 == __waitCheckFileTickTime)
        	return false;
        log_handler.WriteConsole("[preset::Read] - wait_check_file_tick_time : "+__waitCheckFileTickTime);
        
		
		return true;
	}
	
	
	public static String GetInterviewBucketName()
	{
		return __s3InterviewBucketName;
	}
	
	public static String GetAppTestBucketName()
	{
		return __s3AppTestBucketName;
	}
	
	public static String GetRecordFolderPath()
	{  
		return __recordFolderPath;
	}
	
	public static String GetJanusPPRecFilePath()
	{  
		return __janusPPRecPath;
	}
	
	public static int GetWaitCheckFileTickTime()
	{
		return __waitCheckFileTickTime;
	}

}
