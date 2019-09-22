
public class work_message_delete_muxed_file extends work_message
{
	work_unit _workUnit;
	boolean _deleteWorkUnit;
	
	public work_message_delete_muxed_file()
	{
		Type = work_message.TYPE_DELETE_MUXED_FILE;
	}
	
	public void Build(work_unit workUnit,boolean deleteWorkUnit)
	{
		_workUnit = workUnit;
		_deleteWorkUnit = deleteWorkUnit;
	}
	
	public void Process()
	{
		_workUnit.DeleteMuxedFile(_deleteWorkUnit);
	}
}
