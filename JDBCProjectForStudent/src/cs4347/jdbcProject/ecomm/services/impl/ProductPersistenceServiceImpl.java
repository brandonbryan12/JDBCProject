package cs4347.jdbcProject.ecomm.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import cs4347.jdbcProject.ecomm.dao.CreditCardDAO;
import cs4347.jdbcProject.ecomm.dao.CustomerDAO;
import cs4347.jdbcProject.ecomm.dao.ProductDAO;
import cs4347.jdbcProject.ecomm.dao.impl.CreditCardDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.CustomerDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.ProductDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.services.ProductPersistenceService;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class ProductPersistenceServiceImpl implements ProductPersistenceService
{
	private DataSource dataSource;

	public ProductPersistenceServiceImpl(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	@Override
	public Product create(Product product) throws SQLException, DAOException {
		ProductDAO productDAO = new ProductDaoImpl();

		Connection connection = dataSource.getConnection();
		try {			
			Product prod = productDAO.create(connection, product);
			
			return prod;
		}
		catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public Product retrieve(Long id) throws SQLException, DAOException {
		ProductDAO productDAO = new ProductDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			Product prod = productDAO.retrieve(connection, id);
			Long prodID = prod.getId();

			if (prodID == null) {
				throw new DAOException("Product ID is null");
			}
			
			return prod;
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public int update(Product product) throws SQLException, DAOException {
		ProductDAO productDAO = new ProductDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			connection.setAutoCommit(false);
			
			int rows = productDAO.update(connection, product);

			connection.commit();
			
			return rows;
		}
		catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
		finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public int delete(Long id) throws SQLException, DAOException {
		ProductDAO productDAO = new ProductDaoImpl();
		
		Connection connection = dataSource.getConnection();
		try {
			connection.setAutoCommit(false);
			
			int rows = productDAO.delete(connection, id);
			
			connection.commit();
			
			return rows;
		}
		catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
		finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public Product retrieveByUPC(String upc) throws SQLException, DAOException {
		ProductDAO productDAO = new ProductDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			Product prod = productDAO.retrieveByUPC(connection, upc);
			Long prodID = prod.getId();

			if (prodID == null) {
				throw new DAOException("Product ID is null");
			}
			
			return prod;
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public List<Product> retrieveByCategory(int category) throws SQLException, DAOException {
		ProductDAO productDAO = new ProductDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			List<Product> prods = productDAO.retrieveByCategory(connection, category);
			
			return prods;
		}
		catch (Exception ex) {
			throw ex;
		}
		finally {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

}
