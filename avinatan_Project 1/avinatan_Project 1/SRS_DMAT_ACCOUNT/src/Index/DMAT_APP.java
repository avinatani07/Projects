package Index;

import java.util.Scanner;


public class DMAT_APP {
	public static void main(String args[]) {
			UserData c=new UserData();
			System.out.println("Welcome to SRS DMAT Account");
			System.out.println("1- Create DMAT Account");
			System.out.println("2- Login Account");
			System.out.println("3- Exit Application");
			Scanner sc=new Scanner(System.in);
			while(true) {
					try {
						int input=Integer.parseInt(sc.nextLine());
						if(input==1) {
							c.CreatePage();
						}
						else if(input==2) {
							c.Login();
						}
						else if(input==3) {
							System.exit(0);
						}
					}catch (Exception e) {
						System.out.println("\nEnter Correct value");
						e.getMessage();
					}
			}
	}

}
