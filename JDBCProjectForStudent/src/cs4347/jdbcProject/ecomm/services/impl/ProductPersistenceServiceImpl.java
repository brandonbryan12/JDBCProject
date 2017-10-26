package cs4347.jdbcProject.ecomm.services.impl;

import javax.sql.DataSource;

import cs4347.jdbcProject.ecomm.services.ProductPersistenceService;

public class ProductPersistenceServiceImpl implements ProductPersistenceService
{
	private DataSource dataSource;

	public ProductPersistenceServiceImpl(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

}
