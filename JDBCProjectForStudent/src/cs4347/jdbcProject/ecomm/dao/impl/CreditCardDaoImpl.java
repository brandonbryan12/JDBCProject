package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cs4347.jdbcProject.ecomm.dao.CreditCardDAO;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.util.DAOException;


public class CreditCardDaoImpl implements CreditCardDAO
{
	private static final String insertSQL = "INSERT INTO creditcard(name, cc_number, exp_date, security_code, CUSTOMER_id) VALUES(?, ?, ?, ?, ?);";
	@Override
	public CreditCard create(Connection connection, CreditCard creditCard, Long customerID)
			throws SQLException, DAOException {
		 //Throw exception here
		if(creditCard.getCustId()!=null){
			throw new DAOException ("Trying to insert credit_card with NON-NULL ID");
		}

		PreparedStatement ps = null;
		try{
			ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, creditCard.getName());
			ps.setString(2, creditCard.getCcNumber());
			ps.setString(3, creditCard.getExpDate());
			ps.setString(4, creditCard.getSecurityCode());
			ps.setLong(5, customerID );
			ps.executeUpdate();
			
			return creditCard;
		}
		finally{
			if (ps !=null && !ps.isClosed())
				ps.close();
		}
	}

	private static final String selectSQL = "SELECT CUSTOMER_id, name, cc_number, exp_date, security_code FROM creditcard where CUSTOMER_id = ?";
	@Override
	public CreditCard retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		if(customerID==null)
			throw new DAOException("Trying to retrieve Customer with NULL ID");
		PreparedStatement ps = null;
		try{
			ps = connection.prepareStatement(selectSQL);
			ps.setLong(1, customerID);
			ResultSet rs = ps.executeQuery();
			if(!rs.next())
				return null;
			
			CreditCard c = new CreditCard();
			c.setCustId(rs.getLong("CUSTOMER_id"));
			c.setName(rs.getString("name"));
			c.setCcNumber(rs.getString("cc_number"));
			c.setExpDate(rs.getString("exp_date"));
			c.setSecurityCode(rs.getString("security_code"));
			return c;
		}
		
		finally {
			if (ps != null && !ps.isClosed()){
				ps.close();
			}
		}
	}
	final static String deleteSQL = "DELETE FROM creditcard WHERE CUSTOMER_id = ?;";
	@Override
	public void deleteForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		if (customerID ==null )
			throw new DAOException ("Trying to delete Credit_Card with NULL ID");
		PreparedStatement ps = null;
		try{
			ps = connection.prepareStatement(deleteSQL);
			ps.setLong(1, customerID);
			
			ps.executeUpdate();
		}
		finally{
			if( ps != null && !ps.isClosed())
				ps.close();
		}
	}
	final static String updateSQL = "UPDATE creditcard SET name = ?, cc_number = ?, exp_date = ?, security_code = ? WHERE CUSTOMER_id = ?;" ;
	@Override
	public int dupdateForCustomerID(Connection connection, CreditCard creditcard) throws SQLException, DAOException {
		if (creditcard.getCustId() == null) {
			throw new DAOException("Trying to update Address with NULL ID");
		}
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(updateSQL);
			ps.setString(1, creditcard.getName());
			ps.setString(2, creditcard.getCcNumber());
			ps.setString(3, creditcard.getExpDate());
			ps.setString(4, creditcard.getSecurityCode());
			
			int rows =ps.executeUpdate();
			return rows;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}
}
