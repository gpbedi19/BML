INSERT INTO autolab.role (`role`,`role_description`) VALUES ('MANAGER','Lab manager');
INSERT INTO autolab.role (`role`,`role_description`) VALUES ('USER','General user');
INSERT INTO autolab.role (`role`,`role_description`) VALUES ('ADMIN','Super user');

INSERT INTO autolab.static_data (`data_key`,`is_array`,`description`) VALUES ('org.type', b'1','Organization types');
INSERT INTO autolab.static_data (`data_key`,`is_array`,`description`) VALUES ('designation', b'1','Designation of user');

/* Setup the organization types. */
INSERT INTO autolab.static_data_value (`data_id`,`data_value`) VALUES ((SELECT data_id FROM autolab.static_data WHERE data_key='org.type'),'Academic');
INSERT INTO autolab.static_data_value (`data_id`,`data_value`) VALUES ((SELECT data_id FROM autolab.static_data WHERE data_key='org.type'),'Govt. lab');
INSERT INTO autolab.static_data_value (`data_id`,`data_value`) VALUES ((SELECT data_id FROM autolab.static_data WHERE data_key='org.type'),'Individual');
INSERT INTO autolab.static_data_value (`data_id`,`data_value`) VALUES ((SELECT data_id FROM autolab.static_data WHERE data_key='org.type'),'Industry');

/* Setup the designations. */
INSERT INTO autolab.static_data_value (`data_id`,`data_value`) VALUES ((SELECT data_id FROM autolab.static_data WHERE data_key='designation'),'Professor');
INSERT INTO autolab.static_data_value (`data_id`,`data_value`) VALUES ((SELECT data_id FROM autolab.static_data WHERE data_key='designation'),'Scientist');

/* Setup resource types which we support. */
INSERT INTO `autolab`.`resource_type`(`code`,`description`) VALUES ('SEM', 'Normal SEM');
INSERT INTO `autolab`.`resource_type`(`code`,`description`) VALUES ('SEM-EDS', 'SEM with EDS');
INSERT INTO `autolab`.`resource_type`(`code`,`description`) VALUES ('Gold Carbon Coating', 'Gold Carbon Coating');
INSERT INTO `autolab`.`resource_type`(`code`,`description`) VALUES ('SPM-AFM', 'SPM-AFM');
INSERT INTO `autolab`.`resource_type`(`code`,`description`) VALUES ('SPM-MFM', 'SPM-MFM');
INSERT INTO `autolab`.`resource_type`(`code`,`description`) VALUES ('SPM-STM', 'SPM-STM');
INSERT INTO `autolab`.`resource_type`(`code`,`description`) VALUES ('SPM-EFM', 'SPM-EFM');
INSERT INTO `autolab`.`resource_type`(`code`,`description`) VALUES ('SPM-NLITH', 'SPM-NLITH');
INSERT INTO `autolab`.`resource_type`(`code`,`description`) VALUES ('SPM-NOXID', 'SPM-NOXID');
INSERT INTO `autolab`.`resource_type`(`code`,`description`) VALUES ('SPM-FCI', 'SPM-FCI');
INSERT INTO `autolab`.`resource_type`(`code`,`description`) VALUES ('XRD-POWDER', 'XRD-POWDER');
INSERT INTO `autolab`.`resource_type`(`code`,`description`) VALUES ('XRD-OTHER', 'XRD-OTHER');
INSERT INTO `autolab`.`resource_type`(`code`,`description`) VALUES ('OTHER', 'OTHER');

/* Setup prices (in INR) information for resources. */
INSERT INTO `autolab`.`resource_pricing` (`resource_id`, `per_sample_price`, `per_result_price`, `is_current`, `valid_from`) VALUES ((SELECT resource_id from autolab.resource_type WHERE code='SEM'), '1500', 10, b'1', current_date());
INSERT INTO `autolab`.`resource_pricing` (`resource_id`, `per_sample_price`, `per_result_price`, `is_current`, `valid_from`) VALUES ((SELECT resource_id from autolab.resource_type WHERE code='SEM-EDS'), '2000', 10, b'1', current_date());
INSERT INTO `autolab`.`resource_pricing` (`resource_id`, `per_sample_price`, `per_result_price`, `is_current`, `valid_from`) VALUES ((SELECT resource_id from autolab.resource_type WHERE code='Gold Carbon Coating'), '200', 10, b'1', current_date());
INSERT INTO `autolab`.`resource_pricing` (`resource_id`, `per_sample_price`, `per_result_price`, `is_current`, `valid_from`) VALUES ((SELECT resource_id from autolab.resource_type WHERE code='SPM-AFM'), '2400', 20, b'1', current_date());
INSERT INTO `autolab`.`resource_pricing` (`resource_id`, `per_sample_price`, `per_result_price`, `is_current`, `valid_from`) VALUES ((SELECT resource_id from autolab.resource_type WHERE code='SPM-MFM'), '4000', 20, b'1', current_date());
INSERT INTO `autolab`.`resource_pricing` (`resource_id`, `per_sample_price`, `per_result_price`, `is_current`, `valid_from`) VALUES ((SELECT resource_id from autolab.resource_type WHERE code='SPM-STM'), '1500', 20, b'1', current_date());
INSERT INTO `autolab`.`resource_pricing` (`resource_id`, `per_sample_price`, `per_result_price`, `is_current`, `valid_from`) VALUES ((SELECT resource_id from autolab.resource_type WHERE code='SPM-EFM'), '4000', 20, b'1', current_date());
INSERT INTO `autolab`.`resource_pricing` (`resource_id`, `per_sample_price`, `per_result_price`, `is_current`, `valid_from`) VALUES ((SELECT resource_id from autolab.resource_type WHERE code='SPM-NLITH'), '3500', 20, b'1', current_date());
INSERT INTO `autolab`.`resource_pricing` (`resource_id`, `per_sample_price`, `per_result_price`, `is_current`, `valid_from`) VALUES ((SELECT resource_id from autolab.resource_type WHERE code='SPM-NOXID'), '4800', 20, b'1', current_date());
INSERT INTO `autolab`.`resource_pricing` (`resource_id`, `per_sample_price`, `per_result_price`, `is_current`, `valid_from`) VALUES ((SELECT resource_id from autolab.resource_type WHERE code='SPM-FCI'), '2000', 20, b'1', current_date());
INSERT INTO `autolab`.`resource_pricing` (`resource_id`, `per_sample_price`, `per_result_price`, `is_current`, `valid_from`) VALUES ((SELECT resource_id from autolab.resource_type WHERE code='XRD-POWDER'), '1000', 20, b'1', current_date());
INSERT INTO `autolab`.`resource_pricing` (`resource_id`, `per_sample_price`, `per_result_price`, `is_current`, `valid_from`) VALUES ((SELECT resource_id from autolab.resource_type WHERE code='XRD-OTHER'), '1200', 10, b'1', current_date());

/* Setup discounts (in %) for resource prices. */
INSERT INTO `autolab`.`discount` (`rate`, `discount_code`, `active`, `description`) VALUES ('95', 'SEM_IITRPR', b'1', 'IIT Ropar internal users discount');
INSERT INTO `autolab`.`discount` (`rate`, `discount_code`, `active`, `description`) VALUES ('60', 'SEM_GOVT', b'1', 'Govt. labs discount');

