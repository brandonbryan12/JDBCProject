package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.CustomerDAO;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class CustomerDaoImpl implements CustomerDAO
{

	@Override
	public Customer create(Connection connection, Customer customer) throws SQLException, DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer retrieve(Connection connection, Long id) throws SQLException, DAOException {
		
		
		return null;
	}

	@Override
	public int update(Connection connection, Customer customer) throws SQLException, DAOException {
		String updateSQL = "UPDATE customer SET firstName = ?, lastName = ?, gender = ?, dob = ?, email = ?, address = ?, creditCard = ? "
		        + "WHERE customer_id = ?;";
		
		if (customer.getId() == null) {
			throw new DAOException("Trying to update Customer with NULL ID");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(updateSQL);
			ps.setString(1, customer.getFirstName());
			ps.setString(2, customer.getLastName());
			ps.setString(3, String.valueOf(customer.getGender()));
			ps.setString(4, customer.getEmail());
			ps.setInt(5, customer.getActive());
			ps.setLong(6, customer.getStoreId());
			ps.setLong(7, customer.getAddressId());
			ps.setLong(8, customer.getCustomerId());

			int rows = ps.executeUpdate();
			return rows;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
		return 0;
	}

	@Override
	public int delete(Connection connection, Long id) throws SQLException, DAOException {
		String deleteSQL = "DELETE FROM CUSTOMER WHERE ID = ?;";
		
		if (id == null) {
			throw new DAOException("Trying to delete Customer with NULL ID");
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
	public List<Customer> retrieveByZipCode(Connection connection, String zipCode) throws SQLException, DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> retrieveByDOB(Connection connection, Date startDate, Date endDate)
			throws SQLException, DAOException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
