
DROP SCHEMA IF EXISTS  `balance`;

delimiter $$

CREATE DATABASE `balance` /*!40100 DEFAULT CHARACTER SET latin1 */$$

delimiter $$

CREATE TABLE balance.person (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  ds_name varchar(250) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$


/*delimiter $$

CREATE TABLE `tp_payment` (
  `id` int(11) NOT NULL,
  `ds_text` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$
*/
/*
delimiter $$

CREATE TABLE `balance`.`tp_transaction` (
  `id` int(11) NOT NULL,
  `ds_text` varchar(100) NOT NULL,
  `postive` boolean NOT NULL DEFAULT TRUE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$
*/
delimiter $$

CREATE TABLE balance.account (
	id bigint(20) NOT NULL AUTO_INCREMENT,
	ds_text varchar(300) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$

delimiter $$

CREATE TABLE balance.category (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  id_parent bigint(20) DEFAULT NULL,
  id_person bigint(20) DEFAULT NULL,
  ds_text varchar(300) NOT NULL,
  nr_order int(11) NOT NULL DEFAULT 1,
  is_positive boolean NOT NULL DEFAULT FALSE,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id),
  KEY fk_category_parent_idx (id_parent),
  KEY fk_transaction_person_idx (id_person),
  CONSTRAINT fk_category_parent FOREIGN KEY (id_parent) REFERENCES balance.category (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_transaction_person FOREIGN KEY (id_person) REFERENCES balance.person (id) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$


delimiter $$

CREATE TABLE balance.transaction (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  id_category bigint(20) NOT NULL,
  id_account bigint(20) NOT NULL,
  dt_transaction datetime NOT NULL,
  vl_value decimal(19,4) DEFAULT 0,
  ds_text varchar(1000) NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id),
  KEY fk_transaction_category_idx (id_category),
  KEY fk_transaction_account_idx (id_account),
  CONSTRAINT fk_transaction_category FOREIGN KEY (id_category) REFERENCES balance.category (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_transaction_account FOREIGN KEY (id_account) REFERENCES balance.account (id) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$

delimiter ;
/*
SELECT c.ds_text, c2.ds_text, SUM(t.vl_value), t.dt_transaction FROM balance.category c
LEFT OUTER JOIN balance.category c2 on c.id = c2.id_parent
LEFT OUTER JOIN balance.transaction t on c2.id = t.id_category
where c.id_parent is null 
group by year(t.dt_transaction), month(t.dt_transaction), c.id, c2.id 
order by c.nr_order, c.ds_text, c2.nr_order, c2.ds_text, year(t.dt_transaction), month(t.dt_transaction);

SELECT c2.ds_text, t.id_category, c.ds_text,SUM(t.vl_value), t.dt_transaction FROM balance.transaction t
LEFT OUTER JOIN balance.category c on t.id_category = c.id
LEFT OUTER JOIN balance.category c2 on c.id_parent = c2.id
where t.vl_value is not null 
group by c2.id, year(t.dt_transaction), month(t.dt_transaction) order by year(t.dt_transaction), month(t.dt_transaction), c2.nr_order
*/
