
public class work_message_janus_archive_callback_merged_file  extends work_message
{
	private merge_work_info _mergeWorkInfo;
	
	
	public work_message_janus_archive_callback_merged_file()
	{
		Type = work_message.TYPE_JANUS_ARCHIVE_CALLBACK_MERGED_FILE;
	}
	
	public void BuildMergeWorkInfo(merge_work_info mergeWorkInfo)
	{
		_mergeWorkInfo = mergeWorkInfo;	
	}
	
	public void Process()
	{
		janus_archive_callback_handler.RequestUpdateMerge(_mergeWorkInfo);
		_mergeWorkInfo.SetProcessStatus(merge_work_info.STATUS_FINISH_CALL_REQUEST_MERGED_FILE);
	}
}