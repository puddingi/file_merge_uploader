
public class work_message_upload_merged_file_to_s3  extends work_message
{
	private merge_work_info _mergeWorkInfo;
	
	public work_message_upload_merged_file_to_s3()
	{
		Type = work_message.TYPE_UPLOAD_MERGED_FILE_TO_S3;
	}
	
	public void Build(merge_work_info mergeWorkInfo)
	{
		_mergeWorkInfo = mergeWorkInfo;	
	}
	
	public void Process()
	{
		aws_s3_handler.UploadMergedFile(_mergeWorkInfo);
		
		_mergeWorkInfo.SetProcessStatus(merge_work_info.STATUS_FINISH_UPLOAD_MERGED_FILE);
	}
}