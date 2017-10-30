package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.PurchaseDAO;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.entity.Purchase;
import cs4347.jdbcProject.ecomm.services.PurchaseSummary;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class PurchaseDaoImpl implements PurchaseDAO
{

	@Override
	public Purchase create(Connection connection, Purchase purchase) throws SQLException, DAOException {
		/**
		 * The create method must throw a DAOException if the 
		 * given Purchase has a non-null ID. The create method must 
		 * return the same Purcahse with the ID attribute set to the
		 * value set by the application's auto-increment primary key column. 
		 * @throws DAOException if the given Purchase has a non-null id.
		 */
		String insertSQL = 
				"INSERT INTO PURCHASE (purchase_date, purchase_amt, PRODUCT_id, CUSTOMER_id) "
				+ "VALUES (?, ?, ?, ?);";
		
		// Requirement: Create operations require that the customer's ID is null
		// before being inserted into the table.
		if (purchase.getId() != null) 
		{
			throw new DAOException("Trying to insert Purchase with NON-NULL ID");
		}
		
				
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
			ps.setDate(1, purchase.getPurchaseDate());
			ps.setDouble(2, purchase.getPurchaseAmount());
			ps.setString(3, String.valueOf(purchase.getProductID()));
			ps.setString(4, String.valueOf(purchase.getCustomerID()));
			

			int res = ps.executeUpdate();
			if(res != 1) {
				throw new DAOException("Create Did Not Update Expected Number Of Rows");
			}

			// REQUIREMENT: Copy the generated auto-increment primary key to the
			// customer ID.
			ResultSet keyRS = ps.getGeneratedKeys();
			keyRS.next();
			int lastKey = keyRS.getInt(1);
			purchase.setId((long) lastKey);

			return purchase;
		}
		finally {
			if (ps != null && !ps.isClosed()) 
				ps.close();
		}
		
	}

	@Override
	public Purchase retrieve(Connection connection, Long id) throws SQLException, DAOException 
	{
		String selectQuery = "SELECT purchase_date, purchase_amt, PRODUCT_id, CUSTOMER_id "
		        + "FROM purchase where id = ?";
		
		if (id == null) {
			throw new DAOException("Trying to retrieve Purchase with NULL ID");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectQuery);
			System.out.println(id);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return null;
			}

			Purchase pur = new Purchase();
			pur.setId(id);
			pur.setPurchaseDate(rs.getDate("purchase_date"));
			//amt
			pur.setPurchaseAmount(rs.getDouble("purchase_amt"));
			//pid
			pur.setProductID(rs.getLong("PRODUCT_id"));
			//cid
			pur.setCustomerID(rs.getLong("CUSTOMER_id"));

			return pur;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}

		
	}

	@Override
	public int update(Connection connection, Purchase purchase) throws SQLException, DAOException {
		/**
		 * The update method must throw DAOException if the provided 
		 * Purchase has a NULL id. 
		 */
		String updateSQL = "UPDATE purchase SET purchase_date = ?, purchase_amt = ?, PRODUCT_id = ?, CUSTOMER_id = ? "
		        + "WHERE id = ?;";

		if (purchase.getId() == null) {
			throw new DAOException("Trying to update Purchase with NULL ID");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(updateSQL);
			ps.setDate(1, purchase.getPurchaseDate());
			ps.setDouble(2, purchase.getPurchaseAmount());
			ps.setLong(3, purchase.getProductID());
			ps.setLong(4, purchase.getCustomerID());
			ps.setLong(5, purchase.getId());

			int rows = ps.executeUpdate();
			return rows;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
		
	 
	}

	@Override
	public int delete(Connection connection, Long id) throws SQLException, DAOException {
String deleteSQL = "DELETE FROM Purchase WHERE id = ?;";
		
		if (id == null) {
			throw new DAOException("Trying to delete Purchase with NULL ID");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(deleteSQL);
			ps.setLong(1, id);

			int rows = ps.executeUpdate();
			return rows;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public List<Purchase> retrieveForCustomerID(Connection connection, Long customerID)
			throws SQLException, DAOException {
		String selectQuery = "SELECT p.id, purchase_date, purchase_amt, PRODUCT_id "
		        + "FROM customer as c JOIN purchase as p on c.id = p.CUSTOMER_id "
				+ "where p.CUSTOMER_id = ?";
		
		List<Purchase> purs = new ArrayList<>();
		
		if (customerID == null) {
			throw new DAOException("Trying to retrieve Purchase with NULL customerID");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectQuery);
			ps.setLong(1, customerID);
			ResultSet rs = ps.executeQuery();
			
			if (!rs.next()) {
				return purs;
			}
			
			do {
				Purchase pur = new Purchase();
				pur.setId(rs.getLong("id"));
				pur.setPurchaseDate(rs.getDate("purchase_date"));
				//amt
				pur.setPurchaseAmount(rs.getDouble("purchase_amt"));
				//pid
				pur.setProductID(rs.getLong("PRODUCT_id"));
				//cid
				pur.setCustomerID(customerID);
				
				
				purs.add(pur);
			} while(rs.next());

			return purs;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public List<Purchase> retrieveForProductID(Connection connection, Long productID)
			throws SQLException, DAOException {
		String selectQuery = "SELECT p.id, purchase_date, purchase_amt, PRODUCT_id, CUSTOMER_id "
		        + "FROM product as pr JOIN purchase as p on pr.id = p.PRODUCT_id "
				+ "where pr.id = ?";
		
		List<Purchase> purs = new ArrayList<>();
		
		if (productID == null) {
			throw new DAOException("Trying to retrieve Purchase with NULL zipCode");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectQuery);
			ps.setLong(1, productID);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return purs;
			}
			
			do {
				Purchase pur = new Purchase();
				pur.setId(rs.getLong("id"));
				pur.setPurchaseDate(rs.getDate("purchase_date"));
				//amt
				pur.setPurchaseAmount(rs.getDouble("purchase_amt"));
				//pid
				pur.setProductID(rs.getLong("PRODUCT_id"));
				//cid
				pur.setCustomerID(rs.getLong("CUSTOMER_id"));
				
				
				purs.add(pur);
			} while(rs.next());

			return purs;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
		
	}

	@Override
	public PurchaseSummary retrievePurchaseSummary(Connection connection, Long customerID)
			throws SQLException, DAOException {

		String selectQuery = "SELECT id, purchase_amt "
		        + "FROM purchase where CUSTOMER_id = ?";
		PurchaseSummary summary = new PurchaseSummary();
		
		//get all the purchases for custID, and get statistics for it. Return the object. 
		if (customerID == null) {
			throw new DAOException("Trying to retrieve Purchase with NULL customerID");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectQuery);
			ps.setLong(1, customerID);
			ResultSet rs = ps.executeQuery();
			
			if (!rs.next()) {
				return null;
			}
			
			float min = Float.MAX_VALUE;
			float max = Float.MIN_VALUE;
			int count = 0;
			double sum = 0;
			
			do
			{
			
				count++;
				sum += rs.getDouble("purchase_amt");
				if(rs.getDouble("purchase_amt")<min)
				{
					min = (float) rs.getDouble("purchase_amt");
				}
				else if(rs.getDouble("purchase_amt")>max)
				{
					max = (float) rs.getDouble("purchase_amt");
				}
			}while(rs.next());
			
			summary.minPurchase=min;
			summary.maxPurchase=max;
			summary.avgPurchase=(float) sum/count;

			
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
		
						
		return summary;
	}
	
}
