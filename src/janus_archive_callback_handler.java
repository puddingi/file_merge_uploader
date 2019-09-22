import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class janus_archive_callback_handler 
{
	public static void RequestUpdateConvert(work_unit workUnit)
	{
		if(work_unit.CHECK_FILE_SIZE < workUnit.ConvertedVideoFileSize)
		{
			_RequestUpdateConvert(workUnit,true);
		}
			
		if(work_unit.CHECK_FILE_SIZE < workUnit.ConvertedAudioFileSize)
		{
			_RequestUpdateConvert(workUnit,false);
		}
	}
	
	static void _RequestUpdateConvert(work_unit workUnit,boolean video)
	{
		String s3Url = "https://s3.amazonaws.com/";
		boolean debugMode = false;
		if(workUnit.Env.toLowerCase().contains("dev"))
		{
			debugMode = true;
		}
		
		String type = "";
		if(workUnit.Content.toLowerCase().contains("interview"))
		{
			type = "interview";
			s3Url += preset.GetInterviewBucketName() + "/";
			s3Url += workUnit.Env+"/"+workUnit.Projectid+"/"+workUnit.Interviewid+"/"; 
		}
		else
		{
			type = "apptest";
			s3Url += preset.GetAppTestBucketName() + "/";
			s3Url += workUnit.Env+"/"+workUnit.Projectid+"/"; 
		}
		if(true == video)
			s3Url += workUnit.ConvertedVideoFileFullName;
		else
		{
			if(true == utility.IsNullOrEmpty(workUnit.ConvertedAudioFileFullName2))
			{
				s3Url += workUnit.ConvertedAudioFileFullName;
			}
			else
			{
				s3Url += workUnit.ConvertedAudioFileFullName2;
			}
		}
		
		//
		String videoType = "";
		if(workUnit.Content.toLowerCase().contains("screen"))
			videoType = "screen";
		else
			videoType = "camera";
		
		String fileType = "";
		long fileSize = 0L;
		if(true == video)
		{
			fileType = "video";
			fileSize = workUnit.ConvertedVideoFileSize;
		}
		else
		{
			fileType = "audio";
			fileSize = workUnit.ConvertedAudioFileSize;
		}
		
		_RequestServer(debugMode,type,"status",workUnit.Interviewid,workUnit.GetStartEpochMilli(),
				workUnit.UserCode,fileType,videoType,workUnit.GetFinishEpochMilli(),
				(int)workUnit.GetDurationSecondsTime(),fileSize,s3Url);
	}
	
	public static void RequestUpdateMux(work_unit workUnit)
	{
		String s3Url = "https://s3.amazonaws.com/";
		boolean debugMode = false;
		if(workUnit.Env.toLowerCase().contains("dev"))
		{
			debugMode = true;
		}
		
		String type = "";
		if(workUnit.Content.toLowerCase().contains("interview"))
		{
			type = "interview";
			s3Url += preset.GetInterviewBucketName() + "/";
			s3Url += workUnit.Env+"/"+workUnit.Projectid+"/"+workUnit.Interviewid+"/"; 
		}
		else
		{
			type = "apptest";
			s3Url += preset.GetAppTestBucketName() + "/";
			s3Url += workUnit.Env+"/"+workUnit.Projectid+"/"; 
		}
		s3Url += workUnit.GetMuxedFileFullName();
		
		//
		String videoType = "";
		if(workUnit.Content.toLowerCase().contains("screen"))
			videoType = "screen";
		else
			videoType = "camera";
		
		String fileType = "mux";
		long fileSize = workUnit.MuxedFileSize;
		
		_RequestServer(debugMode,type,"status",workUnit.Interviewid,workUnit.GetStartEpochMilli(),
				workUnit.UserCode,fileType,videoType,workUnit.GetFinishEpochMilli(),
				(int)workUnit.GetDurationSecondsTime(),fileSize,s3Url);
	}
	
	public static void RequestUpdateMerge(merge_work_info mergeWorkInfo)
	{
		String s3Url = "https://s3.amazonaws.com/";
		boolean debugMode = false;
		if(mergeWorkInfo.GetEnv().toLowerCase().contains("dev"))
		{
			debugMode = true;
		}
		
		String videoType = "";
		String type = "";
		if(mergeWorkInfo.GetContent().toLowerCase().contains("interview"))
		{
			type = "interview";
			s3Url += preset.GetInterviewBucketName() + "/";
			s3Url += mergeWorkInfo.GetEnv()+"/"+mergeWorkInfo.GetProjectID()+"/"+mergeWorkInfo.GetInterviewID()+"/"; 
		}
		else
		{
			type = "apptest";
			s3Url += preset.GetAppTestBucketName() + "/";
			s3Url += mergeWorkInfo.GetEnv()+"/"+mergeWorkInfo.GetProjectID()+"/"; 
			
			if(mergeWorkInfo.GetContent().contains("screen"))
				videoType = "screen";
			else
				videoType = "camera";
		}
		s3Url += mergeWorkInfo.GetMergedFileFullName();
		
		String fileType = "merge";
		long fileSize = mergeWorkInfo.GetMergedFileSize();
		
		_RequestServer(debugMode,type,"status",mergeWorkInfo.GetInterviewID(),
				mergeWorkInfo.StartEpochMilli,
				"",fileType,videoType,mergeWorkInfo.FinishEpochMilli,
				(int)mergeWorkInfo.GetDurationSecondsTime(),fileSize,s3Url);
	}
	
	
	static void _RequestServer(boolean debugMode,String type,String uploadStatus,String interviewRequestId,long startTime,
			String participantId_or_userId,String fileType,String videoType,long endTime,int duration,long fileSize,String s3Url)
    {
        try
        {  
        	/*
			Body:
			{
    			"type": "interview" or "apptest",
				"status": "<status string>",       // status string 
				"interviewRequestId": "<...>",     // required if type is "interview"
				"participantId": "<...>",          // required if type is "apptest"
				"videoType": "screen" or "camera", // required if type is "apptest"
				"startTime": num,                  // seconds since epoch 
				"endTime": num,                    // seconds since epoch 
				"duration": num,                   // seconds 
				"size": num,                       // file size in bytes 
				"url": "<url of file>",            // s3 url 
				"secret": "Ygo/eH-55B"             // must include this!
			}
        	*/
        	
            String fullURL;
            if (false == debugMode)
            {
                fullURL = "https://www.methinks.io/janus/archive-callback";
            }
            else
            {
                fullURL = "https://dev.methinks.io/janus/archive-callback";
            }
           
            System.out.println("[_RequestServer] - URL : "+fullURL);

            URL url = new URL(fullURL);
            HttpURLConnection httpURLConnection = null;
            
            OutputStream outputStream = null;
            InputStream inputStream = null;
            DataOutputStream dataOutputStream = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            int bytesRead = 0, bytesAvailable = 0, bufferSize = 0;
            byte[] buffer = null;
            int maxBufferSize = 1 * 1024 * 1024;
            String BoundaryConstant = "----------V2ymHFg03ehb4238qgZCaK128O6jy";

            ByteArrayOutputStream byteArrayOutputStream = null;

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonObjectBody = new JSONObject();
            
            jsonObjectBody.put("type",type);// "interview" or "apptest",
            jsonObjectBody.put("status", uploadStatus);
            jsonObjectBody.put("fileType",fileType);
            
            switch(type)
            {
            case "interview":
            {
            	jsonObjectBody.put("interviewRequestId",interviewRequestId);// required if type is "interview"
            	jsonObjectBody.put("userId",participantId_or_userId);
            }
            break;
            
            case "apptest":
            {
            	jsonObjectBody.put("participantId",participantId_or_userId);          // required if type is "apptest"
            }
            break;
            }
            
            jsonObjectBody.put("videoType",videoType); //"screen" or "camera", // required if type is "apptest"
            jsonObjectBody.put("startTime",startTime);          
            jsonObjectBody.put("endTime",endTime);              
            jsonObjectBody.put("duration",duration);
            jsonObjectBody.put("size",fileSize);                        
            jsonObjectBody.put("url",s3Url);           
            jsonObjectBody.put("secret","Ygo/eH-55B");   
            
            String jsonString = jsonObjectBody.toJSONString();
            System.out.println("janus/archive-callback-json : " + jsonString);
            
            byte[] jsonBytes = jsonString.getBytes();
            int jsonBytesLength = jsonBytes.length;
            System.out.println("janus/archive-callback-Length : " + jsonBytesLength);
            
            httpURLConnection.setRequestProperty("Content-Length", Integer.toString(jsonBytesLength));
            outputStream = httpURLConnection.getOutputStream();
            outputStream.write(jsonBytes);
            outputStream.flush();
            outputStream.close();
            
            httpURLConnection.connect();
            String serverResponseMessage = "None";
            int serverResponseCode = httpURLConnection.getResponseCode();
            System.out.println("ResponseCode : " + serverResponseCode + " , ResponseMessage : "+httpURLConnection.getResponseMessage());

            switch (serverResponseCode)
            {
                case HttpURLConnection.HTTP_OK:
                {
                	System.out.println("ResponseCode : HTTP_OK");

                    inputStream = httpURLConnection.getInputStream();
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[2048];
                    byte[] byteData = null;
                    int nLength = 0;
                    while ((nLength = inputStream.read(byteBuffer, 0, byteBuffer.length)) != -1)
                    {
                        byteArrayOutputStream.write(byteBuffer, 0, nLength);
                    }

                    byteData = byteArrayOutputStream.toByteArray();
                    serverResponseMessage = new String(byteData);
                    System.out.println("ResponseMessage : " + serverResponseMessage);
                    
                    //JSONParser responseJSON = new JSONParser();
                    //try
                    //{
                    //	JSONObject jsonObject = (JSONObject) responseJSON.parse(serverResponseMessage);
                    //}
                    //catch(ParseException e)
                    //{}
                }
                break;

                default:
                {}
                break;
            }
            httpURLConnection.disconnect();
        
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();           
        }
        catch(IOException e)
        {
        	e.printStackTrace();          
        }
        
        log_handler.WriteConsole("-------------------------------------------------------------------------------"+System.getProperty("line.separator"));
    }
}

//'simple-1.1.1.jar' is not marked as executable.  If this was downloaded or copied from an untrusted source, it may be dangerous to run.  For more details, read about the
//sudo chmod +x /home/mike/Downloads/simple-1.1.1.jar
