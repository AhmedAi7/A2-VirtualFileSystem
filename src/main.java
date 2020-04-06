import java.util.Scanner;

public class main {

	public main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		System.out.println("1-	Contiguous Allocation ");
		System.out.println("2-	Indexed Allocation ");
		Scanner input=new Scanner(System.in);
		int Choice= input.nextInt();
		if (Choice==1) {
			Contig contig=new Contig(40);
			contig.run();
		}
		else if (Choice==2) {
			Indexed idx=new Indexed(40);
			idx.run();
		}
		else {
			System.out.println("Wrong choice");
		}
	}

}
