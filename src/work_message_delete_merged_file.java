
public class work_message_delete_merged_file extends work_message
{
	merge_work_info _mergeWorkInfo;
	boolean _deleteMergeWorkInfo;
	
	public work_message_delete_merged_file()
	{
		Type = work_message.TYPE_DELETE_MERGED_FILE;
	}
	
	public void Build(merge_work_info mergeWorkInfo,boolean deleteMergeWorkInfo)
	{
		_mergeWorkInfo = mergeWorkInfo;
		_deleteMergeWorkInfo = deleteMergeWorkInfo;
	}
	
	
	public void Process()
	{
		_mergeWorkInfo.DeleteMergedFile(_deleteMergeWorkInfo);
	}
}
