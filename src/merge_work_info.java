import java.util.ArrayList;
import java.util.Collections;

public class merge_work_info
{
	public static final int STATUS_CHECK_ENABLE_MERGE_CONVERTED_FILE = 0;
	
	public static final int STATUS_START_MERGE_CONVERTED_FILE = 1;
	public static final int STATUS_PROCESS_MERGE_CONVERTED_FILE = 2;
	public static final int STATUS_FINISH_MERGE_CONVERTED_FILE = 3;
	
	public static final int STATUS_START_UPLOAD_MERGED_FILE = 4;
	public static final int STATUS_PROCESS_UPLOAD_MERGED_FILE = 5;
	public static final int STATUS_FINISH_UPLOAD_MERGED_FILE = 6;
	
	public static final int STATUS_START_CALL_REQUEST_MERGED_FILE = 7;
	public static final int STATUS_PROCESS_CALL_REQUEST_MERGED_FILE = 8;
	public static final int STATUS_FINISH_CALL_REQUEST_MERGED_FILE = 9;
	
	public static final int STATUS_FINISHED_ALL = 10;
	
    public long StartEpochMilli;
    public long FinishEpochMilli;
    
    public ArrayList<merge_render_info> RenderInfoList;
    
    public static ArrayList<merge_work_info> MergeWorkInfoList;
   
	private int _processStatus;
	
	public merge_render_info SearchMergeRenderInfo(work_unit compareWorkUnit)
	{
		if (null == RenderInfoList)
        	return null;
		
		for(int countIndex=0; countIndex<RenderInfoList.size(); ++countIndex)
		{
			merge_render_info mergeRenderInfo = RenderInfoList.get(countIndex);
			work_unit workUnit = mergeRenderInfo.GetWorkUnit();
			
			if(workUnit.GetStartEpochMilli() == compareWorkUnit.GetStartEpochMilli() &&
				workUnit.GetFinishEpochMilli() == compareWorkUnit.GetFinishEpochMilli())
				{
					if(workUnit.UserCode.equals(compareWorkUnit.UserCode))
						return mergeRenderInfo;
				}
		}
		
		return null;
	}
	
	
	public int GetCountRenderMergeInfo()
	{
		if (null == RenderInfoList)
			return 0;
		
		return RenderInfoList.size();
	}
	
	public work_unit GetWorkUnitRenderMergeInfo(int countIndex)
	{
		if (null == RenderInfoList)
			return null;
		
		merge_render_info mergeRenderInfo = RenderInfoList.get(countIndex);
		work_unit workUnit = mergeRenderInfo.GetWorkUnit();
		return workUnit;
	}
	
	
	/*
	public merge_render_info AddMergeRenderInfo(work_unit workUnit,
			float mergeWorkInfoStartSecondTime,float mergeWorkInfoFinishSecondTime)
	{
		
		if (null == RenderInfoList)
        	RenderInfoList = new ArrayList<merge_render_info>();

        merge_render_info combineRenderInfo = new merge_render_info();
        combineRenderInfo.SetWorkUnit(workUnit);
        combineRenderInfo.BetweenStartSecondTime = 
        		mergeWorkInfoStartSecondTime - workUnit.StartSecondTime;
                		
        combineRenderInfo.BetweenDurationSecondTime = 
        		mergeWorkInfoFinishSecondTime - mergeWorkInfoStartSecondTime;
        
        RenderInfoList.add(combineRenderInfo);
        return RenderInfoList.get(RenderInfoList.size()-1);
	}
	
	public merge_render_info SearchAddMergeRenderInfo(work_unit workUnit,
			float mergeWorkInfoStartSecondTime,float mergeWorkInfoFinishSecondTime)
	{
		merge_render_info mergeRenderInfo = SearchMergeRenderInfo(workUnit);
		if(null != mergeRenderInfo)
			return mergeRenderInfo;
		
		mergeRenderInfo = AddMergeRenderInfo(workUnit,
				mergeWorkInfoStartSecondTime,mergeWorkInfoFinishSecondTime);
		return mergeRenderInfo;
	}
	*/
	
	
	public merge_render_info AddMergeRenderInfoEpochMilli(work_unit workUnit,
			long mergeWorkInfoStartEpochMilli,long mergeWorkInfoFinishEpochMilli)
	{
		if (null == RenderInfoList)
        	RenderInfoList = new ArrayList<merge_render_info>();
        
        merge_render_info mergeRenderInfo = new merge_render_info();
        mergeRenderInfo.SetWorkUnit(workUnit);
        mergeRenderInfo.BetweenStartSecondTime = 
        		utility.GetDurationSecondsTime(workUnit.GetStartEpochMilli(), mergeWorkInfoStartEpochMilli);
        		
        RenderInfoList.add(mergeRenderInfo);
        return RenderInfoList.get(RenderInfoList.size()-1);
	}
	
	
	public merge_render_info SearchAddMergeRenderInfoEpochMilli(work_unit workUnit,
			long mergeWorkInfoStartEpochMilli,long mergeWorkInfoFinishEpochMilli)
	{
		merge_render_info mergeRenderInfo = SearchMergeRenderInfo(workUnit);
		if(null != mergeRenderInfo)
			return mergeRenderInfo;
		
		mergeRenderInfo = AddMergeRenderInfoEpochMilli(workUnit,
				mergeWorkInfoStartEpochMilli,mergeWorkInfoFinishEpochMilli);
		return mergeRenderInfo;
	}
	
	public boolean EnableMerge()
	{
		if(null == RenderInfoList)
		{
			log_handler.WriteConsole("null == RenderInfoList");
			return false;
		}
		
		ArrayList<String> userCodeList = GetUserCodeList();
		if(null == userCodeList)
		{
			log_handler.WriteConsole("null == userCodeList");
			return false;
		}
		
		if(GetContent().contains("interview"))
		{
			if(userCodeList.size() < 2)
			{
				log_handler.WriteConsole("userCodeList.size() < 2");
				return false;
			}
		}
		
		for(int countIndex=0; countIndex<RenderInfoList.size(); ++countIndex)
		{
			merge_render_info mergeRenderInfo = RenderInfoList.get(countIndex);
			work_unit workUnit = mergeRenderInfo.GetWorkUnit();
			if(workUnit.GetProcessStatus() < work_unit.STATUS_AFTER_ENABLE_MERGE)
				return false;
			
			log_handler.WriteConsole(
					"["+countIndex+"/"+RenderInfoList.size()+"]"+
					" StartEpochMilli : " + StartEpochMilli+
					" , FinishEpochMilli : " + FinishEpochMilli +
					" , StartSecondTime : "+mergeRenderInfo.BetweenStartSecondTime+
					" , DurationSecondTime : "+GetDurationSecondsTime() + 
					" , FileTitleName : " +mergeRenderInfo.GetWorkUnit().GetFileTitleName()+
					" , FileStartEpochMilli : "+mergeRenderInfo.GetWorkUnit().GetStartEpochMilli()+
					" , FileFinishEpochMilli : "+mergeRenderInfo.GetWorkUnit().GetFinishEpochMilli()+
					" , FileDurationSecondTime : "+mergeRenderInfo.GetWorkUnit().GetDurationSecondsTime());
		}
		
		return true;
	}
	
	
	public void Update()
	{
		switch(_processStatus)
		{
		case STATUS_CHECK_ENABLE_MERGE_CONVERTED_FILE:
		{
			switch(preset.ServiceType)
			{
				case preset.SERVICE_TYPE_CONVERT_MERGE:
				{
					if(true == EnableMerge())
						_processStatus = STATUS_START_MERGE_CONVERTED_FILE;
				}
				break;
			}
			
		}
		break;
		
		case STATUS_START_MERGE_CONVERTED_FILE:
		{
			_processStatus = STATUS_PROCESS_MERGE_CONVERTED_FILE;
			
			work_message_merge_converted_file workMessageMergeConvertedFile = new work_message_merge_converted_file();
			workMessageMergeConvertedFile.Build(this);
  			try 
  			{
				work_message.QueueMuxMerge.put(workMessageMergeConvertedFile);
			} 
  			catch (InterruptedException e) 
  			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		break;
		
		case STATUS_PROCESS_MERGE_CONVERTED_FILE:
		{}
		break;
		
		case STATUS_FINISH_MERGE_CONVERTED_FILE:
		{
			if(utility.IsFileClosed(preset.GetRecordFolderPath() + GetMergedFileFullName()))
				_processStatus = STATUS_START_UPLOAD_MERGED_FILE;
			
		}
		break;
		
		case STATUS_START_UPLOAD_MERGED_FILE:
		{
			_processStatus = STATUS_PROCESS_UPLOAD_MERGED_FILE;
			
			work_message_upload_merged_file_to_s3 workMessageUploadMergedFileToS3 = 
					new work_message_upload_merged_file_to_s3();
			workMessageUploadMergedFileToS3.Build(this);
  			try 
  			{
				work_message.QueueMuxMerge.put(workMessageUploadMergedFileToS3);
			} 
  			catch (InterruptedException e) 
  			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		break;
		
		case STATUS_PROCESS_UPLOAD_MERGED_FILE:
		{}
		break;
		
		case STATUS_FINISH_UPLOAD_MERGED_FILE:
		{
			_processStatus = STATUS_START_CALL_REQUEST_MERGED_FILE;
		}
		break;
		
		case STATUS_START_CALL_REQUEST_MERGED_FILE:
		{
			_processStatus = STATUS_PROCESS_CALL_REQUEST_MERGED_FILE;
			
			work_message_janus_archive_callback_merged_file workMessageJanusArchiveCallback = 
					new work_message_janus_archive_callback_merged_file();
			workMessageJanusArchiveCallback.BuildMergeWorkInfo(this);
  			try 
  			{
				work_message.QueueMuxMerge.put(workMessageJanusArchiveCallback);
			} 
  			catch (InterruptedException e) 
  			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  			
		}
		break;
		
		case STATUS_PROCESS_CALL_REQUEST_MERGED_FILE:
		{}
		break;
		
		case STATUS_FINISH_CALL_REQUEST_MERGED_FILE:
		{
			_processStatus = STATUS_FINISHED_ALL;
		}
		break;
		
		
		case STATUS_FINISHED_ALL:
		{}
		break;
		
		}
	}
	
	public void SetProcessStatus(int processStatus)
	{
		_processStatus = processStatus;
	}
	
	public String GetMergedFileFullName()
	{
		String mergedFileFullName =  GetEnv() +"_";
		mergedFileFullName += GetProjectID()+"_";
		mergedFileFullName += GetInterviewID()+"_";
		mergedFileFullName += StartEpochMilli+"_";
		mergedFileFullName += FinishEpochMilli+"_";
		mergedFileFullName += GetDurationSecondsTime();
		mergedFileFullName += ".mp4";
        
		return mergedFileFullName;
	}
	
	public long GetDurationSecondsTime()
	{
		return utility.GetDurationSecondsTime(StartEpochMilli, FinishEpochMilli);
	}
	
	
	public String GetEnv()
	{
		if(null == RenderInfoList || 0 == RenderInfoList.size())
			return null;
		
		return RenderInfoList.get(0).GetEnv();
	}
	
    public String GetProjectID()
    {
    	if(null == RenderInfoList || 0 == RenderInfoList.size())
			return null;
    	
    	return RenderInfoList.get(0).GetProjectID();
    }
    
    public String GetInterviewID()
    {
    	if(null == RenderInfoList || 0 == RenderInfoList.size())
    		return null;
    	
    	return RenderInfoList.get(0).GetInterviewID();
    }
    
    public String GetContent()
    {
    	if(null == RenderInfoList || 0 == RenderInfoList.size())
    		return null;
    	
    	return RenderInfoList.get(0).GetContent();
    }
	
	
	public long GetMergedFileSize()
	{
		long mergedFileSize = 
        	utility.GetFileSize(preset.GetRecordFolderPath() + GetMergedFileFullName());
		return mergedFileSize;
	}
	
	public ArrayList<String> GetUserCodeList()
    {
       if (null == RenderInfoList)
            return null;

       //log_handler.PrintConsole("RenderInfoList Size : "+RenderInfoList.size());
       
       
        ArrayList<String> userCodeList = null;
        for (int countIndex = 0; countIndex < RenderInfoList.size(); ++countIndex)
        {   
        	if(null == userCodeList)
        		userCodeList = new ArrayList<String>();
        	
        	work_unit workUnit = RenderInfoList.get(countIndex).GetWorkUnit();
            int userCodeCountIndex = userCodeList.indexOf(workUnit.UserCode);
            if(-1 != userCodeCountIndex)
            	continue;
            
            if (workUnit.UserType.equals("business"))
            {
            	userCodeList.add(0, workUnit.UserCode);
            }
            else if (workUnit.UserType.equals("observer"))
            {
            	if (-1 == userCodeList.indexOf("business"))
                {
            		userCodeList.add(0, workUnit.UserCode);
                }
                else
                {
                	userCodeList.add(1, workUnit.UserCode);
                }
            }
            else
            {
            	userCodeList.add(workUnit.UserCode);
            }
            
        }

        return userCodeList;
    }
	
	
	public merge_render_info SearchMergeRenderInfo(String userCode,String contentType)
	{
		if(null == RenderInfoList)
			return null;
		
		for (int countIndex0 = 0; countIndex0 < RenderInfoList.size(); ++countIndex0)
        {
        	merge_render_info mergeRenderInfo = RenderInfoList.get(countIndex0);
            work_unit workUnit = mergeRenderInfo.GetWorkUnit();
            
            if(workUnit.UserCode.equals(userCode) && workUnit.Content.contains(contentType))
            	return mergeRenderInfo;
        }
    
		return null;
	}
	
	
	public int CalculateRenderLayoutSize()
	{
		int totalRenderInfoCount = 0;
		if(null == RenderInfoList || 0 == RenderInfoList.size())
			return 0;
		
        totalRenderInfoCount = RenderInfoList.size();
        int userCodeCount = GetUserCodeList().size();
        	
        for(int countIndex=0; countIndex<RenderInfoList.size(); ++countIndex)
        {
        	merge_render_info mergeRenderInfo = RenderInfoList.get(countIndex);
        	work_unit workUnit = mergeRenderInfo.GetWorkUnit();
                
            if(3 == workUnit.CommandType)
            	-- userCodeCount;
        }
        	
        int widthCount = 1;
        int heightCount = 1;
        int loopCount = 1;
        while (userCodeCount > loopCount * loopCount)
        {
        	++loopCount;
        	widthCount = loopCount;
        }

        loopCount = 0;
        while (0 != (userCodeCount + loopCount) % widthCount)
        {
        	++loopCount;
        }
        heightCount = (userCodeCount + loopCount) / widthCount;

        //
        if (0 == ffmpeg_handler.MergeResolutionWidth && 0 == ffmpeg_handler.MergeResolutionHeight)
        {
        	ffmpeg_handler.MergeResolutionWidth = 1280;
        	ffmpeg_handler.MergeResolutionHeight = 720;
        }
            
        for (int countIndex = 0; countIndex < totalRenderInfoCount; ++countIndex)
        {
        	merge_render_info mergeRenderInfo = RenderInfoList.get(countIndex);
            work_unit workUnit = mergeRenderInfo.GetWorkUnit();
                
            if(3 == workUnit.CommandType)
            	continue;
                        		
            int userCodeCountIndex = GetUserCodeList().indexOf(workUnit.UserCode);

            int renderWidth = ffmpeg_handler.MergeResolutionWidth / widthCount;
        	int renderHeight = ffmpeg_handler.MergeResolutionHeight / heightCount;
            	
            mergeRenderInfo.Height = renderHeight;

            //camera
            if (true == workUnit.Content.toLowerCase().contains("camera"))
            {
            	if(null != SearchMergeRenderInfo(workUnit.UserCode,"screen"))
            	{
            		mergeRenderInfo.Width = renderWidth / 2;
            		mergeRenderInfo.XPosition =
            					userCodeCountIndex % widthCount * (mergeRenderInfo.Width*2);
            	}
            	else
            	{
            		mergeRenderInfo.Width = renderWidth;
            		mergeRenderInfo.XPosition =
            					userCodeCountIndex % widthCount * (mergeRenderInfo.Width);
            	}
            }
            //screen
            else
            {
            	if(null != SearchMergeRenderInfo(workUnit.UserCode,"camera"))
            	{
            		mergeRenderInfo.Width = renderWidth / 2;
            		mergeRenderInfo.XPosition =
            					userCodeCountIndex % widthCount * (mergeRenderInfo.Width*2) + mergeRenderInfo.Width;
            	}
            	else
            	{
            		mergeRenderInfo.Width = renderWidth;
            		mergeRenderInfo.XPosition =
            					userCodeCountIndex % widthCount * (mergeRenderInfo.Width) + mergeRenderInfo.Width;
            	}
            }

            mergeRenderInfo.YPosition =
            			userCodeCountIndex / widthCount * mergeRenderInfo.Height;
        }
        
        
        return totalRenderInfoCount;
	}
	
	
	public void DeleteMergedFile(boolean deleteMergeWorkInfo)
	{
		
		if(true == deleteMergeWorkInfo)
		{
			
		}
	}
	
	/*
    public int GetRealFileCount()
    {   
        if (null == RenderInfoList || 0 == RenderInfoList.size())
            return 0;

        ArrayList<String> fileNameList = new ArrayList<String>();
        for (int countIndex = 0; countIndex < RenderInfoList.size(); ++countIndex)
        {
            //record_file_info recordFileInfo = 
            //		record_file_info.SearchRecordFileInfo(RenderInfoList.get(countIndex).RecordFileFullName,
           // 				(record_file_info[])RenderInfoList.toArray());
            
            record_file_info recordFileInfo = 
            		record_file_info.SearchRecordFileInfo2(RenderInfoList.get(countIndex).MuxedFileFullName);

            String[] splits = recordFileInfo.MuxedFileFullName.split("-");
            int fileNameCountIndex = fileNameList.indexOf(splits[0].toLowerCase());
            if (-1 == fileNameCountIndex)
            {
                fileNameList.add(splits[0].toLowerCase());
            }
        }
        
        return fileNameList.size();
    }

    public ArrayList<String> GetUserCodeList()
    {
       if (null == RenderInfoList || 0 == RenderInfoList.size())
            return null;

        ArrayList<String> userCodeList = new ArrayList<String>();
        for (int countIndex = 0; countIndex < RenderInfoList.size(); ++countIndex)
        {
            //record_file_info recordFileInfo = 
            //		record_file_info.SearchRecordFileInfo(RenderInfoList.get(countIndex).RecordFileFullName,
            //				(record_file_info[])RenderInfoList.toArray());
            
        	record_file_info recordFileInfo = 
            		record_file_info.SearchRecordFileInfo2(RenderInfoList.get(countIndex).MuxedFileFullName);
        
            int userCodeCountIndex = userCodeList.indexOf(recordFileInfo.UserCode);
            
            //search user
            boolean searchUser = false;
            for (int userCountIndex = 0; userCountIndex < userCodeList.size(); ++userCountIndex)
            {
                if (true == userCodeList.get(userCountIndex).equals(recordFileInfo.UserCode))
                {
                    searchUser = true;
                    break;
                }
            }
            if (false == searchUser)
            {
                if (recordFileInfo.UserType.equals("business"))
                {
                    userCodeList.add(0, recordFileInfo.UserCode);
                }
                else if (recordFileInfo.UserType.equals("observer"))
                {
                    if (-1 == userCodeList.indexOf("business"))
                    {
                        userCodeList.add(0, recordFileInfo.UserCode);
                    }
                    else
                    {
                        userCodeList.add(1, recordFileInfo.UserCode);
                    }
                }
                else
                {
                    userCodeList.add(recordFileInfo.UserCode);
                }
            }
        }

        return userCodeList;
    }
    */
    
    /*
    public static void MergeCombineFile(ArrayList<mjr_converted_file_pair> mjrConvertedFilePairList)
    {	
    	int countIndex = 0;
        for (countIndex = 0; countIndex < mjrConvertedFilePairList.size(); ++countIndex)
        {
        	mjr_converted_file_pair mjrConvertedFilePair = mjrConvertedFilePairList.get(countIndex);
        	if(false == utility.IsFileClosed(work_manager.GetRecordFolderPathName()+mjrConvertedFilePair.MuxFileFullPathName))
        		continue;
        	
        	if (null == record_file_info.RecordFileInfoList)
        		record_file_info.RecordFileInfoList = new ArrayList<record_file_info>();
        		
            record_file_info recordFileInfo = new record_file_info();    
            recordFileInfo.MuxedFileFullName = mjrConvertedFilePair.MuxFileFullPathName;
            
            recordFileInfo.Env = mjrConvertedFilePair.Env;
            recordFileInfo.Projectid = mjrConvertedFilePair.Projectid;
            recordFileInfo.Interviewid = mjrConvertedFilePair.Interviewid;
            recordFileInfo.Content = mjrConvertedFilePair.Content;
            recordFileInfo.ProjectStartTickTime = mjrConvertedFilePair.ProjectStartTickTime;
            recordFileInfo.ProjectFinishTickTime = mjrConvertedFilePair.ProjectFinishTickTime;
            recordFileInfo.ProjectCurrentTickTime = mjrConvertedFilePair.ProjectCurrentTickTime;
            recordFileInfo.UserType = mjrConvertedFilePair.UserType;
            recordFileInfo.UserCode = mjrConvertedFilePair.UserCode;
            recordFileInfo.CommandType = Integer.parseInt(mjrConvertedFilePair.CommandType);
            
            if(null != mjrConvertedFilePair.VideoFileFullPathName && 
            		0 < mjrConvertedFilePair.VideoFileFullPathName.length())
            {
            	recordFileInfo.MediaType = "video";
            }
            
            if(null != mjrConvertedFilePair.AudioFileFullPathName && 
            		0 < mjrConvertedFilePair.AudioFileFullPathName.length())
            {
            	if(null != recordFileInfo.MediaType && 0 < recordFileInfo.MediaType.length())
            	{
            		recordFileInfo.MediaType = "video-audio";
            	}
            	else
            	{
            		recordFileInfo.MediaType = "audio";
            	}
            }
            
            if (0 == ffprobe_handler.FileResolutionWidth && 0 == ffprobe_handler.FileResolutionHeight)
            	ffprobe_handler.GetFileResolution(work_manager.GetRecordFolderPathName()+recordFileInfo.MuxedFileFullName);
            
            recordFileInfo.DurationSecondTime = 
            		ffprobe_handler.GetFileDurationSecondTime(work_manager.GetRecordFolderPathName()+recordFileInfo.MuxedFileFullName);
            recordFileInfo.CalculateStartFinishSecondTime();
            
            record_file_info.RecordFileInfoList.add(recordFileInfo);

            //System.out.println("FileFullName["+countIndex+" / "+ searchFullPathFileNames + "] : "+ recordFileInfo.FileFullName);
            //System.out.println("FullPathFileName[" + countIndex + " / " + searchFullPathFileNames.Length + "] : " + searchFullPathFileNames[countIndex]);
        }
    	
        if (null != record_file_info.RecordFileInfoList)
        {	
            //arrange
        	ArrayList<Float> secondTimeList = new ArrayList<Float>();
            for (countIndex = 0; countIndex < record_file_info.RecordFileInfoList.size(); ++countIndex)
            {   
                if (-1 == secondTimeList.indexOf(record_file_info.RecordFileInfoList.get(countIndex).StartSecondTime))
                    secondTimeList.add(record_file_info.RecordFileInfoList.get(countIndex).StartSecondTime);

                if (-1 == secondTimeList.indexOf(record_file_info.RecordFileInfoList.get(countIndex).FinishSecondTime))
                    secondTimeList.add(record_file_info.RecordFileInfoList.get(countIndex).FinishSecondTime);
            }
           
            Collections.sort(secondTimeList);


            int fileCountIndex = 0;
            for (countIndex = 0; countIndex < secondTimeList.size()-1; ++countIndex)
            {
                if (null == CombineWorkInfoList)
                	CombineWorkInfoList = new ArrayList<combine_work_info>();
                
                if (1.0f > secondTimeList.get(countIndex+1) - secondTimeList.get(countIndex))
                    continue;
                //if (1000L > tickTimeList[countIndex+1] - tickTimeList[countIndex])
                //    return false;
                 
                combine_work_info combineWorkInfo = new combine_work_info();
                combineWorkInfo.StartSecondTime = secondTimeList.get(countIndex);
                combineWorkInfo.FinishSecondTime = secondTimeList.get(countIndex+1);
                combineWorkInfo.MergedFileFullName = "archive_" + fileCountIndex + ".mp4";
                
                for (int countIndex2 = 0; countIndex2 < record_file_info.RecordFileInfoList.size(); ++countIndex2)
                {
                    if (true == record_file_info.RecordFileInfoList.get(countIndex2).ContainSecondTime(combineWorkInfo.StartSecondTime, combineWorkInfo.FinishSecondTime))
                    {
                        if (null == combineWorkInfo.RenderInfoList)
                            combineWorkInfo.RenderInfoList = new ArrayList<combine_render_info>();

                        combine_render_info combineRenderInfo = new combine_render_info();
                        combineRenderInfo.MuxedFileFullName = 
                        		record_file_info.RecordFileInfoList.get(countIndex2).MuxedFileFullName;
                        combineRenderInfo.BetweenStartSecondTime = 
                        		combineWorkInfo.StartSecondTime - record_file_info.RecordFileInfoList.get(countIndex2).StartSecondTime;
                        combineRenderInfo.BetweenDurationSecondTime = 
                        		combineWorkInfo.FinishSecondTime - combineWorkInfo.StartSecondTime;
                        
                        //
                        combineWorkInfo.Env = record_file_info.RecordFileInfoList.get(countIndex2).Env;
                        combineWorkInfo.Projectid = record_file_info.RecordFileInfoList.get(countIndex2).Projectid;
                        combineWorkInfo.Interviewid = record_file_info.RecordFileInfoList.get(countIndex2).Interviewid;
                        combineWorkInfo.Content = record_file_info.RecordFileInfoList.get(countIndex2).Content;

                        combineWorkInfo.RenderInfoList.add(combineRenderInfo);
                    }
                }

                if (null != combineWorkInfo.RenderInfoList)
                {
                    if (2 > combineWorkInfo.GetRealFileCount())
                    {
                        combineWorkInfo = null;
                        continue;
                    }
                    else
                    {
                        ++fileCountIndex;
                    }
                    
                    //1600x1200
                    int userCodeCount = combineWorkInfo.GetUserCodeList().size();
                    int widthCount = 1;
                    int heightCount = 1;
                    int loopCount = 1;
                    while (userCodeCount > loopCount * loopCount)
                    {
                        ++loopCount;
                        widthCount = loopCount;
                    }

                    loopCount = 0;
                    while (0 != (userCodeCount + loopCount) % widthCount)
                    {
                        ++loopCount;
                    }
                    heightCount = (userCodeCount + loopCount) / widthCount;

                    //
                    if (0 == ResizedResolutionWidth && 0 == ResizedResolutionHeight)
                    {
                        int fileResolutionWidth = ffprobe_handler.FileResolutionWidth;
                        int fileResolutionHeight = ffprobe_handler.FileResolutionHeight;

                        ResizedResolutionWidth = 1280;
                        ResizedResolutionHeight = 720;

                        //ffmpeg_handler.ResizeEmptyImage();
                    }

                    for (int countIndex3 = 0; countIndex3 < combineWorkInfo.RenderInfoList.size(); ++countIndex3)
                    {
                        record_file_info recordFileInfo = 
                        		record_file_info.SearchRecordFileInfo(
                        				combineWorkInfo.RenderInfoList.get(countIndex3).MuxedFileFullName,
                        				(record_file_info[])record_file_info.RecordFileInfoList.toArray());
                        
                        int userCodeCountIndex = 
                        		combineWorkInfo.GetUserCodeList().indexOf(recordFileInfo.UserCode);

                        //interview
                        boolean interviewContent = false;
                        if (true == recordFileInfo.Content.toLowerCase().contains("interview"))
                            interviewContent = true;
                        
                        if (true == interviewContent)
                        {
                            if (0 == combineWorkInfo.RenderWidth)
                                combineWorkInfo.RenderWidth = ResizedResolutionWidth / widthCount / 2;
                            if (0 == combineWorkInfo.RenderHeight)
                                combineWorkInfo.RenderHeight = ResizedResolutionHeight / heightCount;

                            if (true == recordFileInfo.Content.toLowerCase().contains("camera"))
                            {
                                combineWorkInfo.RenderInfoList.get(countIndex3).XPosition =
                                userCodeCountIndex % widthCount * (combineWorkInfo.RenderWidth*2);
                            }
                            else//screen
                            {
                                combineWorkInfo.RenderInfoList.get(countIndex3).XPosition =
                                userCodeCountIndex % widthCount * (combineWorkInfo.RenderWidth*2) + combineWorkInfo.RenderWidth;
                            }

                            combineWorkInfo.RenderInfoList.get(countIndex3).YPosition =
                                userCodeCountIndex / widthCount * combineWorkInfo.RenderHeight;
                        }
                        //apptest
                        else
                        {
                            if (0 == combineWorkInfo.RenderWidth)
                                combineWorkInfo.RenderWidth = ResizedResolutionWidth / widthCount;
                            if (0 == combineWorkInfo.RenderHeight)
                                combineWorkInfo.RenderHeight = ResizedResolutionHeight / heightCount;

                            combineWorkInfo.RenderInfoList.get(countIndex3).XPosition =
                                userCodeCountIndex % widthCount * combineWorkInfo.RenderWidth;

                            combineWorkInfo.RenderInfoList.get(countIndex3).YPosition =
                                userCodeCountIndex / widthCount * combineWorkInfo.RenderHeight;
                        }
                    }
                }
                
                CombineWorkInfoList.add(combineWorkInfo);
            }
            
            //process
            if (null != CombineWorkInfoList)
            {
                //3600s(1h) - 5 - 00:22:44 - 1280x720
                //3600s(1h) - 10 - 00:36:42 - 1280x720
                //3600s(1h) - 20 - 01:07:33 - 1280x720
                //3600s(1h) - 20 - 00:59:45 - 1280x720 - preset ultrafast

                for (countIndex = 0; countIndex < CombineWorkInfoList.size(); ++countIndex)
                {
                    //DateTime nowDateTime = DateTime.Now;
                    //System.out.println("StartTime[" + countIndex + "/" + _combineWorkInfoList.size() + "] - " + _combineWorkInfoList.get(countIndex).OutputFileFullName + " - " + nowDateTime.ToString());

                    ffmpeg_handler.CombineFFmpeg(CombineWorkInfoList.get(countIndex),(record_file_info[])record_file_info.RecordFileInfoList.toArray());
                    //TimeSpan timeSpan = DateTime.Now - nowDateTime;
                    //string elapsedTime = timeSpan.ToString();

                    //System.out.println("FinishTime[" + countIndex + "/" + _combineWorkInfoList.size() + "] - " + _combineWorkInfoList.get(countIndex).OutputFileFullName + " - " + DateTime.Now.ToString());
                    //System.out.println("ElapsedTime["+countIndex+"/"+ _combineWorkInfoList.size() + "] - "+ _combineWorkInfoList.get(countIndex).OutputFileFullName+" - "+ elapsedTime);
                }

                String resultFileName = "";
                //resultFileName = ConcatFFmpeg(_combineWorkInfoList);

                boolean deleteFileFlag = false;
                if(true == deleteFileFlag)
                {
                    for (countIndex = 0; countIndex < CombineWorkInfoList.size(); ++countIndex)
                    {
                    	combine_work_info combineWorkInfo = CombineWorkInfoList.get(countIndex);
                    	File deleteFile = 
                    			new File(work_manager.GetRecordFolderPathName()+combineWorkInfo.MergedFileFullName);
                        deleteFile.delete();
                    	
                    }
                }
            }
        }   
    }
    */
    
    /*
    public static void MergeCombineFileInWorkList(ArrayList<work_unit> workUnitList)
    {	
    	if(null == workUnitList)
    		return;
    	
    	int countIndex = 0;
        for (countIndex = 0; countIndex < workUnitList.size(); ++countIndex)
        {
        	work_unit workUnit = workUnitList.get(countIndex);
        	if(false == utility.IsFileClosed(work_manager.GetRecordFolderPathName()+workUnit.MuxedFileFullName))
        		continue;
        	
        	if (null == record_file_info.RecordFileInfoList)
        		record_file_info.RecordFileInfoList = new ArrayList<record_file_info>();
        		
            record_file_info recordFileInfo = new record_file_info();    
            
            recordFileInfo.MuxedFileFullName = workUnit.MuxedFileFullName;
            recordFileInfo.Env = workUnit.Env;
            recordFileInfo.Projectid = workUnit.Projectid;
            recordFileInfo.Interviewid = workUnit.Interviewid;
            recordFileInfo.Content = workUnit.Content;
            recordFileInfo.ProjectStartTickTime = workUnit.GetAbsoluteStartTickCountTime();
            recordFileInfo.ProjectFinishTickTime = workUnit.GetAbsoluteFinishTickCountTime();
            recordFileInfo.ProjectCurrentTickTime = workUnit.GetAbsoluteStartTickCountTime();
            recordFileInfo.UserType = workUnit.UserType;
            recordFileInfo.UserCode = workUnit.UserCode;
            recordFileInfo.CommandType = workUnit.CommandType;
            
            
            if(false == utility.IsNullOrEmpty(workUnit.ConvertedVideoFileFullName))
            {
            	recordFileInfo.MediaType = "video";
            }
            
            if(false == utility.IsNullOrEmpty(workUnit.ConvertedAudioFileFullName))
            {
            	if(null != recordFileInfo.MediaType && 0 < recordFileInfo.MediaType.length())
            	{
            		recordFileInfo.MediaType = "video-audio";
            	}
            	else
            	{
            		recordFileInfo.MediaType = "audio";
            	}
            }
            
            if (0 == ffprobe_handler.FileResolutionWidth && 0 == ffprobe_handler.FileResolutionHeight)
            	ffprobe_handler.GetFileResolution(work_manager.GetRecordFolderPathName()+recordFileInfo.MuxedFileFullName);
            
            recordFileInfo.DurationSecondTime = workUnit.GetDurationSecondsTime();
            recordFileInfo.AbsoluteStartTickCountTime = workUnit.GetAbsoluteStartTickCountTime();
            recordFileInfo.AbsoluteFinishTickCountTime = workUnit.GetAbsoluteFinishTickCountTime();
            		//ffprobe_handler.GetFileDurationSecondTime(recordFileInfo.FullPathFileName);
            //recordFileInfo.CalculateStartFinishSecondTime();
            
            record_file_info.RecordFileInfoList.add(recordFileInfo);

            //System.out.println("FileFullName["+countIndex+" / "+ searchFullPathFileNames + "] : "+ recordFileInfo.FileFullName);
            //System.out.println("FullPathFileName[" + countIndex + " / " + searchFullPathFileNames.Length + "] : " + searchFullPathFileNames[countIndex]);
        }
    	
        if (null != record_file_info.RecordFileInfoList)
        {	
            //arrange
        	ArrayList<Long> tickTimeList = new ArrayList<Long>();
            for (countIndex = 0; countIndex < record_file_info.RecordFileInfoList.size(); ++countIndex)
            {   
            	record_file_info checkRecordFileInfo = record_file_info.RecordFileInfoList.get(countIndex);
            	
                if (-1 == tickTimeList.indexOf(checkRecordFileInfo.AbsoluteStartTickCountTime))
                	tickTimeList.add(checkRecordFileInfo.AbsoluteStartTickCountTime);

                if (-1 == tickTimeList.indexOf(checkRecordFileInfo.AbsoluteFinishTickCountTime))
                	tickTimeList.add(checkRecordFileInfo.AbsoluteFinishTickCountTime);
            }
            
            Collections.sort(tickTimeList);
           
            for (countIndex = 0; countIndex < tickTimeList.size()-1; ++countIndex)
            {
                if (null == CombineWorkInfoList)
                	CombineWorkInfoList = new ArrayList<combine_work_info>();
                
                long zeroTickTime = tickTimeList.get(0);
                long startTickTime = tickTimeList.get(countIndex);
                long finishTickTime = tickTimeList.get(countIndex+1);
                	
                //if (1000L > finishTickTime - startTickTime)
                //    continue;
                 
                combine_work_info combineWorkInfo = new combine_work_info();
                
                combineWorkInfo.StartSecondTime = ((float)(startTickTime - zeroTickTime)) / 1000.0f;
                combineWorkInfo.FinishSecondTime = ((float)(finishTickTime - zeroTickTime)) / 1000.0f;
                combineWorkInfo.StartTickTime = startTickTime;
                combineWorkInfo.FinishTickTime = finishTickTime;
                combineWorkInfo.DurationSecondTime = 
                		(int)utility.GetDurationSecondsTime(startTickTime, finishTickTime, true);
                
                
                for (int countIndex2 = 0; countIndex2 < record_file_info.RecordFileInfoList.size(); ++countIndex2)
                {
                	record_file_info recordFileInfo = 
                    		record_file_info.RecordFileInfoList.get(countIndex2);
                	
                	recordFileInfo.StartSecondTime = 
                			((float)(recordFileInfo.AbsoluteStartTickCountTime  - zeroTickTime)) / 1000.0f;
                	recordFileInfo.FinishSecondTime = 
                			((float)(recordFileInfo.AbsoluteFinishTickCountTime  - zeroTickTime)) / 1000.0f;
                	  	
                    if (false == recordFileInfo.ContainSecondTime(combineWorkInfo.StartSecondTime, combineWorkInfo.FinishSecondTime))
                    	continue;
                    
                    if (null == combineWorkInfo.RenderInfoList)
                    	combineWorkInfo.RenderInfoList = new ArrayList<combine_render_info>();
   
                    combine_render_info combineRenderInfo = new combine_render_info();
                    combineRenderInfo.MuxedFileFullName = recordFileInfo.MuxedFileFullName;
                        
                    combineRenderInfo.BetweenStartSecondTime = 
                        		combineWorkInfo.StartSecondTime - recordFileInfo.StartSecondTime;
                        		
                    combineRenderInfo.BetweenDurationSecondTime = 
                        		combineWorkInfo.FinishSecondTime - combineWorkInfo.StartSecondTime;
                        
                    combineWorkInfo.Env = recordFileInfo.Env;
                    combineWorkInfo.Projectid = recordFileInfo.Projectid;
                    combineWorkInfo.Interviewid = recordFileInfo.Interviewid;
                    combineWorkInfo.Content = recordFileInfo.Content;
                    
                    if(true == utility.IsNullOrEmpty(combineWorkInfo.MergedFileFullName))
                    {
                    	combineWorkInfo.MergedFileFullName = recordFileInfo.Env+"_";
                    	combineWorkInfo.MergedFileFullName += recordFileInfo.Projectid+"_";
                    	combineWorkInfo.MergedFileFullName += recordFileInfo.Interviewid+"_";
                    	combineWorkInfo.MergedFileFullName += startTickTime+"_";
                    	combineWorkInfo.MergedFileFullName += finishTickTime+"_";
                    	combineWorkInfo.MergedFileFullName += (int)(combineWorkInfo.FinishSecondTime - combineWorkInfo.StartSecondTime);
                    	combineWorkInfo.MergedFileFullName += ".mp4";
                    }

                    combineWorkInfo.RenderInfoList.add(combineRenderInfo);
                    
                }

                if (null != combineWorkInfo.RenderInfoList)
                {
                    if (2 > combineWorkInfo.GetRealFileCount())
                    {
                        combineWorkInfo = null;
                        continue;
                    }
                    else
                    {
                        //++fileCountIndex;
                    }
                    
                    //1600x1200
                    int userCodeCount = combineWorkInfo.GetUserCodeList().size();
                    int widthCount = 1;
                    int heightCount = 1;
                    int loopCount = 1;
                    while (userCodeCount > loopCount * loopCount)
                    {
                        ++loopCount;
                        widthCount = loopCount;
                    }

                    loopCount = 0;
                    while (0 != (userCodeCount + loopCount) % widthCount)
                    {
                        ++loopCount;
                    }
                    heightCount = (userCodeCount + loopCount) / widthCount;

                    //
                    if (0 == ResizedResolutionWidth && 0 == ResizedResolutionHeight)
                    {
                        //int fileResolutionWidth = ffprobe_handler.FileResolutionWidth;
                        //int fileResolutionHeight = ffprobe_handler.FileResolutionHeight;

                        ResizedResolutionWidth = 1280;
                        ResizedResolutionHeight = 720;

                        //ffmpeg_handler.ResizeEmptyImage();
                    }

                    for (int countIndex3 = 0; countIndex3 < combineWorkInfo.RenderInfoList.size(); ++countIndex3)
                    {
                    	combine_render_info combineRenderInfo = combineWorkInfo.RenderInfoList.get(countIndex3);
                    	
                        record_file_info recordFileInfo = 
                        		record_file_info.SearchRecordFileInfo2(combineRenderInfo.MuxedFileFullName);
                        
                        int userCodeCountIndex = 
                        		combineWorkInfo.GetUserCodeList().indexOf(recordFileInfo.UserCode);

                        //interview
                        boolean interviewContent = false;
                        if (true == recordFileInfo.Content.toLowerCase().contains("interview"))
                            interviewContent = true;
                        
                        if (true == interviewContent)
                        {
                            if (0 == combineWorkInfo.RenderWidth)
                                combineWorkInfo.RenderWidth = ResizedResolutionWidth / widthCount / 2;
                            if (0 == combineWorkInfo.RenderHeight)
                                combineWorkInfo.RenderHeight = ResizedResolutionHeight / heightCount;

                            if (true == recordFileInfo.Content.toLowerCase().contains("camera"))
                            {
                                combineWorkInfo.RenderInfoList.get(countIndex3).XPosition =
                                userCodeCountIndex % widthCount * (combineWorkInfo.RenderWidth*2);
                            }
                            else//screen
                            {
                                combineWorkInfo.RenderInfoList.get(countIndex3).XPosition =
                                userCodeCountIndex % widthCount * (combineWorkInfo.RenderWidth*2) + combineWorkInfo.RenderWidth;
                            }

                            combineWorkInfo.RenderInfoList.get(countIndex3).YPosition =
                                userCodeCountIndex / widthCount * combineWorkInfo.RenderHeight;
                        }
                        //apptest
                        else
                        {
                            if (0 == combineWorkInfo.RenderWidth)
                                combineWorkInfo.RenderWidth = ResizedResolutionWidth / widthCount;
                            if (0 == combineWorkInfo.RenderHeight)
                                combineWorkInfo.RenderHeight = ResizedResolutionHeight / heightCount;

                            combineWorkInfo.RenderInfoList.get(countIndex3).XPosition =
                                userCodeCountIndex % widthCount * combineWorkInfo.RenderWidth;

                            combineWorkInfo.RenderInfoList.get(countIndex3).YPosition =
                                userCodeCountIndex / widthCount * combineWorkInfo.RenderHeight;
                        }
                    }
                }
                
                CombineWorkInfoList.add(combineWorkInfo);
            }
            
            //process
            if (null != CombineWorkInfoList)
            {
                //3600s(1h) - 5 - 00:22:44 - 1280x720
                //3600s(1h) - 10 - 00:36:42 - 1280x720
                //3600s(1h) - 20 - 01:07:33 - 1280x720
                //3600s(1h) - 20 - 00:59:45 - 1280x720 - preset ultrafast

                for (countIndex = 0; countIndex < CombineWorkInfoList.size(); ++countIndex)
                {
                    //DateTime nowDateTime = DateTime.Now;
                    //System.out.println("StartTime[" + countIndex + "/" + _combineWorkInfoList.size() + "] - " + _combineWorkInfoList.get(countIndex).OutputFileFullName + " - " + nowDateTime.ToString());
                	
                	combine_work_info combineWorkInfo = CombineWorkInfoList.get(countIndex);
                	
                    ffmpeg_handler.CombineFFmpeg2(combineWorkInfo);
                    //TimeSpan timeSpan = DateTime.Now - nowDateTime;
                    //string elapsedTime = timeSpan.ToString();

                    //System.out.println("FinishTime[" + countIndex + "/" + _combineWorkInfoList.size() + "] - " + _combineWorkInfoList.get(countIndex).OutputFileFullName + " - " + DateTime.Now.ToString());
                    //System.out.println("ElapsedTime["+countIndex+"/"+ _combineWorkInfoList.size() + "] - "+ _combineWorkInfoList.get(countIndex).OutputFileFullName+" - "+ elapsedTime);
                    
                    aws_s3_handler.UploadFile(combineWorkInfo);
                    
                    janus_archive_callback_handler.RequestUpdate(combineWorkInfo);
                }

                String resultFileName = "";
                //resultFileName = ConcatFFmpeg(_combineWorkInfoList);

                boolean deleteFileFlag = false;
                if(true == deleteFileFlag)
                {
                    for (countIndex = 0; countIndex < CombineWorkInfoList.size(); ++countIndex)
                    {
                    	combine_work_info combineWorkInfo = CombineWorkInfoList.get(countIndex);
                    	
                    	File deleteFile =
                    			new File(work_manager.GetRecordFolderPathName()+combineWorkInfo.MergedFileFullName);
                        deleteFile.delete();
                    }
                }
            }
        }   
    }
    */
    
	/*
    public static void MergeUploadCall(ArrayList<work_unit> workUnitList)
    {	
    	if(null == workUnitList || 0 == workUnitList.size())
    		return;
    	
    	int countIndex = 0;
        for (countIndex = 0; countIndex < workUnitList.size(); ++countIndex)
        {
        	work_unit workUnit = workUnitList.get(countIndex);
        	if(false == utility.IsFileClosed(work_manager.GetRecordFolderPathName()+workUnit.MuxedFileFullName))
        		continue;
        	
            if (0 == ffprobe_handler.FileResolutionWidth && 0 == ffprobe_handler.FileResolutionHeight)
            	ffprobe_handler.GetFileResolution(work_manager.GetRecordFolderPathName()+workUnit.MuxedFileFullName);
            
            //System.out.println("FileFullName["+countIndex+" / "+ searchFullPathFileNames + "] : "+ recordFileInfo.FileFullName);
            //System.out.println("FullPathFileName[" + countIndex + " / " + searchFullPathFileNames.Length + "] : " + searchFullPathFileNames[countIndex]);
        }
    	
        //arrange
        ArrayList<Long> tickTimeList = new ArrayList<Long>();
        for (countIndex = 0; countIndex < workUnitList.size(); ++countIndex)
        {   
        	work_unit workUnit = workUnitList.get(countIndex);
            	
            if (-1 == tickTimeList.indexOf(workUnit.GetAbsoluteStartTickTime()))
            	tickTimeList.add(workUnit.GetAbsoluteStartTickTime());

            if (-1 == tickTimeList.indexOf(workUnit.GetAbsoluteFinishTickTime()))
                tickTimeList.add(workUnit.GetAbsoluteFinishTickTime());
        }
            
        Collections.sort(tickTimeList);
           
        for (countIndex = 0; countIndex < tickTimeList.size()-1; ++countIndex)
        {
        	if (null == CombineWorkInfoList)
        		CombineWorkInfoList = new ArrayList<combine_work_info>();
                
            long zeroTickTime = tickTimeList.get(0);
            long startTickTime = tickTimeList.get(countIndex);
            long finishTickTime = tickTimeList.get(countIndex+1);
                	
            //if (1000L > finishTickTime - startTickTime)
            //    continue;
                 
            combine_work_info combineWorkInfo = new combine_work_info();
                
            combineWorkInfo.StartSecondTime = ((float)(startTickTime - zeroTickTime)) / 1000.0f;
            combineWorkInfo.FinishSecondTime = ((float)(finishTickTime - zeroTickTime)) / 1000.0f;
            combineWorkInfo.StartTickTime = startTickTime;
            combineWorkInfo.FinishTickTime = finishTickTime;
            combineWorkInfo.DurationSecondTime = 
                		(int)utility.GetDurationSecondsTime(startTickTime, finishTickTime, true);
                
            for (int countIndex2 = 0; countIndex2 < workUnitList.size(); ++countIndex2)
            {
            	work_unit workUnit = workUnitList.get(countIndex2);
                	
            	workUnit.StartSecondTime = 
                			((float)(workUnit.GetAbsoluteStartTickTime()  - zeroTickTime)) / 1000.0f;
         
                if (false == 
                		workUnit.ContainSecondTime(combineWorkInfo.StartSecondTime, combineWorkInfo.FinishSecondTime,true))
                   continue;
                    
                if (null == combineWorkInfo.RenderInfoList)
                	combineWorkInfo.RenderInfoList = new ArrayList<combine_render_info>();
   
                    combine_render_info combineRenderInfo = new combine_render_info();
                    combineRenderInfo.SetWorkUnit(workUnit);
                    
                    combineRenderInfo.BetweenStartSecondTime = 
                        		combineWorkInfo.StartSecondTime - workUnit.StartSecondTime;
                        		
                    combineRenderInfo.BetweenDurationSecondTime = 
                        		combineWorkInfo.FinishSecondTime - combineWorkInfo.StartSecondTime;
                    
                    combineWorkInfo.RenderInfoList.add(combineRenderInfo);
                    
                }

                if (null != combineWorkInfo.RenderInfoList)
                {
                    if (2 > combineWorkInfo.GetRealFileCount())
                    {
                        combineWorkInfo = null;
                        continue;
                    }
                    else
                    {
                        //++fileCountIndex;
                    }
                    
                    //1600x1200
                    int userCodeCount = combineWorkInfo.GetUserCodeList().size();
                    int widthCount = 1;
                    int heightCount = 1;
                    int loopCount = 1;
                    while (userCodeCount > loopCount * loopCount)
                    {
                        ++loopCount;
                        widthCount = loopCount;
                    }

                    loopCount = 0;
                    while (0 != (userCodeCount + loopCount) % widthCount)
                    {
                        ++loopCount;
                    }
                    heightCount = (userCodeCount + loopCount) / widthCount;

                    //
                    if (0 == ffmpeg_handler.MergeResolutionWidth && 
                    		0 == ffmpeg_handler.MergeResolutionHeight)
                    {
                        //int fileResolutionWidth = ffprobe_handler.FileResolutionWidth;
                        //int fileResolutionHeight = ffprobe_handler.FileResolutionHeight;

                        ffmpeg_handler.MergeResolutionWidth = 1280;
                        ffmpeg_handler.MergeResolutionHeight = 720;

                        //ffmpeg_handler.ResizeEmptyImage();
                    }

                    for (int countIndex3 = 0; countIndex3 < combineWorkInfo.RenderInfoList.size(); ++countIndex3)
                    {
                    	combine_render_info combineRenderInfo = combineWorkInfo.RenderInfoList.get(countIndex3);
                    	
                        work_unit recordFileInfo = combineRenderInfo.GetWorkUnit();
                        
                        int userCodeCountIndex = 
                        		combineWorkInfo.GetUserCodeList().indexOf(recordFileInfo.UserCode);

                        //interview
                        boolean interviewContent = false;
                        if (true == recordFileInfo.Content.toLowerCase().contains("interview"))
                            interviewContent = true;
                        
                        if (true == interviewContent)
                        {
                            if (0 == combineWorkInfo.RenderWidth)
                                combineWorkInfo.RenderWidth = ffmpeg_handler.MergeResolutionWidth / widthCount / 2;
                            if (0 == combineWorkInfo.RenderHeight)
                                combineWorkInfo.RenderHeight = ffmpeg_handler.MergeResolutionHeight / heightCount;

                            if (true == recordFileInfo.Content.toLowerCase().contains("camera"))
                            {
                                combineWorkInfo.RenderInfoList.get(countIndex3).XPosition =
                                userCodeCountIndex % widthCount * (combineWorkInfo.RenderWidth*2);
                            }
                            else//screen
                            {
                                combineWorkInfo.RenderInfoList.get(countIndex3).XPosition =
                                userCodeCountIndex % widthCount * (combineWorkInfo.RenderWidth*2) + combineWorkInfo.RenderWidth;
                            }

                            combineWorkInfo.RenderInfoList.get(countIndex3).YPosition =
                                userCodeCountIndex / widthCount * combineWorkInfo.RenderHeight;
                        }
                        //apptest
                        else
                        {
                            if (0 == combineWorkInfo.RenderWidth)
                                combineWorkInfo.RenderWidth = ffmpeg_handler.MergeResolutionWidth / widthCount;
                            if (0 == combineWorkInfo.RenderHeight)
                                combineWorkInfo.RenderHeight = ffmpeg_handler.MergeResolutionHeight / heightCount;

                            combineWorkInfo.RenderInfoList.get(countIndex3).XPosition =
                                userCodeCountIndex % widthCount * combineWorkInfo.RenderWidth;

                            combineWorkInfo.RenderInfoList.get(countIndex3).YPosition =
                                userCodeCountIndex / widthCount * combineWorkInfo.RenderHeight;
                        }
                    }
                }
                
                CombineWorkInfoList.add(combineWorkInfo);
            }
            
            //process
            if (null != CombineWorkInfoList)
            {
                //3600s(1h) - 5 - 00:22:44 - 1280x720
                //3600s(1h) - 10 - 00:36:42 - 1280x720
                //3600s(1h) - 20 - 01:07:33 - 1280x720
                //3600s(1h) - 20 - 00:59:45 - 1280x720 - preset ultrafast

                for (countIndex = 0; countIndex < CombineWorkInfoList.size(); ++countIndex)
                {
                    //DateTime nowDateTime = DateTime.Now;
                    //System.out.println("StartTime[" + countIndex + "/" + _combineWorkInfoList.size() + "] - " + _combineWorkInfoList.get(countIndex).OutputFileFullName + " - " + nowDateTime.ToString());
                	
                	combine_work_info combineWorkInfo = CombineWorkInfoList.get(countIndex);
                	
                    ffmpeg_handler.Merge(combineWorkInfo);
                    //TimeSpan timeSpan = DateTime.Now - nowDateTime;
                    //string elapsedTime = timeSpan.ToString();

                    //System.out.println("FinishTime[" + countIndex + "/" + _combineWorkInfoList.size() + "] - " + _combineWorkInfoList.get(countIndex).OutputFileFullName + " - " + DateTime.Now.ToString());
                    //System.out.println("ElapsedTime["+countIndex+"/"+ _combineWorkInfoList.size() + "] - "+ _combineWorkInfoList.get(countIndex).OutputFileFullName+" - "+ elapsedTime);
                    
                    aws_s3_handler.UploadFile(combineWorkInfo);
                    
                    janus_archive_callback_handler.RequestUpdate(combineWorkInfo);
                }

                String resultFileName = "";
                //resultFileName = ConcatFFmpeg(_combineWorkInfoList);

                boolean deleteFileFlag = false;
                if(true == deleteFileFlag)
                {
                    for (countIndex = 0; countIndex < CombineWorkInfoList.size(); ++countIndex)
                    {
                    	combine_work_info combineWorkInfo = CombineWorkInfoList.get(countIndex);
                    	
                    	File deleteFile =
                    			new File(work_manager.GetRecordFolderPathName()+combineWorkInfo.GetMergedFileFullName());
                        deleteFile.delete();
                    }
                }
            }      
    }
    */
    
    
    public static void UploadFile2S3()
    {
    	if(null == MergeWorkInfoList)
    		return;
    	
        for (int countIndex = 0; countIndex < MergeWorkInfoList.size(); ++countIndex)
        {
        	//DateTime nowDateTime = DateTime.Now;
            //System.out.println("StartTime[" + countIndex + "/" + _combineWorkInfoList.size() + "] - " + _combineWorkInfoList.get(countIndex).OutputFileFullName + " - " + nowDateTime.ToString());
            	
            merge_work_info mergeWorkInfo = MergeWorkInfoList.get(countIndex);
            	
            //TimeSpan timeSpan = DateTime.Now - nowDateTime;
            //string elapsedTime = timeSpan.ToString();

            //System.out.println("FinishTime[" + countIndex + "/" + _combineWorkInfoList.size() + "] - " + _combineWorkInfoList.get(countIndex).OutputFileFullName + " - " + DateTime.Now.ToString());
            //System.out.println("ElapsedTime["+countIndex+"/"+ _combineWorkInfoList.size() + "] - "+ _combineWorkInfoList.get(countIndex).OutputFileFullName+" - "+ elapsedTime);
                
            aws_s3_handler.UploadMergedFile(mergeWorkInfo);
                
            //janus_archive_callback_handler.RequestUpdateMergeWorkInfo(mergeWorkInfo);
        }
    }
    
}