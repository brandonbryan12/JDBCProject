package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cs4347.jdbcProject.ecomm.dao.AddressDAO;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class AddressDaoImpl implements AddressDAO
{
	@Override
	public int updateforCustomerID(Connection connection, Address address) throws SQLException, DAOException {
		String updateSQL = "UPDATE address SET address1 = ?, address2 = ?, city = ?, state = ?, zipcode = ? "
		        + "WHERE customer_id = ?;";
		if (address.getCustId() == null) {
			throw new DAOException("Trying to update Address with NULL ID");
		}
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(updateSQL);
			ps.setString(1, address.getAddress1());
			ps.setString(2, address.getAddress2());
			ps.setString(3, address.getCity());
			ps.setString(4, address.getState());
			ps.setString(5, address.getZipcode());
			ps.setLong(6, address.getCustId());
			
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
	public Address create(Connection connection, Address address, Long customerID) throws SQLException, DAOException {
		String insertSQL = 
				"INSERT INTO ADDRESS (customer_id, address1, address2, city, state, zipcode) "
				+ "VALUES (?, ?, ?, ?, ?, ?);";
		
		// throw exception if address.getId() is not null
		if (address.getCustId() != null) {
			throw new DAOException("Trying to insert Address with NON-NULL ID");
		}
		
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, customerID);
			ps.setString(2, address.getAddress1());
			ps.setString(3, address.getAddress2());
			ps.setString(4, address.getCity());
			ps.setString(5, address.getState());
			ps.setString(6, address.getZipcode());

			int res = ps.executeUpdate();
			if(res != 1) {
				throw new DAOException("Create Did Not Update Expected Number Of Rows");
			}
			
			address.setCustId(customerID);
			
			return address;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public Address retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		String selectQuery = "SELECT customer_id, address1, address2, city, state, zipcode "
		        + "FROM address where customer_id = ?";
		
		if (customerID == null) {
			throw new DAOException("Trying to retrieve Address with NULL ID");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectQuery);
			ps.setLong(1, customerID);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return null;
			}

			Address addr = new Address();
			addr.setCustId(rs.getLong("customer_id"));
			addr.setAddress1(rs.getString("address1"));
			addr.setAddress2(rs.getString("address2"));
			addr.setCity(rs.getString("city"));
			addr.setState(rs.getString("state"));
			addr.setZipcode(rs.getString("zipcode"));

			return addr;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public void deleteForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		String deleteSQL = "DELETE FROM ADDRESS WHERE CUSTOMER_ID = ?;";
		
		if (customerID == null) {
			throw new DAOException("Trying to delete Address with NULL ID");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(deleteSQL);
			ps.setLong(1, customerID);

			int rows = ps.executeUpdate();
			//return rows;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}
}
