
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryMgmt {
	List<Integer> pageRequest= new ArrayList<Integer>();
	
	//Constructor: Reads the page request from the file and stores them in a list.
	public MemoryMgmt(String filePath) throws IOException{
		File  file= new File(filePath);
		String line = "";
		
		BufferedReader inputStream= new BufferedReader(new FileReader(file));
		while((line=inputStream.readLine())!=null){
			
			pageRequest.add(Integer.parseInt(line));
			
			
		}
		inputStream.close();
		int noOfPages=pageRequest.get(0);
		
	}
	/*Implementation of PFF: Page replacement algorithm.
	  Observation: As we increase the Threshold value, number of page fault decreases.*/
	public void pageReplacementPFF(int thresholdF){
		int minFrames = 1;
		int maxFrames = 0;
		int totalNoOfPageFault=0;
		int virtualTime=0;
		//Hash map to implement main memory.
		Map<Integer,Integer> memory = new HashMap<Integer,Integer>();
		for(int i=1;i<pageRequest.size();i++){
			int page= pageRequest.get(i);
			if(memory.containsKey(page)){
				memory.put(page, 1);
				virtualTime++;
			}
			//If page fault occurs that means the page not found in memory
			else
			{
				totalNoOfPageFault++;
				if(thresholdF<=virtualTime){
					resetUsedBits(memory);
					memory.put(page,1);
					virtualTime = 0;
				}
				else{
					memory.put(page,1);
					virtualTime = 1;
				}
			}
			minFrames= Math.min(minFrames, memory.size());
			maxFrames= Math.max(maxFrames, memory.size());
		}
		System.out.println("No of Pagefault  "+ totalNoOfPageFault);
		System.out.println("No of Min frames "+ minFrames);
		System.out.println("No of Max Frames "+ maxFrames + "\n");
	}
	/*Implementation of VSWS policy for page replacement.
	Here we vary 3 values i.e. max duration, min duration and number of page faults allowed
	Observation: 1)As noOfPageFaultsAllowed increases, totalNoOfPageFaults decrease.
	2)As difference between min duration and max duration decreases, totalNoOfPageFaults decrease.
	3)As value of min and max duration decreases, totalNoOfPageFaults increase.
	4)And also totalNoOfPageFaults vary with the max Frames value(which varies with the other 3 values)
	*/
	public void pageReplacementVSWS(int maxDuration,int minDuration,int noOfPageFaultsAllowed){
		int virtualTime=0;
		int noOfPageFaults=0;
		int totalNoOfPageFaults=0;
		int minFrames = 1;
		int maxFrames = 0;
		Map<Integer,Integer>mainMemory= new HashMap<Integer,Integer>();	
		            for(int i=1;i<pageRequest.size();i++){
		            	virtualTime++;
						int page= pageRequest.get(i);
						if(mainMemory.containsKey(page)){
							mainMemory.put(page, 1);
						} else {
							totalNoOfPageFaults++;
							noOfPageFaults++;
						}
							if(virtualTime>=maxDuration){
								resetUsedBits(mainMemory);
								mainMemory.put(page,1);
								virtualTime=0;
								noOfPageFaults=0;	
							}
							else
							{
								if(noOfPageFaults>=noOfPageFaultsAllowed){
									if(virtualTime<minDuration){
										mainMemory.put(page,1);
										
									}
									else{
										resetUsedBits(mainMemory);
										mainMemory.put(page, 1);
										virtualTime=0;
										noOfPageFaults=0;
									}
								}
								else
								{
									mainMemory.put(page,1);
								}	
						}
						minFrames= Math.min(minFrames, mainMemory.size());
						maxFrames= Math.max(maxFrames, mainMemory.size());
							
				}
				
		            System.out.println("No of page faults " + totalNoOfPageFaults);
		            System.out.println("No of Min frames "+ minFrames);
		    		System.out.println("No of Max Frames "+ maxFrames+"\n");
			}
			
						
	//This function resets used bits in the hash map. If the used bit is 0, then it removes that page.
	// If used bit is 1, it resets it to 0.				
	 public void resetUsedBits(Map<Integer,Integer> memory){
		 List<Integer> pagesToRemove = new ArrayList<Integer>();
			for(Integer j : memory.keySet()){
				if(memory.get(j) == 0){
					pagesToRemove.add(j);
				}
				else 
				{
					memory.put(j, 0);
				}
			}
			for(Integer j : pagesToRemove) {
				memory.remove(j);
			}
			
	}
	 
	public static void main(String[] args)  {
		try{
			//Give the input(file path)
			MemoryMgmt obj1= new MemoryMgmt(args[0]);
			MemoryMgmt obj2= new MemoryMgmt(args[0]);
			System.out.println("PFF Algorithm result: ");
			//vary the threshold value here.
			obj1.pageReplacementPFF(3);
			System.out.println("VSWS Algorithm result: ");
			//vary the max duration, min duration and no. of page faults allowed values here.
			obj2.pageReplacementVSWS(10,8,2);
			
		}catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
