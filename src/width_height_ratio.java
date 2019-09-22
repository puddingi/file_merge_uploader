
public class width_height_ratio 
{
	public int Xposition;
	public int Yposition;
	public int Width;
	public int Height;
	
	public void Calculate(int destinationWidth,int destinationHeight,int sourceWidth,int sourceHeight)
	{
		
			int war = 0;
		    int har = 0;
			int gcd = 0;
			int temp = 0;
			int max = 0;
			int min = 0;
			int rwidth = sourceWidth; 
			int rheight = sourceHeight;      
			
			if(rwidth < rheight)
			{    
				max = rwidth;           
				min = rheight; 
			}
			else
			{ 
				max = rheight;
				min = rwidth; 
			}
			
			while(max % min != 0)
			{   
				temp = max % min;    
				max = min;        
				min = temp;      
				gcd = min;      
				
				 war = rwidth/gcd;      
				 har = rheight/gcd;     
			}
			
			if(war > har)
			{
				Width = destinationWidth;
				Height =  destinationWidth * har / war;
				Yposition = (destinationHeight - Height) / 2;
				
			}
			else if(war < har)
			{
				Height = destinationHeight;
				Width = destinationHeight * war / har;
				Xposition = (destinationWidth - Width) / 2;
			}
		
	}
}
