

public class work_message_upload_converted_file_to_s3  extends work_message
{
	private work_unit _workUnit;
	
	public work_message_upload_converted_file_to_s3()
	{
		Type = work_message.TYPE_UPLOAD_CONVERTED_FILE_TO_S3;
	}
	
	public void BuildWorkUnit(work_unit workUnit)
	{
		_workUnit = workUnit;
	}
	
	public void Process()
	{
		log_handler.WriteFile(_workUnit.GetLogFullPathFileName(), "[STATUS_PROCESS_UPLOAD_CONVERTED_FILE]");
		
		aws_s3_handler.UploadConvertedFile(_workUnit);
		_workUnit.SetProcessStatus(work_unit.STATUS_FINISH_UPLOAD_CONVERTED_FILE);
			
	}
}