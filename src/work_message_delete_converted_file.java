
public class work_message_delete_converted_file extends work_message
{
	work_unit _workUnit;
	boolean _deleteWorkUnit;
	boolean _deleteMjrFile;
	
	public work_message_delete_converted_file()
	{
		Type = work_message.TYPE_DELETE_CONVERTED_FILE;
	}
	
	public void Build(work_unit workUnit,boolean deleteWorkUnit,boolean deleteMjrFile)
	{
		_workUnit = workUnit;
		_deleteWorkUnit = deleteWorkUnit;
		_deleteMjrFile = deleteMjrFile;
	}
	
	
	public void Process()
	{
		if(true == _deleteMjrFile)
			_workUnit.DeleteMjrFile();
		
		_workUnit.DeleteConvertedFile(_deleteWorkUnit);
		_workUnit.SetProcessStatus(work_unit.STATUS_FINISH_DELETE_MJR_CONVERTED_FILE);
	}
}
