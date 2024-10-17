package Models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Scanner;
import Database.DatabaseConnection;
import Database.User;
import Database.UserDAO;

public class UserPage {
	
	static User user;
	UserDAO dao=new UserDAO();
	private DatabaseConnection connectionManager;
	Scanner sc=new Scanner(System.in);
	
	public UserPage() {
		try {
			connectionManager=new DatabaseConnection();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void UserDetails() {
		System.out.println("Enter the Account Number to confirm:");
		int AccountNumber=sc.nextInt();
		try{
			String sql="Select transaction_date,transaction_sold_date,share_purchase_quantity,sold_share_quantity,share_price_sold,share_price_purchased from stocks_purchased where account_number=?";
			PreparedStatement stmt = connectionManager.getConnection().prepareStatement(sql);
			stmt.setInt(1, AccountNumber);
			ResultSet rs=stmt.executeQuery();
			while(rs.next()) {
                double sharePricePurchased = rs.getDouble("share_price_purchased");
                int sharePurchasedQuantity=rs.getInt("share_purchase_quantity");
                LocalDateTime transactionDate = rs.getObject("transaction_date", LocalDateTime.class);
                double sharePriceSold = rs.getDouble("share_price_sold");
                LocalDateTime transactionSoldDate = rs.getObject("transaction_sold_date", LocalDateTime.class);
                int soldShareQuantity = rs.getInt("sold_share_quantity");

                System.out.println("Share Purchase Quantity: " + sharePurchasedQuantity);
                System.out.println("Sold Share Quantity: " + soldShareQuantity);
                System.out.println("Share Price Purchased: " + sharePricePurchased);
                System.out.println("Transaction Date: " + transactionDate);
                System.out.println("Share Price Sold: " + sharePriceSold);
                System.out.println("Transaction Sold Date: " + transactionSoldDate);
                System.out.println();
			}
		}
		catch (Exception e) {
			e.getMessage();
		}
	}
	
	
	public void Deposit() {
		System.out.println("Enter the amount which you want to add:");
		double amount=sc.nextDouble();
		while(true) {
			System.out.println("Enter the Account Number to confirm:");
			int AccountNumber=sc.nextInt();
			try{
				String sql = "UPDATE users1 SET balance = balance + ? WHERE account_number = ?";
				PreparedStatement stmt = connectionManager.getConnection().prepareStatement(sql);
				stmt.setDouble(1, amount);
				stmt.setInt(2, AccountNumber);
	            int numRowsUpdated = stmt.executeUpdate();
				if (numRowsUpdated == 1) {
	                System.out.println("Balance updated successfully.\n");
	                break;
	            } else {
	                System.out.println("Error: Failed to update balance.\n");
	            }
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void Withdraw() {
			double amount=0.0;
			try {
				System.out.println("Enter the Account Number to confirm:");
				int accountNumber=Integer.parseInt(sc.nextLine());
				// Check the current balance in the account
	            String sql = "SELECT balance FROM users1 WHERE account_number = ?";
	            PreparedStatement stmt = connectionManager.getConnection().prepareStatement(sql);
	            stmt.setInt(1, accountNumber);
	            ResultSet rs = stmt.executeQuery();
	            rs.next();
	            double currentBalance = rs.getDouble("balance");
	
	            // Throw an error if the amount to withdraw is greater than the current balance
	            while(true) {
	            	System.out.println("Enter the amount which you want to Withdraw:");
	            	amount=sc.nextDouble();
		            if (amount> currentBalance) {
		                System.out.println("Error: Insufficient balance in account."+"  "+currentBalance+"\n");
		            }
		            else {
		            	break;
		            }
	            }
	
	            // Prepare a SQL statement to update the balance column for the given account number
	            sql = "UPDATE users1 SET balance = balance - ? WHERE account_number = ?";
	            stmt = connectionManager.getConnection().prepareStatement(sql);
	            stmt.setDouble(1, amount);
	            stmt.setInt(2, accountNumber);
	
	            // Execute the SQL statement to update the balance column
	            int numRowsUpdated = stmt.executeUpdate();
	            if (numRowsUpdated == 1) {
	                System.out.println("Balance updated successfully.\n");
	            } else {
	                System.out.println("Error: Failed to update balance.\n");
            }
	            
        } catch (Exception e) {
        	System.out.println("Invalid Value Entered");
        }
		
		
	}
	
	public void Buy() {
		Scanner sc=new Scanner(System.in);
		try{
			String sql = "SELECT * FROM stockmarket";
			PreparedStatement stmt = connectionManager.getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
            	String stockname=rs.getString("stock_name");
            	double stockprice=rs.getDouble("stock_price");
            	int stockquantity=rs.getInt("stock_quantity");
            	System.out.println("Stock Name:"+stockname);
            	System.out.println("Stock Price:"+stockprice);
            	System.out.println("Stock Quantity:"+stockquantity);
            	System.out.println("*-*-*-*-*-*-*-*-*-*-*");
            }
            System.out.println("Choose the stocks from the above list");
            System.out.println("Enter the Stock Name:");
            String stname=sc.nextLine();
            System.out.println("Enter the Quantity:");
            int quantity=Integer.parseInt(sc.nextLine());
            System.out.println("Enter Account Number for confirmation");
            int acn=Integer.parseInt(sc.nextLine());
            double price =dao.checkquantity(stname,quantity);
            double transactionCharge=Math.max(100, 0.05*price);
            double stt=0.01*price;
            double totaltransaction=transactionCharge+stt+price;
            double balance=dao.checkbalance(acn);
            if(balance>totaltransaction) {
            	dao.updateBalance(acn,balance-totaltransaction);
            	dao.updatepurchase(acn,stname,quantity,totaltransaction);
            	dao.stockupdate(stname,quantity);
            }
            else {
            	System.out.println("Kindly add Balance in your Wallet to make purchase");
            	Deposit();
            }
		}
		catch (Exception e) {
			
		}
	}
	public void Sell() {
		Scanner sc=new Scanner(System.in);
		try {
			System.out.println("Enter the stock name which you want to sell");
			String stname=sc.nextLine();
			System.out.println("Enter the quantity");
			int quantity=Integer.parseInt(sc.nextLine());
			System.out.println("Enter Account Number for confirmation");
			int acn=Integer.parseInt(sc.nextLine());
			if(dao.check(acn,quantity,stname)) {
				double price=dao.checkprice(stname);
				price=price*quantity;
				double transactionCharge=Math.max(100, 0.05*price);
	            double stt=0.01*price;
	            double totaltransaction=transactionCharge+stt+price;
	            double balance=dao.checkbalance(acn);
	            dao.updateBalance(acn, balance+totaltransaction);
	            dao.updatesell(acn,stname,quantity,totaltransaction);
	            dao.stocksell(stname,quantity);
			}
			else {
				System.out.println("Wrong Quantity Entered");
			}
		} catch (Exception e) {
			e.getMessage();
		}
		
	}
	
	public void Tdetails() {
		while(true) {
			System.out.println("\nSelect from below options");
			System.out.println("1. View Transaction by Date Range");
			System.out.println("2.View Transaction By Stock Name");
			System.out.println("0. Back To Previous Menu");
			String fromDate;
			String toDate;
			int acn;
			String stname;
			try {
			int input=Integer.parseInt(sc.nextLine());
			switch(input) {
							case 0: return;
							case 1: System.out.println("Enter From Date:");
									fromDate=sc.nextLine();
									System.out.println("Enter To Date");
									toDate=sc.nextLine();
									System.out.println("Enter Account Number for confirmation");
									acn=Integer.parseInt(sc.nextLine());
									dao.viewTransactionBetweenDateRange(fromDate, toDate, acn); 
							break;
							case 2:System.out.println("Enter Stock Name");
									stname=sc.nextLine();
									System.out.println("Enter Account Number for confirmation");
									acn=Integer.parseInt(sc.nextLine());
								dao.StockTransactionbyName(stname,acn);
							break;
							default:System.out.println("Enter Correct Value");
				}
			}
			catch (Exception e) {
				System.out.println("Enter valid input");
			}
		}
		
	}

}
