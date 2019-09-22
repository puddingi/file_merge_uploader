
public class work_message_upload_muxed_file_to_s3  extends work_message
{
	private work_unit _workUnit;
	
	public work_message_upload_muxed_file_to_s3()
	{
		Type = work_message.TYPE_UPLOAD_MUXED_FILE_TO_S3;
	}
	
	public void Build(work_unit workUnit)
	{
		_workUnit = workUnit;	
	}
	
	public void Process()
	{
		aws_s3_handler.UploadMuxedFile(_workUnit);
		_workUnit.SetProcessStatus(work_unit.STATUS_FINISH_UPLOAD_MUXED_FILE);
	}
}