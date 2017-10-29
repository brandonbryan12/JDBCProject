package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.CustomerDAO;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class CustomerDaoImpl implements CustomerDAO
{

	@Override
	public Customer create(Connection connection, Customer customer) throws SQLException, DAOException {
		String insertSQL = 
				"INSERT INTO CUSTOMER (first_name, last_name, gender, dob, email) "
				+ "VALUES (?, ?, ?, ?, ?);";
		
		// Requirement: Create operations require that the customer's ID is null
		// before being inserted into the table.
		if (customer.getId() != null) {
			throw new DAOException("Trying to insert Customer with NON-NULL ID");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, customer.getFirstName());
			ps.setString(2, customer.getLastName());
			ps.setString(3, String.valueOf(customer.getGender()));
			ps.setDate(4, customer.getDob());
			ps.setString(5, customer.getEmail());

			int res = ps.executeUpdate();
			if(res != 1) {
				throw new DAOException("Create Did Not Update Expected Number Of Rows");
			}

			// REQUIREMENT: Copy the generated auto-increment primary key to the
			// customer ID.
			ResultSet keyRS = ps.getGeneratedKeys();
			keyRS.next();
			int lastKey = keyRS.getInt(1);
			customer.setId((long) lastKey);

			return customer;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public Customer retrieve(Connection connection, Long id) throws SQLException, DAOException {
		String selectQuery = "SELECT id, first_name, last_name, gender, dob, email "
		        + "FROM customer where id = ?";
		
		if (id == null) {
			throw new DAOException("Trying to retrieve Customer with NULL ID");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectQuery);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return null;
			}

			Customer cust = new Customer();
			cust.setId(rs.getLong("id"));
			cust.setFirstName(rs.getString("first_name"));
			cust.setLastName(rs.getString("last_name"));
			cust.setGender(rs.getString("gender").charAt(0));
			cust.setDob(rs.getDate("dob"));
			cust.setEmail(rs.getString("email"));

			return cust;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public int update(Connection connection, Customer customer) throws SQLException, DAOException {
		String updateSQL = "UPDATE customer SET first_name = ?, last_name = ?, gender = ?, dob = ?, email = ? "
		        + "WHERE id = ?;";

		if (customer.getId() == null) {
			throw new DAOException("Trying to update Customer with NULL ID");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(updateSQL);
			ps.setString(1, customer.getFirstName());
			ps.setString(2, customer.getLastName());
			ps.setString(3, String.valueOf(customer.getGender()));
			ps.setDate(4, customer.getDob());
			ps.setString(5, customer.getEmail());
			ps.setLong(6, customer.getId());

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
		String deleteSQL = "DELETE FROM CUSTOMER WHERE id = ?;";
		
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
		String selectQuery = "SELECT id, first_name, last_name, gender, dob, email "
		        + "FROM customer as c JOIN address as a on c.id = a.CUSTOMER_id "
				+ "where a.zipCode = ?";
		
		List<Customer> custs = new ArrayList<>();
		
		if (zipCode == null) {
			throw new DAOException("Trying to retrieve Customer with NULL zipCode");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectQuery);
			ps.setString(1, zipCode);
			ResultSet rs = ps.executeQuery();
			
			if (!rs.next()) {
				return custs;
			}
			
			do {
				Customer cust = new Customer();
				cust.setId(rs.getLong("id"));
				cust.setFirstName(rs.getString("first_name"));
				cust.setLastName(rs.getString("last_name"));
				cust.setGender(rs.getString("gender").charAt(0));
				cust.setDob(rs.getDate("dob"));
				cust.setEmail(rs.getString("email"));
				
				custs.add(cust);
			} while(rs.next());
			
			

			return custs;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public List<Customer> retrieveByDOB(Connection connection, Date startDate, Date endDate)
			throws SQLException, DAOException {
		String selectQuery = "SELECT id, first_name, last_name, gender, dob, email "
		        + "FROM customer where dob BETWEEN ? and ?";
		
		ArrayList<Customer> custs = new ArrayList<>();
		
		if (startDate == null || endDate == null) {
			throw new DAOException("Trying to retrieve Customer with NULL startDate or endDate");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectQuery);
			ps.setDate(1, startDate);
			ps.setDate(2, endDate);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return null;
			}
			
			do {
				Customer cust = new Customer();
				cust.setId(rs.getLong("id"));
				cust.setFirstName(rs.getString("first_name"));
				cust.setLastName(rs.getString("last_name"));
				cust.setGender(rs.getString("gender").charAt(0));
				cust.setDob(rs.getDate("dob"));
				cust.setEmail(rs.getString("email"));
				
				custs.add(cust);
			} while(rs.next());

			return custs;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}
	
}
