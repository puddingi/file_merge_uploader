
public class work_message_convert_mjr_file  extends work_message
{
	String _convertAudioFileExtend;
	String _convertVideoFileExtend;
	work_unit _workUnit;
	boolean _addDurationInFileName = false;
	boolean _addEndEpochMilliInFileName = true;
	
	public work_message_convert_mjr_file()
	{
		Type = work_message.TYPE_CONVERT_MJR_FILE;
	}
	
	public void Build(work_unit workUnit)
	{
		_workUnit = workUnit;
	}
	
	public void Build(work_unit workUnit,String convertAudioFileExtend,String convertVideoFileExtend)
	{
		_workUnit = workUnit;
		_convertAudioFileExtend = convertAudioFileExtend;
		_convertVideoFileExtend = convertVideoFileExtend;
	}
	
	public void Process()
	{
		log_handler.WriteFile(_workUnit.GetLogFullPathFileName(), "[STATUS_PROCESS_CONVERT_MJR_FILE]");
		
		String convertedVideoFileFullName = "";
		String convertedAudioFileFullName = "";
		String convertedAudioFileFullName2 = "";
		String convertedVideoFileFullName2 = "";
		long durationSecondTime = 0L;
		int fileResolutionWidth = 0;
		int fileResolutionHeight = 0;
		
		if(false == utility.IsNullOrEmpty(_workUnit.MjrVideoFileFullName))
		{
			if(_workUnit.MjrVideoFileSize > work_unit.CHECK_FILE_SIZE)
			{
				String result = 
          		 janus_pp_rec_handler.Check(preset.GetRecordFolderPath() + _workUnit.MjrVideoFileFullName);
			
				convertedVideoFileFullName = _workUnit.GetFileTitleName();
				
				if(result.contains("vp8"))
					convertedVideoFileFullName += ".webm";
		        else if(result.contains("vp9"))
		        	convertedVideoFileFullName += ".webm";
		        else if(result.contains("h264"))
		        	convertedVideoFileFullName += ".mp4";
				
				janus_pp_rec_handler.Convert(
					preset.GetRecordFolderPath() + _workUnit.MjrVideoFileFullName,
					preset.GetRecordFolderPath() + convertedVideoFileFullName);
				
				if(0 == durationSecondTime)
					durationSecondTime = 
						(long)ffprobe_handler.GetFileDurationSecondTime(preset.GetRecordFolderPath()+convertedVideoFileFullName);
			
				if(true == _addDurationInFileName)
				{
					String convertedVideoFileFullReName = "";
					if(convertedVideoFileFullName.contains(".webm"))
					{
						convertedVideoFileFullReName = 
								convertedVideoFileFullName.replace(".webm","_"+durationSecondTime+".webm");
					}
					else if(convertedVideoFileFullName.contains(".mp4"))
					{
						convertedVideoFileFullReName = 
								convertedVideoFileFullName.replace(".mp4","_"+durationSecondTime+".mp4");
					}
					utility.RenameFile(preset.GetRecordFolderPath()+convertedVideoFileFullName, 
							preset.GetRecordFolderPath()+convertedVideoFileFullReName);
					
					convertedVideoFileFullName = convertedVideoFileFullReName;
				}
				
				if(true == _addEndEpochMilliInFileName)
				{
					long finishEpochMilli = _workUnit.GetFinishEpochMilli(durationSecondTime);
					String convertedVideoFileFullReName = "";
					if(convertedVideoFileFullName.contains(".webm"))
					{
						convertedVideoFileFullReName = 
								convertedVideoFileFullName.replace(".webm","_"+finishEpochMilli+".webm");
					}
					else if(convertedVideoFileFullName.contains(".mp4"))
					{
						convertedVideoFileFullReName = 
								convertedVideoFileFullName.replace(".mp4","_"+finishEpochMilli+".mp4");
					}
					utility.RenameFile(preset.GetRecordFolderPath()+convertedVideoFileFullName, 
							preset.GetRecordFolderPath()+convertedVideoFileFullReName);
					
					convertedVideoFileFullName = convertedVideoFileFullReName;
				}
				
			
				ffprobe_handler ffprobeHandler = new ffprobe_handler();
				if(ffprobeHandler.GetFileResolution(preset.GetRecordFolderPath()+convertedVideoFileFullName))
				{
					fileResolutionWidth = ffprobeHandler.FileResolutionWidth;
					fileResolutionHeight = ffprobeHandler.FileResolutionHeight;
				}
				
				if(preset.SERVICE_TYPE_CONVERT2 == preset.ServiceType)
				{
					if(false == utility.IsNullOrEmpty(_convertVideoFileExtend))
					{
						ffmpeg_handler.ConvertVideoFile(_workUnit,convertedVideoFileFullName,_convertVideoFileExtend);
						convertedVideoFileFullName2 = _workUnit.GetFileTitleName()+"."+_convertVideoFileExtend;
					}
				}
				
			}
          
		}
		
		if(false == utility.IsNullOrEmpty(_workUnit.MjrAudioFileFullName))
		{
			if(_workUnit.MjrAudioFileSize > work_unit.CHECK_FILE_SIZE)
			{
				String result = 
          		 janus_pp_rec_handler.Check(preset.GetRecordFolderPath() + _workUnit.MjrAudioFileFullName);
			
				convertedAudioFileFullName = _workUnit.GetFileTitleName();
				if(result.contains("opus"))
					convertedAudioFileFullName += ".opus";
				else if(result.contains("g711"))
					convertedAudioFileFullName += ".wav";
				
				janus_pp_rec_handler.Convert(
						preset.GetRecordFolderPath() + _workUnit.MjrAudioFileFullName,
						preset.GetRecordFolderPath() + convertedAudioFileFullName);
				
				if(0 == durationSecondTime)
					durationSecondTime = 
						(long)ffprobe_handler.GetFileDurationSecondTime(preset.GetRecordFolderPath()+convertedAudioFileFullName);
				
				if(true == _addDurationInFileName)
				{
					String convertedAudioFileFullReName = "";
					if(convertedAudioFileFullName.contains(".opus"))
					{
						convertedAudioFileFullReName = 
								convertedAudioFileFullName.replace(".opus","_"+durationSecondTime+".opus");
					}
					else if(convertedAudioFileFullName.contains(".wav"))
					{
						convertedAudioFileFullReName = 
								convertedAudioFileFullName.replace(".wav","_"+durationSecondTime+".wav");
					}
					utility.RenameFile(preset.GetRecordFolderPath()+convertedAudioFileFullName, 
							preset.GetRecordFolderPath()+convertedAudioFileFullReName);
					
					convertedAudioFileFullName = convertedAudioFileFullReName;
				}
				
				if(true == _addEndEpochMilliInFileName)
				{
					long finishEpochMilli = _workUnit.GetFinishEpochMilli(durationSecondTime);
					String convertedAudioFileFullReName = "";
					if(convertedAudioFileFullName.contains(".opus"))
					{
						convertedAudioFileFullReName = 
								convertedAudioFileFullName.replace(".opus","_"+finishEpochMilli+".opus");
					}
					else if(convertedAudioFileFullName.contains(".wav"))
					{
						convertedAudioFileFullReName = 
								convertedAudioFileFullName.replace(".wav","_"+finishEpochMilli+".wav");
					}
					utility.RenameFile(preset.GetRecordFolderPath()+convertedAudioFileFullName, 
							preset.GetRecordFolderPath()+convertedAudioFileFullReName);
					
					convertedAudioFileFullName = convertedAudioFileFullReName;
				}
				
				
				
				
				if(preset.SERVICE_TYPE_CONVERT2 == preset.ServiceType)
				{
					if(false == utility.IsNullOrEmpty(_convertAudioFileExtend))
					{
						ffmpeg_handler.ConvertAudioFile(_workUnit,convertedAudioFileFullName,_convertAudioFileExtend);
						convertedAudioFileFullName2 = _workUnit.GetFileTitleName()+"."+_convertAudioFileExtend;
					}
				}
				
			}
		}
			
		//
		work_message_set_converted_file_attribute message = 
				new work_message_set_converted_file_attribute();
		message.Build(_workUnit, convertedVideoFileFullName, convertedAudioFileFullName,
				durationSecondTime,fileResolutionWidth,fileResolutionHeight,
				convertedAudioFileFullName2,convertedVideoFileFullName2);
		try 
		{
			work_message.QueueCheck.put(message);
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
