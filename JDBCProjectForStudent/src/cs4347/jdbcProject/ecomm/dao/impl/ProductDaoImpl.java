package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.ProductDAO;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class ProductDaoImpl implements ProductDAO
{

	@Override
	public Product create(Connection connection, Product product) throws SQLException, DAOException {
		String insertSQL = 
				"INSERT INTO PRODUCT (prod_name, prod_description, prod_category, prod_upc) "
				+ "VALUES (?, ?, ?, ?);";
		
		// Requirement: Create operations require that the product's ID is null
		// before being inserted into the table.
		if (product.getId() != null) {
			throw new DAOException("Trying to insert Product with NON-NULL ID");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, product.getProdName());
			ps.setString(2, product.getProdDescription());
			ps.setInt(3, product.getProdCategory());
			ps.setString(4, product.getProdUPC());

			int res = ps.executeUpdate();
			if(res != 1) {
				throw new DAOException("Create Did Not Update Expected Number Of Rows");
			}

			// REQUIREMENT: Copy the generated auto-increment primary key to the
			// customer ID.
			ResultSet keyRS = ps.getGeneratedKeys();
			keyRS.next();
			int lastKey = keyRS.getInt(1);
			product.setId((long) lastKey);
			
			return product;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public Product retrieve(Connection connection, Long id) throws SQLException, DAOException {
		String selectQuery = "SELECT prod_name, prod_description, prod_category, prod_upc "
		        + "FROM product where id = ?";
		
		if (id == null) {
			throw new DAOException("Trying to retrieve Product with NULL ID");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectQuery);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			
			if (!rs.next()) {
				return null;
			}

			Product product = new Product();
			product.setId(id);
			product.setProdName(rs.getString("prod_name"));
			product.setProdDescription((rs.getString("prod_description")));
			product.setProdCategory(rs.getInt("prod_category"));
			product.setProdUPC(rs.getString("prod_upc"));

			return product;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

	@Override
	public int update(Connection connection, Product product) throws SQLException, DAOException {
		String updateSQL = "UPDATE product SET prod_name = ?, prod_description = ?, prod_category = ?, prod_upc = ? "
		        + "WHERE id = ?;";

		if (product.getId() == null) {
			throw new DAOException("Trying to update Product with NULL ID");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(updateSQL);
			ps.setString(1, product.getProdName());
			ps.setString(2, product.getProdDescription());
			ps.setInt(3, product.getProdCategory());
			ps.setString(4, product.getProdUPC());
			ps.setLong(5, product.getId());

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
		String deleteSQL = "DELETE FROM PRODUCT WHERE id = ?;";
		
		if (id == null) {
			throw new DAOException("Trying to delete Product with NULL ID");
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
	public List<Product> retrieveByCategory(Connection connection, int category) throws SQLException, DAOException {
		String selectQuery = "SELECT id, prod_name, prod_description, prod_category, prod_upc  "
		        + "FROM product where prod_category = ?";
		
		ArrayList<Product> prods = new ArrayList<>();

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectQuery);
			ps.setInt(1, category);
			
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return null;
			}
			
			do {
				Product product = new Product();
				product.setId(rs.getLong("id"));
				product.setProdName(rs.getString("prod_name"));
				product.setProdDescription((rs.getString("prod_description")));
				product.setProdCategory(rs.getInt("prod_category"));
				product.setProdUPC(rs.getString("prod_upc"));
				
				prods.add(product);
			} while(rs.next());

			return prods;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}

	}

	@Override
	public Product retrieveByUPC(Connection connection, String upc) throws SQLException, DAOException {
		String selectQuery = "SELECT id, prod_name, prod_description, prod_category, prod_upc  "
		        + "FROM product where prod_upc = ?";
		
		if (upc == null) {
			throw new DAOException("Trying to retrieve Product with NULL upc");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectQuery);
			ps.setString(1, upc);
			
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return null;
			}
			
			Product product = new Product();
			product.setId(rs.getLong("id"));
			product.setProdName(rs.getString("prod_name"));
			product.setProdDescription((rs.getString("prod_description")));
			product.setProdCategory(rs.getInt("prod_category"));
			product.setProdUPC(rs.getString("prod_upc"));

			return product;
		}
		finally {
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}

}
