import java.io.File;
import java.util.ArrayList;

public class check_file_changed_status_thread implements Runnable
{
	delay_timer _delayTimer = new delay_timer();
	long _sleepTickTime = 2000L;
	
	int _GetWorkMessgeSize()
	{
		return work_message.QueueCheck.size();
	}
	
	work_message _PeekWorkMessage()
	{
		return work_message.QueueCheck.peek();
	}
	
	void _TakeWorkMessage()
	{
		try
		{
			work_message.QueueCheck.take();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		try
		{	
			boolean loop = true;
			while(true == loop)
			{
				while(_GetWorkMessgeSize() > 0)
				{
					work_message workMessage = _PeekWorkMessage();
					switch(workMessage.Type)
					{
						case work_message.TYPE_SET_CONVERTED_FILE_ATTRIBUTE:
						{
							work_message_set_converted_file_attribute message = 
									(work_message_set_converted_file_attribute)workMessage;
							message.Process();
							
							_TakeWorkMessage();
						}
						break;
						
						case work_message.TYPE_SET_MUXED_FILE_ATTRIBUTE:
						{
							work_message_set_muxed_file_attribute message = 
									(work_message_set_muxed_file_attribute)workMessage;
							message.Process();
							
							_TakeWorkMessage();
						}
						break;
					
						case work_message.TYPE_DELETE_MJR_FILE:
						{
							work_message_delete_mjr_file workMessageDeleteMjrFile = 
									(work_message_delete_mjr_file)workMessage;
							workMessageDeleteMjrFile.Process();
						
							_TakeWorkMessage();
						}
						break;
						
						case work_message.TYPE_DELETE_CONVERTED_FILE:
						{
							work_message_delete_converted_file workMessageDeleteConvertedFile = 
									(work_message_delete_converted_file)workMessage;
							workMessageDeleteConvertedFile.Process();
						
							_TakeWorkMessage();
						}
						break;
						
						case work_message.TYPE_QUIT_THREAD:
						{
							loop = false;
							
							log_handler.WriteConsole("[check_file_changed_status_thread::run] - message : TYPE_QUIT_THREAD_CHECK_FILE_CHANGED_STATUS");
							
							_TakeWorkMessage();
							
							//
							work_message_quit_thread workMessageQuitThread = 
		        					new work_message_quit_thread();
		        			
		        			try 
		        			{
		        				work_message.QueueConvert.put(workMessageQuitThread);
		        			} 
		        			catch (InterruptedException e) 
		        			{
		        				// TODO Auto-generated catch block
		        				e.printStackTrace();
		        			}
						}
						break;
					}
				}
				
	    		ArrayList<File> searchedMjrFileList = null;
	    		if(_delayTimer.ElapsedTickCount(preset.GetWaitCheckFileTickTime()))
	    		{
	    			searchedMjrFileList =  utility.GetListFilesForFolder(preset.GetRecordFolderPath(),
	    		  					work_manager.GetFirstCheckFileExtend()); 
	    		
	    			int searchedMjrFileListCountIndex = 0;
    				int deleteMjrFileNameListCountIndex = 0;
    				
	    			if(null != searchedMjrFileList && null != work_manager.CurrenSearchedFileNameList)
	    			{
	    				searchedMjrFileListCountIndex = 0;
	    				deleteMjrFileNameListCountIndex = 0;
	    				for(searchedMjrFileListCountIndex=0; searchedMjrFileListCountIndex<searchedMjrFileList.size();++searchedMjrFileListCountIndex)
	    				{
	    					File file = searchedMjrFileList.get(searchedMjrFileListCountIndex);
	    					String fileFullName = file.getName();
	    					
	    					for(int countIndex2=0; countIndex2<work_manager.CurrenSearchedFileNameList.size(); ++countIndex2)
	    					{
	    						if(fileFullName.equals(work_manager.CurrenSearchedFileNameList.get(countIndex2)))
	    						{
	    							searchedMjrFileList.remove(searchedMjrFileListCountIndex);
	    								
	    							searchedMjrFileListCountIndex=0;
	    							break;
	    						}
	    					}				
	    				}
	    			
	    				searchedMjrFileListCountIndex=0;
	    				deleteMjrFileNameListCountIndex = 0;
						for(deleteMjrFileNameListCountIndex=0; deleteMjrFileNameListCountIndex<work_manager.CurrenSearchedFileNameList.size(); ++deleteMjrFileNameListCountIndex)
						{
							boolean searched = false;
							
							for(searchedMjrFileListCountIndex=0; searchedMjrFileListCountIndex<searchedMjrFileList.size();++searchedMjrFileListCountIndex)
			    			{
			    				File file = searchedMjrFileList.get(searchedMjrFileListCountIndex);
			    				if(work_manager.CurrenSearchedFileNameList.get(deleteMjrFileNameListCountIndex).equals(file.getName()))
			    				{
			    					searched = true;
			    					break;
			    				}
			    			}
			    			
							if(false == searched)
							{
								work_manager.CurrenSearchedFileNameList.remove(deleteMjrFileNameListCountIndex);
								deleteMjrFileNameListCountIndex = 0;
							}
						}
	    				
					}
	    			
	    			if(null != searchedMjrFileList)
	    			{
	    				searchedMjrFileListCountIndex = 0;
	    				for(searchedMjrFileListCountIndex=0; searchedMjrFileListCountIndex<searchedMjrFileList.size();++searchedMjrFileListCountIndex)
	    				{
	    					File file = searchedMjrFileList.get(searchedMjrFileListCountIndex);
	    					String fileFullName = file.getName();
    					
	    					if(null == work_manager.CurrenSearchedFileNameList)
	    						work_manager.CurrenSearchedFileNameList = new ArrayList<String>();
    	    			
	    					if(-1 == work_manager.CurrenSearchedFileNameList.indexOf(fileFullName))
	    						work_manager.CurrenSearchedFileNameList.add(fileFullName);
	    				}
	    			}
	    			
	    		}
	    		work_manager.BuildMergeList(searchedMjrFileList);	
	  			
				//System.out.println("check_file_changed_status_thread");
				Thread.sleep(_sleepTickTime);
			}
			
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}