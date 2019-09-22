
public class work_message_set_converted_file_attribute extends work_message
{
	work_unit _workUnit;
	String _convertedVideoFileFullName;
	String _convertedAudioFileFullName;
	String _convertedAudioFileFullName2;
	String _convertedVideoFileFullName2;
	long _durationSecondTime;
	int _fileResolutionWidth;
	int _fileResolutionHeight;
	
	public work_message_set_converted_file_attribute()
	{
		Type = work_message.TYPE_SET_CONVERTED_FILE_ATTRIBUTE;
	}
	
	public void Build(work_unit workUnit,String convertedVideoFileFullName,String convertedAudioFileFullName,
			long durationSecondTime,int fileResolutionWidth,int fileResolutionHeight,
			String convertedAudioFileFullName2,String convertedVideoFileFullName2)
	{
		_workUnit = workUnit;
		
		_convertedVideoFileFullName = convertedVideoFileFullName;
		_convertedAudioFileFullName = convertedAudioFileFullName;
		_convertedAudioFileFullName2 = convertedAudioFileFullName2;
		_convertedVideoFileFullName2 = convertedVideoFileFullName2;
		_durationSecondTime = durationSecondTime;
		_fileResolutionWidth = fileResolutionWidth;
		_fileResolutionHeight = fileResolutionHeight;
	}
	
	public void Process()
	{
		_workUnit.ConvertedAudioFileFullName = _convertedAudioFileFullName;
		_workUnit.ConvertedVideoFileFullName = _convertedVideoFileFullName;
		_workUnit.ConvertedAudioFileFullName2 = _convertedAudioFileFullName2;
		_workUnit.ConvertedVideoFileFullName2 = _convertedVideoFileFullName2;
		_workUnit.SetDurationSecondsTime(_durationSecondTime);
		_workUnit.SetFileResolution(_fileResolutionWidth,_fileResolutionHeight);
		
		_workUnit.SetProcessStatus(work_unit.STATUS_FINISH_CONVERT_MJR_FILE);
	}

}
