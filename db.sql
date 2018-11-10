
DROP TABLE IF EXISTS `t_params`;
CREATE TABLE `t_params` (
	`id` INT NOT NULL PRIMARY KEY,
	`name` VARCHAR(128) NOT NULL,
	`description` VARCHAR(255) NULL,
	`value` TEXT NULL,
	CONSTRAINT `uid_params_name` UNIQUE INDEX(`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `t_params` (`id`, `name`, `description`, `value`) VALUES(1, 'exchange_rate', 'exchange rate', '5');
INSERT INTO `t_params` (`id`, `name`, `description`, `value`) VALUES(2, 'profit_rate', 'profit rate', '1.3');

DROP TABLE IF EXISTS `t_stores`;
CREATE TABLE `t_stores` (
	`id` INT NOT NULL PRIMARY KEY,
	`name` VARCHAR(255) NOT NULL,
	`website` TEXT NULL,
	`require_login` BOOLEAN NOT NULL,
	`retired` BOOLEAN NOT NULL,
	`last_sync_time` DATETIME NULL,
	`product_count` BIGINT NOT NULL,
	`converter_name` VARCHAR(128) NOT NULL,
	CONSTRAINT `uid_stores_name` UNIQUE INDEX(`name`),
	INDEX `id_stores_retired` (`retired`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `t_stores` (`id`, `name`, `website`, `require_login`, `retired`, `last_sync_time`, `product_count`, `converter_name`) VALUES(1, '华洋', 'http://www.xinniuguoji.com', false, false, null, 0, 'net.ojava.openkit.pricespy.business.converter.HYConverter');
INSERT INTO `t_stores` (`id`, `name`, `website`, `require_login`, `retired`, `last_sync_time`, `product_count`, `converter_name`) VALUES(2, '纽康', 'http://www.niukangguoji.com', false, false, null, 0, 'net.ojava.openkit.pricespy.business.converter.NCConverter');
INSERT INTO `t_stores` (`id`, `name`, `website`, `require_login`, `retired`, `last_sync_time`, `product_count`, `converter_name`) VALUES(3, 'NZH', 'http://www.nzhg.co.nz', true, false, null, 0, 'net.ojava.openkit.pricespy.business.converter.NZHConverter');

DROP TABLE IF EXISTS `t_storeprops`;
CREATE TABLE `t_storeprops` (
	`id` INT NOT NULL PRIMARY KEY,
	`store_id` INT NOT NULL,
	`name` VARCHAR(255) NOT NULL,
	`value` TEXT NULL,
	CONSTRAINT `uid_storeprops_store_name` UNIQUE INDEX(`store_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `t_storeprops` (`id`, `store_id`, `name`, `value`) VALUES(1, 1, 'login_url', 'http://www.xinniuguoji.com/?passport-verify.html');
INSERT INTO `t_storeprops` (`id`, `store_id`, `name`, `value`) VALUES(2, 1, 'login_form', 'login=stevencn76&password=lion1976');
INSERT INTO `t_storeprops` (`id`, `store_id`, `name`, `value`) VALUES(4, 1, 'product_url', 'http://www.xinniuguoji.com/?product-#pid.html');
INSERT INTO `t_storeprops` (`id`, `store_id`, `name`, `value`) VALUES(5, 1, 'min_productid', '5001');
INSERT INTO `t_storeprops` (`id`, `store_id`, `name`, `value`) VALUES(6, 1, 'max_productid', '9999');

INSERT INTO `t_storeprops` (`id`, `store_id`, `name`, `value`) VALUES(7, 2, 'login_url', 'http://niukangguoji.com/?passport-verify.html');
INSERT INTO `t_storeprops` (`id`, `store_id`, `name`, `value`) VALUES(8, 2, 'login_form', 'login=stevencn76&password=lion1976');
INSERT INTO `t_storeprops` (`id`, `store_id`, `name`, `value`) VALUES(9, 2, 'product_url', 'http://niukangguoji.com/?product-#pid.html');
INSERT INTO `t_storeprops` (`id`, `store_id`, `name`, `value`) VALUES(10, 2, 'min_productid', '1');
INSERT INTO `t_storeprops` (`id`, `store_id`, `name`, `value`) VALUES(11, 2, 'max_productid', '2300');

INSERT INTO `t_storeprops` (`id`, `store_id`, `name`, `value`) VALUES(12, 3, 'login_url', 'http://www.nzhg.co.nz/user/handleLogin');
INSERT INTO `t_storeprops` (`id`, `store_id`, `name`, `value`) VALUES(13, 3, 'login_form', 'accountVal=0291284151&passwordVal=lion1976');
INSERT INTO `t_storeprops` (`id`, `store_id`, `name`, `value`) VALUES(14, 3, 'product_url', 'http://www.nzhg.co.nz/product/detail?id=100#pid');
INSERT INTO `t_storeprops` (`id`, `store_id`, `name`, `value`) VALUES(15, 3, 'min_productid', '0');
INSERT INTO `t_storeprops` (`id`, `store_id`, `name`, `value`) VALUES(16, 3, 'max_productid', '3000');
INSERT INTO `t_storeprops` (`id`, `store_id`, `name`, `value`) VALUES(17, 3, 'pid_length', '4');


DROP TABLE IF EXISTS `t_products`;
CREATE TABLE `t_products` (
	`id` INT NOT NULL PRIMARY KEY,
	`store_id` INT NOT NULL,
	`product_id` VARCHAR(10) NOT NULL,
	`name` VARCHAR(255) NULL,
	`nz_price` DECIMAL(12, 2) NOT NULL,
	`cn_price` DECIMAL(12, 2) NOT NULL,
	`pic_url` VARCHAR(255) NULL,
	CONSTRAINT `uid_products_store_product` UNIQUE INDEX(`store_id`, `product_id`),
	INDEX `id_products_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




