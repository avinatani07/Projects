package Index;

import java.util.Scanner;

import Database.User;
import Database.UserDAO;
import Models.UserPage;

public class UserData extends UserPage {
		
		Scanner sc=new Scanner(System.in);
		UserDAO dao=new UserDAO();
		User user = new User();
		//Create DMAT account 
		
		void CreatePage() {
		System.out.println("Enter Your Name:");
		user.setName(sc.nextLine());
		System.out.println("Enter Phone Number");
		user.setpno(sc.nextLine());
		System.out.println("Enter Your PAN Card Number");
		user.setpano(sc.nextLine());
		System.out.println("Enter Your Email Address");
		user.setEmail(sc.nextLine());
		System.out.println("Enter Your Password");
		user.setPassword(sc.nextLine());
		user.setAccountNumber(user.generate());
		System.out.println("User Has been Created Successfully!");
		System.out.println("This is your Account Number"+"  "+user.getAcccountNumber());
		dao.createUser(user);
		LoggedIn();
		}
		
		//Login DMAT account
		void Login() {
			int i=0;
			while(i<3) 
			{	
					System.out.println("\nEnter Account Number");
					int AccountNumber=Integer.parseInt(sc.nextLine());
					System.out.println("Enter Password");
					String password=sc.nextLine();
						if(dao.getUserByACN(AccountNumber, password)) {
				 								LoggedIn();
												}
						else {
							System.out.println("Enter Correct Account Number only"+""+(2-i)+"attempts left");
							i++;
							}
			}
		}
		
		//Logged in DMAT account
		void LoggedIn() {
			while(true) {
				Scanner sc=new Scanner(System.in);
				System.out.println("0- Exit Application");
				System.out.println("1- Display DMAT Account Details");
				System.out.println("2- Deposit Money");
				System.out.println("3- Withdraw Money");
				System.out.println("4- Buy Transaction");
				System.out.println("5- Sell Transaction");
				System.out.println("6- View Transaction Details");
				int input=sc.nextInt();
				switch (input) {
				case 0:
						System.exit(0);
					break;
				case 1:
						UserDetails();
					break;
				case 2:
						Deposit();
					break;
				case 3:
						Withdraw();
					break;
				case 4:
						Buy();
					break;
				case 5:
						Sell();
					break;
				case 6:
						Tdetails();
					break;
					
				default:
					System.out.println("Wrong input Entered");
				}
			}
		}
		
}
