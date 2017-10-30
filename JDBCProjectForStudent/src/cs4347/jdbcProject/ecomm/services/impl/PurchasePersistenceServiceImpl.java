package cs4347.jdbcProject.ecomm.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import cs4347.jdbcProject.ecomm.dao.ProductDAO;
import cs4347.jdbcProject.ecomm.dao.PurchaseDAO;
import cs4347.jdbcProject.ecomm.dao.impl.ProductDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.PurchaseDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.entity.Purchase;
import cs4347.jdbcProject.ecomm.services.PurchasePersistenceService;
import cs4347.jdbcProject.ecomm.services.PurchaseSummary;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class PurchasePersistenceServiceImpl implements PurchasePersistenceService
{
	private DataSource dataSource;

	public PurchasePersistenceServiceImpl(DataSource dataSource)
	{
		
		this.dataSource = dataSource;
	}

	@Override
	public Purchase create(Purchase purchase) throws SQLException, DAOException 
	{
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();

		Connection connection = dataSource.getConnection();
		try {			
			Purchase pur = purchaseDAO.create(connection, purchase);
			
			return pur;
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
	public Purchase retrieve(Long id) throws SQLException, DAOException {
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			Purchase pur = purchaseDAO.retrieve(connection, id);
			Long prodID = pur.getId();

			if (prodID == null) {
				throw new DAOException("Purchase ID is null");
			}
			
			return pur;
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
	public int update(Purchase purchase) throws SQLException, DAOException {
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			connection.setAutoCommit(false);
			
			int rows = purchaseDAO.update(connection, purchase);

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
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
		
		Connection connection = dataSource.getConnection();
		try {
			connection.setAutoCommit(false);
			
			int rows = purchaseDAO.delete(connection, id);
			
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
	public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException {
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			List<Purchase> purs = purchaseDAO.retrieveForCustomerID(connection, customerID);
			
			return purs;
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
	public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException {
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			PurchaseSummary summary = purchaseDAO.retrievePurchaseSummary(connection, customerID);
			
			return summary;
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
	public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException {
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			List<Purchase> purs = purchaseDAO.retrieveForProductID(connection, productID);
			
			return purs;
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
