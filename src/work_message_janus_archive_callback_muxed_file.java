
public class work_message_janus_archive_callback_muxed_file  extends work_message
{
	private work_unit _workUnit;
	
	public work_message_janus_archive_callback_muxed_file()
	{
		Type = work_message.TYPE_JANUS_ARCHIVE_CALLBACK_MUXED_FILE;
	}
	
	public void BuildWorkUnit(work_unit workUnit)
	{
		_workUnit = workUnit;	
	}
	
	public void Process()
	{
		switch(_workUnit.GetProcessStatus())
		{
			case work_unit.STATUS_PROCESS_CALL_REQUEST_CONVERTED_FILE:
			{
				janus_archive_callback_handler.RequestUpdateConvert(_workUnit);
				_workUnit.SetProcessStatus(work_unit.STATUS_FINISH_CALL_REQUEST_CONVERTED_FILE);
			}
			break;
			
			case work_unit.STATUS_PROCESS_CALL_REQUEST_MUXED_FILE:
			{
				janus_archive_callback_handler.RequestUpdateMux(_workUnit);
				_workUnit.SetProcessStatus(work_unit.STATUS_FINISH_CALL_REQUEST_MUXED_FILE);
			}
			break;	
		}			
	}
}