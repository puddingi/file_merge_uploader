import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


// http://sehaeng.blogspot.com/2015/02/ubuntu-xvfb.html
//sudo apt-get install xvfb
//Xvfb :100 -ac &
//DISPLAY=:100 java -jar ~/file_merge_uploader/FileMergeUploader.jar Main


public class work_manager 
{	
	public static ArrayList<work_manager> List;
	
	public ArrayList<work_unit> WorkUnitList;
	ArrayList<merge_work_info> _mergeWorkInfoList;
	
	public static ArrayList<String> CurrenSearchedFileNameList;
	
	public String GetProjectID()
	{
		if(null == WorkUnitList)
			return null;
		if(0 == WorkUnitList.size())
			return null;
		return WorkUnitList.get(0).Projectid;
	}
	
	public String GetInterviewID()
	{
		if(null == WorkUnitList)
			return null;
		if(0 == WorkUnitList.size())
			return null;
		return WorkUnitList.get(0).Interviewid;
	}
	
	private merge_work_info _AddMergeWorkInfo(long startEpochMilli,long finishEpochMilli)
	{
		if (null == _mergeWorkInfoList)
        	_mergeWorkInfoList = new ArrayList<merge_work_info>();
		
		merge_work_info mergeWorkInfo = new merge_work_info();       
        mergeWorkInfo.StartEpochMilli = startEpochMilli;
        mergeWorkInfo.FinishEpochMilli = finishEpochMilli;
        
        _mergeWorkInfoList.add(mergeWorkInfo);
        return mergeWorkInfo;
	}
	
	private merge_work_info _SearchMergeWorkInfo(long startEpochMilli,long finishEpochMilli)
	{
		if(null == _mergeWorkInfoList)
			return null;
		
		for(int countIndex=0; countIndex<_mergeWorkInfoList.size(); ++countIndex)
		{
			merge_work_info mergeWorkInfo = _mergeWorkInfoList.get(countIndex);
			if(startEpochMilli == mergeWorkInfo.StartEpochMilli && 
					finishEpochMilli == mergeWorkInfo.FinishEpochMilli)
				return mergeWorkInfo;
		}
		
		return null;
	}
	
	private merge_work_info _SearchAddMergeWorkInfo(long startEpochMilli,long finishEpochMilli)
	{
		merge_work_info mergeWorkInfo = _SearchMergeWorkInfo(startEpochMilli,finishEpochMilli);
		if(null != mergeWorkInfo)
			return mergeWorkInfo;
		
		mergeWorkInfo = _AddMergeWorkInfo(startEpochMilli,finishEpochMilli);
		return mergeWorkInfo;
	}
	
	
	public int SerchAddMjrFile(File mjrFile)
	{
    	String mjrFileFullName = mjrFile.getName();
    	//if(mjrFileFullName.contains("-data."))
		//	return -1;
    		
    	//dev_f3dbycW9Hp_9bRHGFDob4_interview@camera_customer_xrEYZCYIyE_0_1558462071-video.mjr 
    	if(null != WorkUnitList)
    	{
    		for(int countIndex=0; countIndex<WorkUnitList.size(); ++countIndex) 
    		{
    			work_unit workUnit = WorkUnitList.get(countIndex);
    			if(workUnit.ExistMjrFileFullName(mjrFileFullName))
    				return countIndex;
    		
    			String [] splits = mjrFile.getName().split("-");
    			if(workUnit.ContainMjrFileFullName(splits[0]))
    			{
    				if(splits[1].contains("video"))
    				{
    					workUnit.MjrVideoFileFullName = mjrFile.getName();
    				}
    				else if(splits[1].contains("audio"))
    				{
    					workUnit.MjrAudioFileFullName = mjrFile.getName();
    				}
    				else if(splits[1].contains("data"))
    				{
    					workUnit.MjrDataFileFullName = mjrFile.getName();
    				}
    			
    				return countIndex;
    			}
    		}	
    	}
    	
    	
		String [] fileTitleName = mjrFileFullName.split("\\.");
		String [] splits = fileTitleName[0].split("_");
		if (null == splits || 8 != splits.length)
            return -1;
		
		String [] splits2 = splits[splits.length-1].split("-");
		
    	work_unit workUnit = new work_unit();
		if(mjrFileFullName.contains("video"))
		{
			workUnit.MjrVideoFileFullName = mjrFile.getName();
		}
		else if(mjrFileFullName.contains("audio"))
		{
			workUnit.MjrAudioFileFullName = mjrFile.getName();
		}
		else if(splits[1].contains("data"))
		{
			workUnit.MjrDataFileFullName = mjrFile.getName();
		}
		
		workUnit.Env = splits[0];
		workUnit.Projectid = splits[1];
		workUnit.Interviewid = splits[2];
		workUnit.Content = splits[3];
		workUnit.UserType = splits[4];
		workUnit.UserCode = splits[5];
		workUnit.CommandType = Integer.parseInt(splits[6]);
		workUnit.SetStartEpochMilli(Long.parseLong(splits2[0]));
		
		if(null == WorkUnitList)
			WorkUnitList = new ArrayList<work_unit>();
			
		WorkUnitList.add(workUnit);
		return WorkUnitList.size()-1;
    }
	
	
	public work_unit SearchByMjrFileFullName(String mjrFileFullName)
    {
		if(null == WorkUnitList)
			return null;
		
        for (int countIndex = 0; countIndex < WorkUnitList.size(); ++countIndex)
        {
        	work_unit workUnit = WorkUnitList.get(countIndex);
            
        	if(false == utility.IsNullOrEmpty(workUnit.MjrVideoFileFullName))
        	{
        		if (true == workUnit.MjrVideoFileFullName.equals(mjrFileFullName))
        			return workUnit;
        	}
        	
        	if(false == utility.IsNullOrEmpty(workUnit.MjrAudioFileFullName))
        	{
        		if (true == workUnit.MjrAudioFileFullName.equals(mjrFileFullName))
        			return workUnit;
        	}
        	
        	if(false == utility.IsNullOrEmpty(workUnit.MjrDataFileFullName))
        	{
        		if (true == workUnit.MjrDataFileFullName.equals(mjrFileFullName))
        			return workUnit;
        	}
        }
        
        return null;
    }
	
	public int Update()
	{
		if(null == WorkUnitList)
			return 0;
		
		ArrayList<Long> epochMilliList = null;
		int countIndex=0;
		for(countIndex=0; countIndex<WorkUnitList.size(); ++countIndex)
		{
			work_unit workUnit = WorkUnitList.get(countIndex);
			if(work_unit.STATUS_DESTROY_SELF == workUnit.GetProcessStatus())
			{
				WorkUnitList.remove(countIndex);
				countIndex = 0;
				continue;
			}
			
			workUnit.Update();
			
			if(null == epochMilliList)
				epochMilliList = new ArrayList<Long>();
			
			if (-1 == epochMilliList.indexOf(workUnit.GetStartEpochMilli()))
				epochMilliList.add(workUnit.GetStartEpochMilli());

			if(0L != workUnit.GetDurationSecondsTime())
			{
				if (-1 == epochMilliList.indexOf(workUnit.GetFinishEpochMilli()))
					epochMilliList.add(workUnit.GetFinishEpochMilli());
			}
		}
	
		if(null == epochMilliList)
			return 1;
		
		Collections.sort(epochMilliList); 
		for (int countIndex7 = 0; countIndex7 < epochMilliList.size()-1; ++countIndex7)
        {
			long startEpochMilli = epochMilliList.get(countIndex7);
            long finishEpochMilli = epochMilliList.get(countIndex7+1);
            
            ArrayList<work_unit> workUnitList = null;
            for(int countIndex8=0; countIndex8<WorkUnitList.size(); ++countIndex8)
    		{
    			work_unit workUnit = WorkUnitList.get(countIndex8);
    			if (false == workUnit.ContainEpochMilli(startEpochMilli,finishEpochMilli,true))
                	continue;
    			
    			if(null == workUnitList)
    				workUnitList = new ArrayList<work_unit>();
    				
    			workUnitList.add(workUnit);
    		}
            
            if(null == workUnitList)
            	continue;
            
            boolean checked = true;
            for(int countIndex10=0; countIndex10<workUnitList.size(); ++countIndex10)
            {
            	work_unit workUnit = workUnitList.get(countIndex10);
            	if(0L == workUnit.GetDurationSecondsTime())
            	{
            		checked = false;
            		break;
            	}
            }
            	
            if(false == checked)
            {
            	log_handler.WriteConsole("false == checked");
            	continue;
            }
            	
            
            switch(preset.ServiceType)
            {
            case preset.SERVICE_TYPE_CONVERT:
            case preset.SERVICE_TYPE_CONVERT2:
            case preset.SERVICE_TYPE_CONVERT_MUX:
            {}
            break;
            
            case preset.SERVICE_TYPE_CONVERT_MERGE:
            case preset.SERVICE_TYPE_MERGE_TOPDOWN:
            {
            	merge_work_info mergeWorkInfo = 
                		_SearchAddMergeWorkInfo(startEpochMilli,finishEpochMilli);
                
                for(int countIndex20=0; countIndex20<workUnitList.size(); ++countIndex20)
                {
                	work_unit workUnit = workUnitList.get(countIndex20);
                	mergeWorkInfo.SearchAddMergeRenderInfoEpochMilli(workUnit,
                			startEpochMilli,finishEpochMilli);
                }   
            }
            break;
            }
            
            //log_handler.PrintConsole("WorkUnitCount : "+workUnitList.size()+" , mergeWorkInfo : "+mergeWorkInfo.RenderInfoList.size());
        }
		
		/*
		for (countIndex = 0; countIndex < tickTimeList.size()-1; ++countIndex)
        {
			long startTickTime = tickTimeList.get(countIndex);
            long finishTickTime = tickTimeList.get(countIndex+1);
			
        	if (null == _mergeWorkInfoList)
        		_mergeWorkInfoList = new ArrayList<merge_work_info>();
				
        	merge_work_info mergeWorkInfo = _SearchMergeWorkInfo(startTickTime,finishTickTime);
        	if(null == mergeWorkInfo)
        	{
        		mergeWorkInfo = new merge_work_info();
            
        		//combineWorkInfo.StartSecondTime = ((float)(startTickTime - zeroTickTime)) / 1000.0f;
        		//combineWorkInfo.FinishSecondTime = ((float)(finishTickTime - zeroTickTime)) / 1000.0f;
        		mergeWorkInfo.StartTickTime = startTickTime;
        		mergeWorkInfo.FinishTickTime = finishTickTime;
        		mergeWorkInfo.DurationSecondTime = 
                		(int)utility.GetDurationSecondsTime(startTickTime, finishTickTime, true);
        		
        		_mergeWorkInfoList.add(mergeWorkInfo);
        	}
			
            //float startSecondTime = ((float)(startTickTime - zeroTickTime)) / 1000.0f;
            //float finishSecondTime = ((float)(finishTickTime - zeroTickTime)) / 1000.0f;
                          
            for (int countIndex2 = 0; countIndex2 < WorkUnitList.size(); ++countIndex2)
            {
            	work_unit workUnit = WorkUnitList.get(countIndex2);
            	
            	//workUnit.StartSecondTime = 
                //			((float)(workUnit.GetAbsoluteStartTickTime() - zeroTickTime)) / 1000.0f;
            		  	
                //if (false == workUnit.ContainSecondTime(startSecondTime,finishSecondTime,true))
                //	continue;
            	
            	if (false == workUnit.ContainTickTime(startTickTime,finishTickTime,true))
                    	continue;
                
                //combineWorkInfo.SearchAddCombineRenderInfo(workUnit,
                //		combineWorkInfo.StartSecondTime,combineWorkInfo.FinishSecondTime);
            	
            	mergeWorkInfo.SearchAddMergeRenderInfoTickTime(workUnit,
            			startTickTime,finishTickTime);
            }
        }
        */
		
		if(null == _mergeWorkInfoList)
		{
			//log_handler.PrintConsole("null == _mergeWorkInfoList");
			return 2;
		}
		
		for(int mergeWorkInfoCountIndex=0; mergeWorkInfoCountIndex<_mergeWorkInfoList.size(); ++mergeWorkInfoCountIndex)
		{
			merge_work_info mergeWorkInfo = _mergeWorkInfoList.get(mergeWorkInfoCountIndex);
			mergeWorkInfo.Update();
		}
		
		return 0;
	}
	
	
	public static work_manager SearchWorkManager(work_unit workUnit)
	{
		for(int countIndex=0; countIndex<List.size(); ++countIndex)
		{
			work_manager workManager = List.get(countIndex);
			if(workManager.GetProjectID().equals(workUnit.Projectid) && 
					workManager.GetInterviewID().equals(workUnit.Interviewid))
			{
				return workManager;
			}
		}
		
		return null;
	}
	
	public static void BuildMergeList(ArrayList<File> searchedMjrFileList)
	{
		if(null != searchedMjrFileList)
		{
			for(int countIndex0=0; countIndex0<searchedMjrFileList.size(); ++countIndex0)
			{
				File mjrFile = searchedMjrFileList.get(countIndex0);
			
				String mjrFileFullName = mjrFile.getName();
				String [] splits = mjrFileFullName.split("_");
			
				if(null == List)
					List = new ArrayList<work_manager>();
			
				boolean searchAddFile = false;
				for(int countIndex1=0; countIndex1<List.size(); ++countIndex1)
				{
					work_manager workManager = List.get(countIndex1);
					if(true == workManager.GetProjectID().equals(splits[1]) &&
							true == workManager.GetInterviewID().equals(splits[2]))
					{
						workManager.SerchAddMjrFile(mjrFile);
						searchAddFile = true;
						break;
					}
				}	
					
				if(true == searchAddFile)
					continue;
				
				work_manager newWorkManager = new work_manager();
				int indexOf = newWorkManager.SerchAddMjrFile(mjrFile);
				List.add(newWorkManager);
			}
		}
		
		if(null == List)
			return;
			
		//update
		for(int countIndex2=0; countIndex2<List.size(); ++countIndex2)
		{
			work_manager workManager = List.get(countIndex2);
			if(1 == workManager.Update())
			{
				List.remove(countIndex2);
				countIndex2 = 0;
			}
		}	
	}
	
	/*
	public static void SearchAddWorkManager(ArrayList<work_unit> workList)
	{
		if(null == workList)
			return;
		
		for(int countIndex=0; countIndex<workList.size(); ++countIndex)
		{
			work_unit checkWork = workList.get(countIndex);
			SearchAddWorkManager(checkWork);
		}
	}
	*/
	
	/*
	public static void Update2()
	{
		if(null == List)
			return;
		
		for(int countIndex=0; countIndex<List.size(); ++countIndex)
		{
			work_manager workManager = List.get(countIndex);
			if(null == workManager.WorkUnitList)
				continue;
				
			work_unit.ConvertMjrFile(workManager.WorkUnitList);
	    	work_unit.MuxConvertedFile(workManager.WorkUnitList);
	  		combine_work_info.MergeUploadCall(workManager.WorkUnitList);
		}
	}
	
	public boolean MergeCheck()
	{
		if(null != WorkList)
		{
			ArrayList<combine_work_info> CombineWorkInfoList = null;
			ArrayList<Long> TickTimeList = null;
			for(int countIndex=0; countIndex<WorkList.size(); ++countIndex)
			{
				if(null == TickTimeList)
					TickTimeList = new ArrayList<Long>();
				
				work checkWork = WorkList.get(countIndex);
				
				if(-1 == TickTimeList.indexOf(checkWork.GetAbsoluteStartTickCountTime()))
					TickTimeList.add(checkWork.GetAbsoluteStartTickCountTime());
				
				if(checkWork.EnableMerge())
				{
					if(-1 == TickTimeList.indexOf(checkWork.GetAbsoluteFinishTickCountTime()))
						TickTimeList.add(checkWork.GetAbsoluteFinishTickCountTime());
				}
			}
			
			if(null != TickTimeList)
			{
				Collections.sort(TickTimeList);
				
				int fileCountIndex = 0;
				for (int countIndex = 0; countIndex < TickTimeList.size()-1; ++countIndex)
				{
					long time0 = TickTimeList.get(countIndex);
					long time1 = TickTimeList.get(countIndex+1);
					
					for(int countIndex2=0; countIndex2<WorkList.size(); ++countIndex2)
					{
						work checkWork = WorkList.get(countIndex2);
						
						if(checkWork.ContainTickTime(time0,time1))
						{
							if (null == CombineWorkInfoList)
			                	CombineWorkInfoList = new ArrayList<combine_work_info>();
								
			                combine_work_info combineWorkInfo = new combine_work_info();
			                combineWorkInfo.StartSecondTime = secondTimeList.get(countIndex);
			                combineWorkInfo.FinishSecondTime = secondTimeList.get(countIndex+1);
			                combineWorkInfo.OutputFileFullName = "archive_" + fileCountIndex + ".mp4";
			                    
			                for (int countIndex3 = 0; countIndex3 < record_file_info.RecordFileInfoList.size(); ++countIndex3)
			                {
			                	if (true == record_file_info.RecordFileInfoList.get(countIndex2).ContainSecondTime(combineWorkInfo.StartSecondTime, combineWorkInfo.FinishSecondTime))
			                    {
			                		if (null == combineWorkInfo.RenderInfoList)
			                			combineWorkInfo.RenderInfoList = new ArrayList<combine_render_info>();

			                        combine_render_info combineRenderInfo = new combine_render_info();
			                        combineRenderInfo.RecordFileFullName = 
			                            		record_file_info.RecordFileInfoList.get(countIndex2).FileFullName;
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
			                     if (0 == combine_work_info.ResizedResolutionWidth && 
			                    		 0 == combine_work_info.ResizedResolutionHeight)
			                     {
			                    	 //int fileResolutionWidth = ffprobe_handler.FileResolutionWidth;
			                    	 //int fileResolutionHeight = ffprobe_handler.FileResolutionHeight;

			                    	 combine_work_info.ResizedResolutionWidth = 1280;
			                    	 combine_work_info.ResizedResolutionHeight = 720;

			                         //ffmpeg_handler.ResizeEmptyImage();
			                     }

			                     for (int countIndex3 = 0; countIndex3 < combineWorkInfo.RenderInfoList.size(); ++countIndex3)
			                     {
			                    	 record_file_info recordFileInfo = 
			                            		record_file_info.SearchRecordFileInfo(
			                            				combineWorkInfo.RenderInfoList.get(countIndex3).RecordFileFullName,
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
			                        		 combineWorkInfo.RenderWidth = combine_work_info.ResizedResolutionWidth / widthCount / 2;
			                             if (0 == combineWorkInfo.RenderHeight)
			                            	 combineWorkInfo.RenderHeight = combine_work_info.ResizedResolutionHeight / heightCount;

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
			                        		 combineWorkInfo.RenderWidth = combine_work_info.ResizedResolutionWidth / widthCount;
			                        	 if (0 == combineWorkInfo.RenderHeight)
			                        		 combineWorkInfo.RenderHeight = combine_work_info.ResizedResolutionHeight / heightCount;

			                        	 combineWorkInfo.RenderInfoList.get(countIndex3).XPosition =
			                        			 userCodeCountIndex % widthCount * combineWorkInfo.RenderWidth;

			                        	 combineWorkInfo.RenderInfoList.get(countIndex3).YPosition =
			                        			 userCodeCountIndex / widthCount * combineWorkInfo.RenderHeight;
			                         }
			                     }
			                    }
			                    
			                    CombineWorkInfoList.add(combineWorkInfo);
			    			 
			    			
			    			return false;	
			                	
						}
					}
				}
			}
			
			//process
            if (null != CombineWorkInfoList)
            {
                //3600s(1h) - 5 - 00:22:44 - 1280x720
                //3600s(1h) - 10 - 00:36:42 - 1280x720
                //3600s(1h) - 20 - 01:07:33 - 1280x720
                //3600s(1h) - 20 - 00:59:45 - 1280x720 - preset ultrafast

                for (int countIndex = 0; countIndex < CombineWorkInfoList.size(); ++countIndex)
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
                    for (int countIndex = 0; countIndex < CombineWorkInfoList.size(); ++countIndex)
                    {
                    	File deleteFile = new File(CombineWorkInfoList.get(countIndex).OutputFileFullName);
                        deleteFile.delete();
                    	//File.Delete(combineWorkInfoList.get(countIndex).OutputFileFullName);
                    }
                }

                //if(false == string.IsNullOrEmpty(resultFileName))
                //    RunPlayResultFile(resultFileName);
            }
			
		}
		return false;
	}
	*/
	
	public static String [] GetFirstCheckFileExtend()
	{
		switch(preset.ServiceType)
		{
		case preset.SERVICE_TYPE_MERGE_TOPDOWN:
			return new String[] {".mp4",".m4a"};
		
		default:
			break;
		}
		
		return new String[] {".mjr"};
	}
	
	public static void Test()
	{
		ArrayList<String> retiveFileNameList = 
				aws_s3_handler.RetriveFileName(preset.GetInterviewBucketName(),
				"dev","f3dbycW9Hp","wNc60LgZtU");
		

		ArrayList<String> downloadFileNameList = null;
		if(null != main_argument.StreamUserInfoList)
		{
			for(int countIndex=0; countIndex<main_argument.StreamUserInfoList.size(); ++countIndex)
			{
				
			}
		}
		
		
		aws_s3_handler.DownloadFile(preset.GetInterviewBucketName(), 
				"dev","f3dbycW9Hp","wNc60LgZtU",downloadFileNameList);
		
		//
		//log_handler.PrintConsole("start retrive file s3");
		//aws_s3_handler.RetriveFile(work_manager.GetInterviewBucketName(),
		//		"s3BucketFolderName");
		//log_handler.PrintConsole("finish retrive file s3");
		
		//
		//log_handler.PrintConsole("start download file s3");
		//aws_s3_handler.DownloadFile(work_manager.GetInterviewBucketName(), 
		//		"s3BucketFolderName");
		//log_handler.PrintConsole("finish download file s3");
	}
	
	
}
