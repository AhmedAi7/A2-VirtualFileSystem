import java.util.ArrayList;

import com.sun.javafx.fxml.BeanAdapter;

public class FreeSpaceManager {
	int Size;
	int numOfFreeBlocks;
	char[] Blocks;
	public FreeSpaceManager(int size) {
		Size=size;
		numOfFreeBlocks=size;
		Blocks=new char [size];
		for (int i = 0; i < Blocks.length; i++) {
			Blocks[i]='0';
		}
	}

	public void PrintDiskStatus() {
	    System.out.println("Total Allocated space: "+(Size - numOfFreeBlocks));   
	    System.out.println("Total Available space: "+ numOfFreeBlocks);   
	    ArrayList<Integer>allocated =new ArrayList<Integer>();
	    ArrayList<Integer>free =new ArrayList<Integer>();
	    for (int i = 0; i < Blocks.length; i++) {
			if(Blocks[i]=='0')
				free.add(i);
			else {
				allocated.add(i);
			}
		}
	    System.out.print("Free Blocks:");
	    for (int i = 0; i < free.size(); i++) {
			System.out.print(free.get(i)+" ");
			}
	    System.out.println(" ");
	    System.out.print("Allocated Blocks:");
	    for (int i = 0; i < allocated.size(); i++) {
			System.out.print(allocated.get(i)+" ");
			}
	    }
	
	public int Indexed_allc(int size,ArrayList<Integer> blocks)
	{
		if (size + 1 > numOfFreeBlocks)
			return -1;
		else {
				int count = 0;
				int index = -1;
				for (int i = 0; i < Size; i++) {
				if (Blocks[i]=='0') {
					index=i;
					Blocks[i]='1';
					numOfFreeBlocks--;
					break;
				}
				}
				for (int i = 0; i < Blocks.length; i++) {
					if(Blocks[i]=='0')
					{
						Blocks[i]='1';
						blocks.add(i);
						count++;
						numOfFreeBlocks--;
						if(count==size)
							break;
					}
				}
		return index;		
		}	
		}
public void Indexed_deallc(ArrayList<Integer>blocks,int index) {
    System.out.println("Blocks Before: ");
    for (int i = 0; i < Blocks.length; i++) {
		System.out.print(Blocks[i]);
	}
    System.out.println(" ");
    Blocks[index]='0';
    numOfFreeBlocks++;
    for (int i = 0; i < blocks.size(); i++) {
		Blocks[blocks.get(i)]='0';
	    numOfFreeBlocks++;
	}
    System.out.println("Blocks After: ");
    for (int i = 0; i < Blocks.length; i++) {
		System.out.print(Blocks[i]);
	}
    System.out.println(" ");
}		
public int Contig_allc(int fsize,ArrayList<Integer> blocks) {
	//Best case.. Smallest enough space
	if (fsize >numOfFreeBlocks)
		return -1;
	else 
	{
		int SmallestS=Size;
		int SmallestIndex=-1;
		boolean check=false; //Start of block
		int start=-1;
		int count=0;
		for (int i = 0; i < Size; i++) {
			if(Blocks[i]=='0') {
				if(check==false)
				{
					start=i;
					check=true;
				}
				count++;
			}
			else {
				if(count>=fsize && count <=SmallestS) {
					SmallestIndex=start;
					SmallestS=count;
				}
				count=0;
				check=false;
			}
		}
		if (count>=fsize && count<=SmallestS) {
			SmallestIndex=start;
			SmallestS=count;
		}
		if (SmallestIndex==-1) {
			return -1;
		}
		else {
			System.out.println("Smallest index: "+SmallestIndex);
			for (int i = SmallestIndex; i < (SmallestIndex+fsize); i++) {
				Blocks[i]='1';
				numOfFreeBlocks--;
				blocks.add(i);
			}
		return SmallestIndex;
		}
	}
	}

	public void Contig_deallc(int start,int size) {
	        numOfFreeBlocks += size;
	        for (int i = 0; i < Blocks.length; i++) {
	    		System.out.print(Blocks[i]);
	    	}
	        System.out.println(" ");
	        for (int i = start; i < (start+size); i++) {
	    		Blocks[i]='0';
	    	    numOfFreeBlocks++;
	    	}
	        System.out.println("Blocks After: ");
	        for (int i = 0; i < Blocks.length; i++) {
	    		System.out.print(Blocks[i]);
	    	}
	}
}
