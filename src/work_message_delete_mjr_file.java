
public class work_message_delete_mjr_file extends work_message
{
	work_unit _workUnit;
	
	public work_message_delete_mjr_file()
	{
		Type = work_message.TYPE_DELETE_MJR_FILE;
	}
	
	public void Build(work_unit workUnit)
	{
		_workUnit = workUnit;
	}
	
	
	public void Process()
	{
		_workUnit.DeleteMjrFile();
	}
}
