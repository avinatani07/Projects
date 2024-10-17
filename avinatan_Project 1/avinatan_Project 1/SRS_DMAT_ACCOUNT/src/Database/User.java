package Database;


import java.util.HashSet;
import java.util.Random;


  


public class User {
	private String name;
	private String pno;
	private String pano;
	private String email;
	private String password;
	private int acn;
	private double balance;
	
	private static final int ACCOUNT_NUMBER_LENGTH = 5; // Desired length of account number
	  private static final int MAX_ACCOUNT_NUMBER = (int) Math.pow(10, ACCOUNT_NUMBER_LENGTH); // Maximum value of account number
	  private static HashSet<String> usedAccountNumbers = new HashSet<String>(); // Set of used account numbers
	  private static Random rand = new Random(); // Random number generator

	  // Generate a unique 5-digit account number
	  public  int generate() {
	    String accountNumber;

	    // Keep generating a new account number until it's unique
	    do {
	      int randomNumber = rand.nextInt(MAX_ACCOUNT_NUMBER); // Generate a random number between 0 and 99999
	      accountNumber = String.format("%05d", randomNumber); // Format the number as a 5-digit string with leading zeros
	    } while (usedAccountNumbers.contains(accountNumber));

	    // Add the new account number to the set of used account numbers
	    usedAccountNumbers.add(accountNumber);

	    return Integer.parseInt(accountNumber);
	  }
	
	public User() {
		name = "";
		pno = "";
		pano = "";
		email = "";
		password = "";
		acn=00000;
		balance=0.0;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public String getpno() {
		return pno;
	}
	public String getpano() {
		return pano;
	}
	public void setpno(String pno) {
		this.pno=pno;
	}
	public void setpano(String pano) {
		this.pano=pano;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public void setAccountNumber(int acn) {
		this.acn=acn;
	}
	public int getAcccountNumber() {
		return acn;
	}
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	

	public User(String  name,String pno,String pano,String email,String password ) {
		this.name = name;
		this.pno=pno;
		this.pano=pano;
		this.email = email;
		this.password = password;
		this.acn=generate();
		}
	
	
}
