import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;

/*
projectId: XX
interviewRequestId : XX
startTime : XX
endTime : XX
streams :[{ “userId” : XX, “type” : camera or screen” audio:bool, video:bool},…]
mergeName: XX
*/


public class main_argument 
{
	public static String Env;
	public static String ProjectId;
	public static String InterviewRequestId;
	public static long StartTime;
	public static long EndTime;
	public static ArrayList<stream_user_info> StreamUserInfoList;
	public static String MergeName;
	
	public static void Parse(String jsonInfo)
	{
		try 
		{
		    JSONParser mainArgumnetJsonParser = new JSONParser();
            JSONObject mainArgumnetJsonObject = (JSONObject) mainArgumnetJsonParser.parse(jsonInfo);
            
            if(mainArgumnetJsonObject.containsKey("env"))
            	Env = (String)mainArgumnetJsonObject.get("env");
            if(mainArgumnetJsonObject.containsKey("projectId"))
            	ProjectId = (String)mainArgumnetJsonObject.get("projectId");
            if(mainArgumnetJsonObject.containsKey("interviewRequestId"))
            	InterviewRequestId = (String)mainArgumnetJsonObject.get("interviewRequestId");
            if(mainArgumnetJsonObject.containsKey("startTime"))
            	StartTime = (Long)mainArgumnetJsonObject.get("startTime");
            if(mainArgumnetJsonObject.containsKey("endTime"))
            	EndTime = (Long)mainArgumnetJsonObject.get("endTime");
           
            if(mainArgumnetJsonObject.containsKey("streams"))
            {
            	JSONArray streamUserInfoArray = (JSONArray) mainArgumnetJsonObject.get("streams"); 
            	for(int countIndex=0; countIndex<streamUserInfoArray.size(); ++countIndex)
            	{ 
            		if(null == StreamUserInfoList)
            			StreamUserInfoList = new ArrayList<stream_user_info>();
            	
            		JSONObject streamUserInfoJsonObject = (JSONObject) streamUserInfoArray.get(countIndex); 
            	
            		stream_user_info streamUserInfo = new stream_user_info();
            		if(streamUserInfoJsonObject.containsKey("userId"))
            			streamUserInfo.UserId = (String)streamUserInfoJsonObject.get("userId");
            		if(streamUserInfoJsonObject.containsKey("type"))
            			streamUserInfo.Type = (String)streamUserInfoJsonObject.get("type");
            		if(streamUserInfoJsonObject.containsKey("audio"))
            			streamUserInfo.Audio = (boolean)streamUserInfoJsonObject.get("audio");
            		if(streamUserInfoJsonObject.containsKey("video"))
            			streamUserInfo.Video = (boolean)streamUserInfoJsonObject.get("video");
            	
            		StreamUserInfoList.add(streamUserInfo);
            	}
            }
            if(mainArgumnetJsonObject.containsKey("mergeName"))
            {
            	MergeName = (String)mainArgumnetJsonObject.get("mergeName");
            }
        } 
		catch (ParseException e)
		{
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	public static void TestSample()
	{
		Env = "dev";
		ProjectId = "f3dbycW9Hp";
		InterviewRequestId = "wNc60LgZtU";
	}

}
