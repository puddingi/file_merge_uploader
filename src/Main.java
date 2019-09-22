import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.time.Instant;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;


//sudo rm -rf ~/file_merge_uploader/FileMergeUploader.jar
//chmod 705 ~/file_merge_uploader/FileMergeUploader.jar

//old : ssh -i Downloads/webrtc-test.pem ubuntu@ec2-3-89-66-237.compute-1.amazonaws.com
//new : ssh -i Downloads/webrtc-test.pem ubuntu@ec2-18-215-148-178.compute-1.amazonaws.com
 

//process list
//ps -efc


// ssh -i Downloads/media-server.pem ubuntu@3.84.189.62.compute-1.amazonaws.com
// ssh -i Downloads/webrtc-test.pem ubuntu@ec2-18-215-148-178.compute-1.amazonaws.com


public class Main extends JFrame
{	
	private JPanel _logPanel;
	private JList<String> _logList;
	private DefaultListModel<String> _loglistModel;
	
	void _Log(String logInfo)
	{
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		
        long year = calendar.get(Calendar.YEAR);
        long month = calendar.get(Calendar.MONTH)+1;
        long day = calendar.get(Calendar.DAY_OF_MONTH);
        long hour = calendar.get(Calendar.HOUR_OF_DAY);
        long minute = calendar.get(Calendar.MINUTE);
        long second = calendar.get(Calendar.SECOND);
		
        long yearMonthDay = year * 10000 + month * 100 + day;
        long hourMinuteSecond = hour * 10000 + minute * 100 + second;
        
        long nowDateTimeExceptTick = date.getTime() / 1000;
		
        String entireLogInfo = "["+yearMonthDay+"_"+hourMinuteSecond+"] - "+logInfo;
        
		_loglistModel.addElement(entireLogInfo);
		System.out.println(entireLogInfo);
	}
	
	
    public Main() 
    {
    	_loglistModel = new DefaultListModel<String>();
    	_logList = new JList<String>(_loglistModel);
        
        _logPanel = new JPanel(false);
        _logPanel.add(_logList);
         add(_logPanel);
    	
        setBounds(100, 100, 450, 300);
        setTitle("FileMergeUploader");
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() 
        {
            public void windowClosed(WindowEvent e) 
            {
                System.out.println("window closed");
                _Finish();
            }

            public void windowClosing(WindowEvent e) 
            {
                System.out.println("window closing");
                _Finish();
            }
        });   
    }
    
    void _Finish()
    {
    	
    	
        System.out.println("Work Finished");	
    }
   
    public void Start()
    {
    	System.out.println("Work Started");	
    	String currentPath = utility.CurrentExecutablePath();
		System.out.println(currentPath);
	}
     
	public static void main(String[] args)
	{
		utility.CreateDirectory(utility.CurrentExecutablePath()+"/log");
		preset.Read();	
		
		log_handler.WriteConsole("FileMergeUploader Started");
		try 
		{	
			if(null != args)
			{
				for(int countIndex=0; countIndex<args.length; ++countIndex)
				{
					log_handler.WriteConsole("main args["+countIndex+"/"+args.length+"] : "+args[countIndex]);
					
					switch(args[countIndex])
					{
					case "--argument":
					{
						main_argument.Parse(args[countIndex+1]);
					}
					break;
					}
				}
			}
			
			
			switch(preset.ServiceType)
			{
				case preset.SERVICE_TYPE_MERGE_TOPDOWN:
				{
					//in editor
					if(0 == args.length)
					{
						main_argument.TestSample();
					}
				}
				break;
			}
			
			
			
			/*
			socket_server_handler socketServerHandler = new socket_server_handler();
			new Thread(socketServerHandler).start();
			
			check_file_changed_status_thread checkFileChangedStatusThread = null;
			convert_thread convertThread = null;
			mux_merge_thread muxMergeThread = null;
			
			switch(work_manager.ServiceType)
			{
				case work_manager.SERVICE_TYPE_CONVERT:
				case work_manager.SERVICE_TYPE_CONVERT2:
				{
					checkFileChangedStatusThread = new check_file_changed_status_thread();
					convertThread = new convert_thread();
					
				}
				break;
			
				case work_manager.SERVICE_TYPE_CONVERT_MUX:
				case work_manager.SERVICE_TYPE_CONVERT_MERGE:
				{
					checkFileChangedStatusThread = new check_file_changed_status_thread();
					convertThread = new convert_thread();
					muxMergeThread = new mux_merge_thread();
				}
				break;
				
				case work_manager.SERVICE_TYPE_MUX:
				case work_manager.SERVICE_TYPE_MERGE_TOPDOWN:	
				{
					muxMergeThread = new mux_merge_thread();
				}
				break;
			}
			
			if(null != checkFileChangedStatusThread)
				new Thread(checkFileChangedStatusThread).start();
			if(null != convertThread)
				new Thread(convertThread).start();
			if(null != muxMergeThread)
				new Thread(muxMergeThread).start();
			*/
			
		} 
		catch (Exception e)
		{
            e.printStackTrace();
        }
		
		
	}
}
