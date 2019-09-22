
public class merge_render_info
{
	public int XPosition;
	public int YPosition;
	public int Width;
	public int Height;
	
	public long BetweenStartSecondTime;
	
	private  work_unit _workUnit;
	
	public int AudioCountIndex = -1;
	public int VideoCountIndex = -1;
	
	public void SetWorkUnit(work_unit workUnit)
	{
		_workUnit = workUnit;
	}
	
	public work_unit GetWorkUnit()
	{
		return _workUnit;
	}
	
	public String GetVideoConvertedFileFullName()
	{
		return _workUnit.ConvertedVideoFileFullName;
	}
	
	public String GetAudioConvertedFileFullName()
	{
		return _workUnit.ConvertedAudioFileFullName;
	}
	
	public String GetMuxedFileFullName()
	{
		return _workUnit.GetMuxedFileFullName();
	}
	
	public String GetEnv() 
	{
		return _workUnit.Env;
	}
	
	public String GetProjectID()
	{
		return _workUnit.Projectid;
	}
	
	
	public String GetInterviewID()
	{
		return _workUnit.Interviewid;
	}
	    
	public String GetContent()
	{
		return _workUnit.Content;
	}
}