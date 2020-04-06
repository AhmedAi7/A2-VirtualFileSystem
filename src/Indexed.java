import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import javax.xml.bind.ParseConversionEvent;

import sun.security.util.Length;

public class Indexed {
Directory root;
FreeSpaceManager fsm;
	public Indexed(int size) {
		root=new Directory("/root", "root");
		fsm=new FreeSpaceManager(size);
	}
	public void deleteFolder(Directory d,FreeSpaceManager fsm) {
		for (int i = 0; i < d.files.size(); i++) {
			deleteFile(d.files.get(i), fsm);
		}
		d.files=null;
		for (int i = 0; i < d.subDirectories.size(); i++) {
			deleteFolder(d.subDirectories.get(i), fsm);
		}
		d.subDirectories=null;
	}
	public void deleteFile(PFile f,FreeSpaceManager fsm) {
		ArrayList<Integer>blocks=f.allocatedBlocks;
		int index=f.index;
		fsm.Indexed_deallc(blocks, index);
		System.out.println("File "+f.fname+" Is deleted");
	}
	public void createFile(String path,int size,FreeSpaceManager fsm,Directory root) {
        String folders[] = path.split("/");
        String name = folders[folders.length-1];
        PFile newFile = new PFile(path, name, size);
        Directory parent = root.SearchDir(folders[folders.length-2]);
        if(parent == null){
            	System.out.println("Path doesn't Exist");
            }
        else {
        	if (parent.SearchList(folders[folders.length-1])) {
				System.out.println("File is already exist");
			}
        	else {
        		ArrayList<Integer> blocks=new ArrayList<Integer>();
				int index=fsm.Indexed_allc(size, blocks);
				if (index==-1) {
					System.out.println("No Available Space");
					return;
				}
				else {
					newFile.allocatedBlocks=blocks;
					newFile.index=index;
					parent.files.add(newFile);
					System.out.println("File created Successfully..");
					System.out.print("Index: "+index + " Points To: ");
					for (int i = 0; i < blocks.size(); i++) {
						System.out.print(blocks.get(i)+"  ");
					}
					System.out.println(" ");
				}
			}
        }
	}
	public void RemoveFolder(String path,FreeSpaceManager fsm,Directory root) {
		String folders[]= path.split("/");
	    String name = folders[folders.length-1];
	    if (name.equals("root")) {
	    	System.out.println("Root can't be deleted");
	    	return;
	    	}
	    if (folders.length < 2) {
	    	System.out.println("Invalid path");
	    	return;
	    }
	    Directory d = root.SearchDir(folders[folders.length-1]);
	    if (d!=null) {
	    	deleteFolder(d, fsm);
	    	Directory parent = root.SearchDir(folders[folders.length-2]);
	    	for (int i = 0; i < parent.subDirectories.size(); i++) {
				if(parent.subDirectories.get(i).name.equals("folders[folders.length-1]"))
					parent.subDirectories.remove(i);
			}
	    	System.out.println("Folder "+folders[folders.length-1]+" Deleted Succesfully");
			}
	    else {
			System.out.println("Folder doesn't exist..");
		}
	}
	public void ExComm(String command,FreeSpaceManager fsm,Directory root) {
		String arr[]=command.split("-");
		if(arr[0].equals("CreateFile")) {
			if(arr.length<3)
				System.out.println("Not enough parameters..");
			else {
				createFile(arr[1],Integer.parseInt(arr[2]), fsm, root);
			}
		}
		else if(arr[0].equals("DisplayDiskStructure")) {
			root.printDirectoryStructure(0);
		}
		else if(arr[0].equals("DisplayDiskStatus")) {
			fsm.PrintDiskStatus();
		}
		else if(arr[0].equals("CreateFolder")) {
			root.createFolder(arr[1]);
		}
		else if(arr[0].equals("DeleteFolder")) {
			RemoveFolder(arr[1], fsm, root);
		}
		else if(arr[0].equals("DeleteFile")) {
			String folders[] = arr[1].split("/");
			String name = folders[folders.length-1];
			Directory parent = root.SearchDir(folders[folders.length-2]);
			if(parent!=null) {
				if(parent.SearchList(name)) {
					for (int i = 0; i < parent.files.size(); i++) {
						if(parent.files.get(i).fname.equals(name)) {
							deleteFile(parent.files.get(i),fsm);
							parent.files.remove(i);
							break;
						}
					}
				}
				else {
					System.out.println("File Not Found");
				}
			}
			else {
				System.out.println("Directory not found");
			}
		}
		else {
	        System.out.println("No such command");
		}
			
	}
	public void run() {
		System.out.println("Indexed Type....");
		try {
			File file =new File("Indexed.vfs");
		    if (file.createNewFile()) {
	          System.out.println("File created.. ");
	        } else {
	    		Store(file, root, fsm);
	    		System.out.println("File already exists.");
	        }
		Scanner inputScanner=new Scanner(System.in);
		while (true) {
			String comm=inputScanner.next();
			if(comm.equals("Exit"))
				break;
			else 
				ExComm(comm,fsm,root);
			System.out.println(" ");
		}
	Save(file, root, fsm);
	System.out.println("Saved..");
		}  catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
		}		
	public void Save(File file,Directory root,FreeSpaceManager fsm) {
		try {
			FileWriter Writer= new FileWriter(file,false);
			Writer.write("Indexed-"+fsm.Size+"-"+fsm.numOfFreeBlocks+"\n");
			for (int i = 0; i < root.subDirectories.size(); i++) {
				Writer.write("D-"+root.subDirectories.get(i).directoryPath+"\n");
			}
			Writer.close();
			for (int i = 0; i < root.files.size(); i++) {
				WriteFile(file, root.files.get(i));
			}
			for (int i = 0; i < root.subDirectories.size(); i++) {
				WriteFolder(file, root.subDirectories.get(i));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void WriteFile(File file,PFile F)
	{
		try {
			FileWriter Writer= new FileWriter(file,true);
			Writer.write("F-"+F.filePath+"-"+F.index+"-");
			for (int i = 0; i < F.allocatedBlocks.size(); i++) {
				Writer.write(F.allocatedBlocks.get(i)+",");
			}
			Writer.write("\n");
			Writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void WriteFolder(File file,Directory D)
	{
		try {
			FileWriter Writer= new FileWriter(file,true);
			for (int i = 0; i < D.files.size(); i++) {
				WriteFile(file, D.files.get(i));
			}
			for (int i = 0; i < D.subDirectories.size(); i++) {
				Writer.write("D-"+D.subDirectories.get(i).directoryPath+"\n");
			}
			for (int i = 0; i < D.subDirectories.size(); i++) {
				WriteFolder(file, D.subDirectories.get(i));
			}
			Writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void Store(File file,Directory root,FreeSpaceManager fsm) {
		Scanner Reader;
		try {
			Reader = new Scanner(file);
	      while (Reader.hasNextLine()) {
	        String Line = Reader.nextLine();
	        String arr[]=Line.split("-");
	        if(arr[0].equals("Indexed")) {// First line
	        	fsm.Size=Integer.parseInt(arr[1]);
	        	fsm.numOfFreeBlocks=Integer.parseInt(arr[2]);
	            if (arr.length>3) {
	        	for (int i = 0; i < arr[3].length(); i++) { 
	                fsm.Blocks[i] = arr[3].charAt(i); 
	            }
	        	}
	        }
	        else if (arr[0].equals("D")) {//Directory
	            String folders[] = arr[1].split("/");
	            String name = folders[folders.length-1];
	            String pname = folders[folders.length-2];
				Directory parent=root.SearchDir(pname);
				Directory d=new Directory(arr[1], name);
				parent.subDirectories.add(d);
			}
	        else if (arr[0].equals("F")) {//File
	            String folders[] = arr[1].split("/");
	            String name = folders[folders.length-1];
	            String pname = folders[folders.length-2];
				Directory parent=root.SearchDir(pname);
				int Findex=Integer.parseInt(arr[2]);
				String block[]=arr[3].split(",");
				ArrayList<Integer> Blocks=new ArrayList<Integer>();
				for (int i = 0; i < block.length; i++) {
					Blocks.add(Integer.parseInt(block[i]));
				}
				int size=Blocks.size();
				PFile f= new PFile(arr[1], name, size);
				f.allocatedBlocks=Blocks;
				f.index=Findex;
				parent.files.add(f);
			}
	      }
	      Reader.close();
	      }
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
