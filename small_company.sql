-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema simple_company
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema simple_company
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `simple_company` DEFAULT CHARACTER SET utf8 ;
USE `simple_company` ;

-- -----------------------------------------------------
-- Table `simple_company`.`Product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simple_company`.`Product` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `prod_name` VARCHAR(45) NULL,
  `prod_description` VARCHAR(1024) NULL,
  `prod_category` INT NULL,
  `prod_upc` CHAR(12) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `simple_company`.`Customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simple_company`.`Customer` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NULL,
  `last_name` VARCHAR(45) NULL,
  `gender` CHAR(1) NULL,
  `dob` DATE NULL,
  `email` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `simple_company`.`Purchase`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simple_company`.`Purchase` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `purchase_date` TIMESTAMP NULL,
  `purchase_amt` DECIMAL(9,2) NULL,
  `PRODUCT_id` INT NULL,
  `CUSTOMER_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `PRODUCT_id_idx` (`PRODUCT_id` ASC),
  INDEX `CUSTOMER_ID_idx2` (`CUSTOMER_id` ASC),
  CONSTRAINT `fk_PRODUCT_id`
    FOREIGN KEY (`PRODUCT_id`)
    REFERENCES `simple_company`.`Product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk3_CUSTOMER_id`
    FOREIGN KEY (`CUSTOMER_id`)
    REFERENCES `simple_company`.`Customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `simple_company`.`CreditCard`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simple_company`.`CreditCard` (
  `name` VARCHAR(45) NULL,
  `cc_number` VARCHAR(45) NULL,
  `exp_date` VARCHAR(45) NULL,
  `security_code` VARCHAR(45) NULL,
  `CUSTOMER_id` INT NULL,
  INDEX `1CUSTOMER_id_idx` (`CUSTOMER_id` ASC),
  CONSTRAINT `4fk_CUSTOMER_id`
    FOREIGN KEY (`CUSTOMER_id`)
    REFERENCES `simple_company`.`Customer` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `simple_company`.`Address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simple_company`.`Address` (
  `address1` VARCHAR(45) NULL,
  `address2` VARCHAR(45) NULL,
  `city` VARCHAR(45) NULL,
  `state` VARCHAR(45) NULL,
  `zipcode` VARCHAR(45) NULL,
  `CUSTOMER_id` INT NULL,
  INDEX `fk2_CUSTOMER_id_idx` (`CUSTOMER_id` ASC),
  CONSTRAINT `fk2_CUSTOMER_id`
    FOREIGN KEY (`CUSTOMER_id`)
    REFERENCES `simple_company`.`Customer` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
