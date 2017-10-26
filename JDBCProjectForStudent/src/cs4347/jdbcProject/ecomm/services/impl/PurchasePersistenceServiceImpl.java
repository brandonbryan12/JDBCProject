package cs4347.jdbcProject.ecomm.services.impl;

import javax.sql.DataSource;

import cs4347.jdbcProject.ecomm.services.PurchasePersistenceService;

public class PurchasePersistenceServiceImpl implements PurchasePersistenceService
{
	private DataSource dataSource;

	public PurchasePersistenceServiceImpl(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

}
