import java.util.ArrayList;

public class PFile {
	public String fname;
	public String filePath;
	public int size;
	public ArrayList<Integer> allocatedBlocks;
	public int index;
	public PFile(String path,String name,int s) {
		fname = name;
		filePath=path;
		size=s;
		allocatedBlocks=new ArrayList<Integer>();
	}

}
