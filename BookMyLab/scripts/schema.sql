-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema autolab
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `autolab` ;

-- -----------------------------------------------------
-- Schema autolab
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `autolab` DEFAULT CHARACTER SET utf8 ;
USE `autolab` ;

-- -----------------------------------------------------
-- Table `autolab`.`resource_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `autolab`.`resource_type` ;

CREATE TABLE IF NOT EXISTS `autolab`.`resource_type` (
  `resource_id` INT(11) NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(25) NOT NULL,
  `active` BIT(1) NOT NULL DEFAULT b'1',
  `description` VARCHAR(150) NULL DEFAULT NULL,
  PRIMARY KEY (`resource_id`),
  UNIQUE INDEX `uk_res_code` (`code` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 33
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autolab`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `autolab`.`user` ;

CREATE TABLE IF NOT EXISTS `autolab`.`user` (
  `user_id` INT(11) NOT NULL AUTO_INCREMENT,
  `password` VARCHAR(250) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `middle_name` VARCHAR(45) NULL DEFAULT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `gender` CHAR(1) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `verified` TINYINT(1) NULL DEFAULT '0',
  `failed_logins` SMALLINT(6) NULL DEFAULT '0',
  `last_login` DATETIME NULL DEFAULT NULL,
  `version_no` INT(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `uk_UserEmail` (`email` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `autolab`.`resource_booking`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `autolab`.`resource_booking` ;

CREATE TABLE IF NOT EXISTS `autolab`.`resource_booking` (
  `booking_id` INT(11) NOT NULL AUTO_INCREMENT,
  `booking_date` DATE NOT NULL,
  `user_id` INT(11) NOT NULL,
  `status` VARCHAR(15) NOT NULL DEFAULT 'PENDING',
  `status_notes` VARCHAR(150) NULL,
  `start_time` CHAR(5) NOT NULL,
  `end_time` CHAR(5) NOT NULL,
  `lab` VARCHAR(10) NOT NULL,
  `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `instrument_date` DATE NOT NULL,
  `instrument_type` VARCHAR(10) NOT NULL,
  `instrument_no` VARCHAR(30) NOT NULL,
  `instrument_amt` DECIMAL NOT NULL,
  PRIMARY KEY (`booking_id`),
  UNIQUE INDEX `uk_booking` (`user_id` ASC, `booking_date` ASC, `start_time` ASC),
  INDEX `booking_date_idx` (`booking_date` ASC),
  INDEX `fk_res_booking_user_idx` (`user_id` ASC),
  CONSTRAINT `fk_slot_booking_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `autolab`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 34
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autolab`.`analysis_mode`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `autolab`.`analysis_mode` ;

CREATE TABLE IF NOT EXISTS `autolab`.`analysis_mode` (
  `analysis_id` INT(11) NOT NULL AUTO_INCREMENT,
  `booking_id` INT(11) NOT NULL,
  `resource_id` INT(11) NOT NULL,
  `samples` SMALLINT(6) NOT NULL,
  `results` SMALLINT(6) NOT NULL,
  `charges` DECIMAL(10,0) NOT NULL,
  `remarks` VARCHAR(250) NULL,
  PRIMARY KEY (`analysis_id`),
  UNIQUE INDEX `uk_booking_mode` (`booking_id` ASC, `resource_id` ASC),
  INDEX `fk_analysis_modes_booking_idx` (`booking_id` ASC),
  INDEX `fk_analysis_mode_res_type_idx` (`resource_id` ASC),
  CONSTRAINT `fk_analysis_mode_res_type`
    FOREIGN KEY (`resource_id`)
    REFERENCES `autolab`.`resource_type` (`resource_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_analysis_modes_booking`
    FOREIGN KEY (`booking_id`)
    REFERENCES `autolab`.`resource_booking` (`booking_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 28
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autolab`.`audit_logs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `autolab`.`audit_logs` ;

CREATE TABLE IF NOT EXISTS `autolab`.`audit_logs` (
  `log_id` INT(11) NOT NULL AUTO_INCREMENT,
  `app_user` VARCHAR(150) NOT NULL,
  `activity` VARCHAR(45) NOT NULL,
  `client_ip` VARCHAR(45) NOT NULL,
  `activity_ts` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autolab`.`resource_pricing`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `autolab`.`resource_pricing` ;

CREATE TABLE IF NOT EXISTS `autolab`.`resource_pricing` (
  `pricing_id` INT(11) NOT NULL AUTO_INCREMENT,
  `resource_id` INT(11) NOT NULL,
  `per_sample_price` DECIMAL(10,0) NOT NULL,
  `per_result_price` DECIMAL(10,0) NOT NULL,
  `is_current` BIT(1) NOT NULL DEFAULT b'1',
  `valid_from` DATETIME NOT NULL,
  `valid_till` DATETIME NULL DEFAULT NULL,
  `remarks` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`pricing_id`),
  INDEX `fk_resource_pricing_res_type_idx` (`resource_id` ASC),
  CONSTRAINT `fk_resource_pricing_1`
    FOREIGN KEY (`resource_id`)
    REFERENCES `autolab`.`resource_type` (`resource_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 37
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autolab`.`billing`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `autolab`.`billing` ;

CREATE TABLE IF NOT EXISTS `autolab`.`billing` (
  `bill_id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `booking_id` INT(11) NOT NULL,
  `amount` DECIMAL(10,0) NOT NULL DEFAULT '0',
  `charge_type` VARCHAR(20) NOT NULL,
  `status` VARCHAR(45) NOT NULL DEFAULT 'PENDING',
  `bill_date` DATE NOT NULL,
  PRIMARY KEY (`bill_id`),
  INDEX `fk_billing_user_idx` (`user_id` ASC),
  INDEX `fk_billing_booking_idx` (`booking_id` ASC),
  CONSTRAINT `fk_billing_booking`
    FOREIGN KEY (`booking_id`)
    REFERENCES `autolab`.`resource_booking` (`booking_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_billing_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `autolab`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autolab`.`booking_category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `autolab`.`booking_category` ;

CREATE TABLE IF NOT EXISTS `autolab`.`booking_category` (
  `category_id` INT(11) NOT NULL AUTO_INCREMENT,
  `category` VARCHAR(20) NOT NULL,
  `opens_at` SMALLINT(6) NOT NULL,
  `closes_at` SMALLINT(6) NOT NULL,
  `week_day` TINYINT(4) NOT NULL,
  `description` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  UNIQUE INDEX `uk_booking_category` (`category` ASC, `opens_at` ASC, `week_day` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autolab`.`discount`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `autolab`.`discount` ;

CREATE TABLE IF NOT EXISTS `autolab`.`discount` (
  `discount_id` INT(11) NOT NULL AUTO_INCREMENT,
  `rate` DECIMAL(10,0) NOT NULL,
  `discount_code` VARCHAR(45) NOT NULL,
  `active` BIT(1) NOT NULL,
  `description` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`discount_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autolab`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `autolab`.`role` ;

CREATE TABLE IF NOT EXISTS `autolab`.`role` (
  `role_id` INT(11) NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(15) NOT NULL,
  `role_description` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`role_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 32
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autolab`.`spm_lab_booking`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `autolab`.`spm_lab_booking` ;

CREATE TABLE IF NOT EXISTS `autolab`.`spm_lab_booking` (
  `spm_id` INT(11) NOT NULL AUTO_INCREMENT,
  `booking_id` INT(11) NOT NULL,
  `material` TEXT NOT NULL,
  `prep_method` TEXT NOT NULL,
  `material_type` VARCHAR(45) NOT NULL,
  `toxic` BIT(1) NOT NULL,
  `conducting` BIT(1) NOT NULL,
  `other_requirements` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`spm_id`),
  INDEX `fk_spm_lab_booking_1_idx` (`booking_id` ASC),
  CONSTRAINT `fk_spm_lab_booking_1`
    FOREIGN KEY (`booking_id`)
    REFERENCES `autolab`.`resource_booking` (`booking_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 20
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autolab`.`xrd_lab_booking`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `autolab`.`xrd_lab_booking` ;

CREATE TABLE IF NOT EXISTS `autolab`.`xrd_lab_booking` (
  `xrd_id` INT(11) NOT NULL AUTO_INCREMENT,
  `booking_id` INT(11) NOT NULL,
  `material` TEXT NOT NULL,
  `prep_method` TEXT NOT NULL,
  `material_type` VARCHAR(45) NOT NULL,
  `scan_angle` VARCHAR(20) NOT NULL,
  `toxic` BIT(1) NOT NULL,
  `texture` BIT(1) NOT NULL,
  `residual_stress` BIT(1) NOT NULL,
  `saxs` BIT(1) NOT NULL,
  `other_requirements` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`xrd_id`),
  INDEX `fk_xrd_lab_booking_1_idx` (`booking_id` ASC),
  CONSTRAINT `fk_xrd_lab_booking_1`
    FOREIGN KEY (`booking_id`)
    REFERENCES `autolab`.`resource_booking` (`booking_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 20
DEFAULT CHARACTER SET = utf8;



-- -----------------------------------------------------
-- Table `autolab`.`static_data`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `autolab`.`static_data` ;

CREATE TABLE IF NOT EXISTS `autolab`.`static_data` (
  `data_id` INT(11) NOT NULL AUTO_INCREMENT,
  `data_key` VARCHAR(150) NOT NULL,
  `is_array` BIT(1) NOT NULL DEFAULT b'0',
  `description` VARCHAR(150) NULL DEFAULT NULL,
  PRIMARY KEY (`data_id`),
  UNIQUE INDEX `uk_data_key` (`data_key` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 19
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autolab`.`static_data_value`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `autolab`.`static_data_value` ;

CREATE TABLE IF NOT EXISTS `autolab`.`static_data_value` (
  `value_id` INT(11) NOT NULL AUTO_INCREMENT,
  `data_id` INT(11) NOT NULL,
  `data_value` VARCHAR(250) NULL DEFAULT NULL,
  PRIMARY KEY (`value_id`),
  UNIQUE INDEX `uk_data_value` (`data_id` ASC, `data_value` ASC),
  INDEX `fk_static_data_value_1_idx` (`data_id` ASC),
  CONSTRAINT `fk_static_data_value_1`
    FOREIGN KEY (`data_id`)
    REFERENCES `autolab`.`static_data` (`data_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 31
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autolab`.`user_profile`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `autolab`.`user_profile` ;

CREATE TABLE IF NOT EXISTS `autolab`.`user_profile` (
  `profile_id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `org_type` VARCHAR(150) NOT NULL,
  `org` VARCHAR(150) NOT NULL,
  `dept` VARCHAR(150) NOT NULL,
  `designation` VARCHAR(45) NOT NULL,
  `office_phone` VARCHAR(45) NOT NULL,
  `address` VARCHAR(1000) NOT NULL,
  `city` VARCHAR(250) NOT NULL,
  `state` VARCHAR(45) NOT NULL,
  `postal_code` VARCHAR(10) NOT NULL,
  `web_page` VARCHAR(250) NULL DEFAULT NULL,
  PRIMARY KEY (`profile_id`),
  UNIQUE INDEX `uk_user_id` (`user_id` ASC),
  INDEX `fk_user_profile_1_idx` (`user_id` ASC),
  CONSTRAINT `fk_user_profile_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `autolab`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `autolab`.`user_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `autolab`.`user_role` ;

CREATE TABLE IF NOT EXISTS `autolab`.`user_role` (
  `user_id` INT(11) NOT NULL,
  `role_id` INT(11) NOT NULL,
  UNIQUE INDEX `index1` (`user_id` ASC, `role_id` ASC),
  INDEX `fk_PersonRole_2_idx` (`role_id` ASC),
  CONSTRAINT `fk_user_role_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `autolab`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_role_2`
    FOREIGN KEY (`role_id`)
    REFERENCES `autolab`.`role` (`role_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

USE `autolab`;

DELIMITER $$

USE `autolab`$$
DROP TRIGGER IF EXISTS `autolab`.`resource_booking_BEFORE_INSERT` $$
USE `autolab`$$
CREATE
DEFINER=`autolab`@`%`
TRIGGER `autolab`.`resource_booking_BEFORE_INSERT`
BEFORE INSERT ON `autolab`.`resource_booking`
FOR EACH ROW
BEGIN
	declare msg varchar(128);
    declare err bool;
    if new.status NOT IN ('PENDING', 'SCHEDULED', 'REJECTED', 'FINISHED') then
        set msg = concat('resource_booking: Booking status is invalid.');
        set err = true;
    end if;
    if err then
        signal sqlstate '45000' set message_text = msg;
    end if;
END$$


USE `autolab`$$
DROP TRIGGER IF EXISTS `autolab`.`resource_booking_BEFORE_UPDATE` $$
USE `autolab`$$
CREATE
DEFINER=`autolab`@`%`
TRIGGER `autolab`.`resource_booking_BEFORE_UPDATE`
BEFORE UPDATE ON `autolab`.`resource_booking`
FOR EACH ROW
BEGIN
	declare msg varchar(128);
    declare err bool;
    if new.status NOT IN ('PENDING', 'SCHEDULED', 'REJECTED', 'FINISHED') then
        set msg = concat('resource_booking: Booking status is invalid.');
        set err = true;
    end if;
    if err then
        signal sqlstate '45000' set message_text = msg;
    end if;
END$$


USE `autolab`$$
DROP TRIGGER IF EXISTS `autolab`.`resource_pricing_BEFORE_INSERT` $$
USE `autolab`$$
CREATE
DEFINER=`autolab`@`%`
TRIGGER `autolab`.`resource_pricing_BEFORE_INSERT`
BEFORE INSERT ON `autolab`.`resource_pricing`
FOR EACH ROW
BEGIN
	declare msg varchar(128);
    if new.valid_till != NULL AND new.valid_from < new.valid_till then
        set msg = concat('resource_pricing: Validity dates are incorrect.');
        signal sqlstate '45000' set message_text = msg;
    end if;
END$$


USE `autolab`$$
DROP TRIGGER IF EXISTS `autolab`.`resource_pricing_BEFORE_UPDATE` $$
USE `autolab`$$
CREATE
DEFINER=`autolab`@`%`
TRIGGER `autolab`.`resource_pricing_BEFORE_UPDATE`
BEFORE UPDATE ON `autolab`.`resource_pricing`
FOR EACH ROW
BEGIN
	declare msg varchar(128);
    if new.valid_till != NULL AND new.valid_from < new.valid_till then
        set msg = concat('resource_pricing: Validity dates are incorrect.');
        signal sqlstate '45000' set message_text = msg;
    end if;
END$$


USE `autolab`$$
DROP TRIGGER IF EXISTS `autolab`.`billing_BEFORE_INSERT` $$
USE `autolab`$$
CREATE
DEFINER=`autolab`@`%`
TRIGGER `autolab`.`billing_BEFORE_INSERT`
BEFORE INSERT ON `autolab`.`billing`
FOR EACH ROW
BEGIN
	declare msg varchar(128);
    if new.charge_type NOT IN ('Cost', 'VAT', 'Service Tax', 'Discount', 'Other Levy') then
        set msg = concat('billing: Charge type is invalid.');
        signal sqlstate '45000' set message_text = msg;
    end if;
    if new.status NOT IN ('PENDING', 'PAID') then
        set msg = concat('billing: Bill status is invalid.');
        signal sqlstate '45000' set message_text = msg;
    end if;
END$$


USE `autolab`$$
DROP TRIGGER IF EXISTS `autolab`.`billing_BEFORE_UPDATE` $$
USE `autolab`$$
CREATE
DEFINER=`autolab`@`%`
TRIGGER `autolab`.`billing_BEFORE_UPDATE`
BEFORE UPDATE ON `autolab`.`billing`
FOR EACH ROW
BEGIN
	declare msg varchar(128);
    if new.charge_type NOT IN ('Cost', 'VAT', 'Service Tax', 'Discount', 'Other Levy') then
        set msg = concat('billing: Charge type is invalid.');
        signal sqlstate '45000' set message_text = msg;
    end if;
    if new.status NOT IN ('PENDING', 'PAID') then
        set msg = concat('billing: Bill status is invalid.');
        signal sqlstate '45000' set message_text = msg;
    end if;
END$$


USE `autolab`$$
DROP TRIGGER IF EXISTS `autolab`.`booking_category_BEFORE_INSERT` $$
USE `autolab`$$
CREATE
DEFINER=`autolab`@`%`
TRIGGER `autolab`.`booking_category_BEFORE_INSERT`
BEFORE INSERT ON `autolab`.`booking_category`
FOR EACH ROW
BEGIN
	declare msg varchar(128);
    declare err bool;
    if new.opens_at < 0 or new.opens_at > 2359 then
        set msg = concat('booking_category: opens_at value is invalid.');
        set err = true;
	end if;
    if new.closes_at <= new.opens_at then
        set msg = concat('booking_category: closes_at value is invalid.');
        set err = true;
	end if;
    if err then
        signal sqlstate '45000' set message_text = msg;
    end if;
END$$


USE `autolab`$$
DROP TRIGGER IF EXISTS `autolab`.`booking_category_BEFORE_UPDATE` $$
USE `autolab`$$
CREATE
DEFINER=`autolab`@`%`
TRIGGER `autolab`.`booking_category_BEFORE_UPDATE`
BEFORE UPDATE ON `autolab`.`booking_category`
FOR EACH ROW
BEGIN
	declare msg varchar(128);
    declare err bool;
    if new.opens_at < 0 or new.opens_at > 2359 then
        set msg = concat('booking_category: opens_at value is invalid.');
        set err = true;
	end if;
    if new.closes_at <= new.opens_at then
        set msg = concat('booking_category: closes_at value is invalid.');
        set err = true;
	end if;
    if err then
        signal sqlstate '45000' set message_text = msg;
    end if;
END$$


DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
