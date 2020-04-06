import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.sun.glass.ui.Window.Level;

public class Directory {
	public String name;
	public String directoryPath;
	public ArrayList<PFile> files;
	public ArrayList<Directory> subDirectories;

	public Directory(String dpath,String dname) {
		name=dname;
		directoryPath=dpath;
		files=new ArrayList<PFile>();
		subDirectories = new ArrayList<Directory>();
	}
	public boolean SearchList(String sname) {
		for (int i = 0; i < subDirectories.size(); i++) {
			if(subDirectories.get(i).name.equals(sname))
				return true;
			}
		for (int i = 0; i < files.size(); i++) {
			if(files.get(i).fname.equals(sname))
				return true;
			}
		return false;
	}
	
	public void createFolder(String path) 
	{
    String folders[] = path.split("/");
    String name = folders[folders.length-1];
    Directory newDir = new Directory(path, name);
    Directory parent=this.SearchDir(folders[folders.length-2]); 
    if (parent != null) {
        if (parent.SearchList(name)==false)
            {
        	parent.subDirectories.add(newDir);
            System.out.println("Directory "+name+" is added Successfully");
            }
        else
        	System.out.println("Directory "+name+" is already Exist");
    }
    else
        System.out.println("Path doesn't exit"); 
    }
	
	public Directory SearchDir(String Name) {
		if (this.name.equals(Name))
			return this;
		else {
		for (int i = 0; i < this.subDirectories.size(); i++) {
			Directory temp= subDirectories.get(i).SearchDir(Name);
			if(temp!=null)
				return temp;
		}		
		}
		return null;
	}
	public void printDirectoryStructure(int Level) {
		Directory d=this;
		for (int i = 0; i < Level*6; i++) {
			System.out.print(" ");}
		System.out.println("<"+d.name+">");
		for (int i = 0; i < d.files.size(); i++) {
			for (int j = 0; j < Level*6; j++) {
				System.out.print(" ");}
			System.out.println("   *"+d.files.get(i).fname);
		}
		for (int i = 0; i < d.subDirectories.size(); i++) {
			d.subDirectories.get(i).printDirectoryStructure(Level+1);
		}
	}
}
