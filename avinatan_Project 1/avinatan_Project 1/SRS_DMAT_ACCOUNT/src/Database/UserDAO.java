package Database;



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.sql.*;


public class UserDAO{
	static User user;
    private DatabaseConnection connectionManager;
	
	public UserDAO() {
        try {
        	connectionManager = new DatabaseConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public User createUser(User user) {
        try  {
            PreparedStatement statement = connectionManager.getConnection().prepareStatement("INSERT INTO users1(account_number,name,pan_number,phone_number,email,password) VALUES (?, ?, ?, ?, ?,?)");
            
            statement.setInt(1, user.getAcccountNumber());
            statement.setString(2, user.getName());
            statement.setString(3, user.getpano());
            statement.setString(4, user.getpno());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPassword());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
    }
        catch (Exception e) {
        	e.getMessage();
		}
        return user;
}
	
	public boolean getUserByACN(int AccountNumber, String password) {
	    boolean isAuthenticated = false;
		try{
				String sql ="Select * from users1 where account_number=?";
				PreparedStatement statement = connectionManager.getConnection().prepareStatement(sql);
				statement.setInt(1,AccountNumber);
				ResultSet rs = statement.executeQuery();
	            if (rs.next()) {
	               String pass=rs.getString("password");
	               if(password.equals(pass)) {
	            	   isAuthenticated=true;
	               }
	            }
	}
	catch(SQLException e){
		e.printStackTrace();
	}
		return isAuthenticated;
		
	}
	
	public double checkquantity(String stname, int qty) {
		try{
			double stockprice;
			int stockquantity;
			String sql = "SELECT * FROM stockmarket where stock_name=?";
			PreparedStatement stmt = connectionManager.getConnection().prepareStatement(sql);
			stmt.setString(1,stname);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
            	stockprice=rs.getDouble("stock_price");
            	stockquantity=rs.getInt("stock_quantity");
            	if(stockquantity>=qty) {
            		return stockprice*qty;
            	}
            	else {
            		return 0.0;
            	}
			}
			
		}
		catch (Exception e) {
			e.getStackTrace();
		}
		return 0.0;
	}
	
	public double checkbalance(int acn) {
			double balance=0.0;
			try{
				String sql = "SELECT * FROM users1 where account_number=?";
				PreparedStatement stmt = connectionManager.getConnection().prepareStatement(sql);
				stmt.setInt(1, acn);
				ResultSet rs = stmt.executeQuery();
				if(rs.next()) {
					balance=rs.getDouble("balance");
					return balance;
				}
			}
			catch (Exception e) {
				e.getStackTrace();
			}
			return balance;
	}
	
	public void updateBalance(int acn,double updatebalance) {
		try{
			String sql="Update users1 SET balance=? where account_number=?";
			PreparedStatement stmt=connectionManager.getConnection().prepareStatement(sql);
			stmt.setDouble(1, updatebalance);
			stmt.setInt(2, acn);
			int affectedRows = stmt.executeUpdate();
	            
			if (affectedRows == 0) {
				throw new SQLException("Creating user failed, no rows affected.");
	            }
		}
		catch (Exception e) {
			e.getMessage();
		}
		
	}
	public void updatepurchase(int acn,String stname,int quantity,double totaltransaction) {
		try {
			String sql="INSERT INTO stocks_purchased (account_number, share_name, share_quantity, share_price_purchased, transaction_date,share_purchase_quantity) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt=connectionManager.getConnection().prepareStatement(sql);
			stmt.setInt(1, acn);
			stmt.setString(2, stname);
			stmt.setInt(3, quantity);
			stmt.setDouble(4,totaltransaction);
			stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			stmt.setInt(6, quantity);

			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating user failed, no rows affected.");
	            }
			else {
				System.out.println("Stocks Purchased Successfylly!");
			}
			
		}
		catch (Exception e) {
			e.getMessage();
		}
		
	}
	public void stockupdate(String stname,int quantity) {
		try{
			String sql="UPDATE stockmarket SET stock_quantity = stock_quantity - ? WHERE stock_name = ?";
			PreparedStatement stmt=connectionManager.getConnection().prepareStatement(sql);
			stmt.setInt(1, quantity);
			stmt.setString(2, stname);
			stmt.executeUpdate();
		}
		catch (Exception e) {
			e.getStackTrace();
		}
	}
	public boolean check(int acn,int quantity,String stname) {
		try{
			String sql="Select * from stocks_purchased where account_number=? and share_name=?";
			PreparedStatement stmt=connectionManager.getConnection().prepareStatement(sql);
			stmt.setInt(1, acn);
			stmt.setString(2, stname);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				int check=rs.getInt("share_quantity");
				if(quantity<=check) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		catch (Exception e) {
			e.getMessage();
		}
		return false;
	}
	public double checkprice(String stname) {
		try {
			String sql="Select * from stockmarket where stock_name=?";
			PreparedStatement stmt=connectionManager.getConnection().prepareStatement(sql);
			stmt.setString(1, stname);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				double price=rs.getDouble("stock_price");
			}
		}
		catch (Exception e) {
			e.getMessage();
		}
		return 0;
	}
	public void updatesell(int acn,String stname,int quantity,double totaltransaction) {
		try {
			String sql="UPDATE stocks_purchased SET share_price_sold = ?, share_quantity = share_quantity - ?, transaction_sold_date = ?, sold_share_quantity = ? WHERE account_number = ? AND share_name = ?";
			PreparedStatement stmt=connectionManager.getConnection().prepareStatement(sql);
			stmt.setDouble(1, totaltransaction);
			stmt.setInt(2, quantity);
		//	stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			stmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
			stmt.setInt(4, quantity);
			stmt.setInt(5, acn);
			stmt.setString(6, stname);
			stmt.executeUpdate();
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating user failed, no rows affected.");
	            }
			else {
				System.out.println("Stocks Sold Successfully!");
			}
		}
		catch (Exception e) {
			e.getMessage();
		}
		
		
	}
	public void stocksell(String stname, int quantity) {
		try {
			String sql="Update stockmarket SET stock_quantity=? where stock_name=?";
			PreparedStatement stmt=connectionManager.getConnection().prepareStatement(sql);
			stmt.setInt(1, quantity);
			stmt.setString(2, stname);
			stmt.executeUpdate();
		}
		catch (Exception e) {
		
		}
		
	}
	public void viewTransactionBetweenDateRange(String fromDate, String toDate, int acn) {

        try {
            String sql = "SELECT * FROM stocks_purchased WHERE account_number=? AND ((transaction_date BETWEEN  ? AND ?) OR (transaction_sold_date BETWEEN ? AND ?))";
            PreparedStatement stmt = connectionManager.getConnection().prepareStatement(sql);
            stmt.setInt(1, acn);
            stmt.setString(2, fromDate+" 00:00:00");
            stmt.setString(3, toDate+" 23:59:59");
            stmt.setString(4, fromDate+" 00:00:00");
            stmt.setString(5, toDate+" 23:59:59");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String shareName = rs.getString("share_name");
                int shareQuantity = rs.getInt("share_quantity");
                double sharePricePurchased = rs.getDouble("share_price_purchased");
                LocalDateTime transactionDate = rs.getObject("transaction_date", LocalDateTime.class);
                double sharePriceSold = rs.getDouble("share_price_sold");
                LocalDateTime transactionSoldDate = rs.getObject("transaction_sold_date", LocalDateTime.class);
                int soldShareQuantity = rs.getInt("sold_share_quantity");

                System.out.println("Share Name: " + shareName);
                System.out.println("Share Quantity: " + shareQuantity);
                System.out.println("Share Price Purchased: " + sharePricePurchased);
                System.out.println("Transaction Date: " + transactionDate);
                System.out.println("Share Price Sold: " + sharePriceSold);
                System.out.println("Transaction Sold Date: " + transactionSoldDate);
                System.out.println("Sold Share Quantity: " + soldShareQuantity);
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}
	public void StockTransactionbyName(String stname, int acn) {
		try {
			String sql="Select * from stocks_purchased where share_name=? and account_number=?";
            PreparedStatement stmt = connectionManager.getConnection().prepareStatement(sql);
            stmt.setString(1, stname);
            stmt.setInt(2, acn);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String shareName = rs.getString("share_name");
                int shareQuantity = rs.getInt("share_quantity");
                double sharePricePurchased = rs.getDouble("share_price_purchased");
                int sharePurchasedQuantity=rs.getInt("share_purchase_quantity");
                LocalDateTime transactionDate = rs.getObject("transaction_date", LocalDateTime.class);
                double sharePriceSold = rs.getDouble("share_price_sold");
                LocalDateTime transactionSoldDate = rs.getObject("transaction_sold_date", LocalDateTime.class);
                int soldShareQuantity = rs.getInt("sold_share_quantity");

                System.out.println("Share Name: " + shareName);
                System.out.println("Share Quantity: " + shareQuantity);
                System.out.println("Share Purchase Quantity: " + sharePurchasedQuantity);
                System.out.println("Share Price Purchased: " + sharePricePurchased);
                System.out.println("Transaction Date: " + transactionDate);
                System.out.println("Share Price Sold: " + sharePriceSold);
                System.out.println("Transaction Sold Date: " + transactionSoldDate);
                System.out.println("Sold Share Quantity: " + soldShareQuantity);
                System.out.println();
            }
		} catch (Exception e) {
			e.getMessage();
		}
		
	}
	
}
