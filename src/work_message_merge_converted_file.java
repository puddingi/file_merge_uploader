
public class work_message_merge_converted_file  extends work_message
{
	private merge_work_info _mergeWorkInfo;
	
	public work_message_merge_converted_file()
	{
		Type = work_message.TYPE_MERGE_CONVERTED_FILE;
	}
	
	public void Build(merge_work_info mergeWorkInfo)
	{
		_mergeWorkInfo = mergeWorkInfo;	
	}
	
	public void Process()
	{
		ffmpeg_handler.MergeConvertedFile(_mergeWorkInfo);
		
		_mergeWorkInfo.SetProcessStatus(merge_work_info.STATUS_FINISH_MERGE_CONVERTED_FILE);
	}
}