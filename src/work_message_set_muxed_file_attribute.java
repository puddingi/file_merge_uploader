
public class work_message_set_muxed_file_attribute extends work_message
{
	work_unit _workUnit;
	
	
	public work_message_set_muxed_file_attribute()
	{
		Type = work_message.TYPE_SET_MUXED_FILE_ATTRIBUTE;
	}
	
	public void Build(work_unit workUnit)
	{
		_workUnit = workUnit;
	}
	
	public void Process()
	{
		_workUnit.SetProcessStatus(work_unit.STATUS_FINISH_MUX_CONVERTED_FILE);
	}

}
