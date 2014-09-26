
DROP SCHEMA IF EXISTS  `balance`;

CREATE DATABASE `balance` /*!40100 DEFAULT CHARACTER SET latin1 */;

DROP TABLE balance.transaction, balance.category, balance.person, balance.account;

CREATE TABLE balance.person (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  ds_name varchar(250) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE balance.account_type (
	id bigint(20) NOT NULL AUTO_INCREMENT,
	ds_text varchar(300) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE balance.account (
	id bigint(20) NOT NULL AUTO_INCREMENT,
        id_type bigint(20) DEFAULT NULL,
	ds_text varchar(300) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id),
  KEY fk_account_type_idx (id_type),
  CONSTRAINT fk_account_type FOREIGN KEY (id_type) REFERENCES balance.account_type (id) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE balance.category (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  id_parent bigint(20) DEFAULT NULL,
  id_person bigint(20) DEFAULT NULL,
  ds_text varchar(300) NOT NULL,
  nr_order int(11) NOT NULL DEFAULT 1,
  is_positive boolean NOT NULL DEFAULT FALSE,
  is_transfer boolean NOT NULL DEFAULT FALSE,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id),
  KEY fk_category_parent_idx (id_parent),
  KEY fk_transaction_person_idx (id_person),
  CONSTRAINT fk_category_parent FOREIGN KEY (id_parent) REFERENCES balance.category (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_transaction_person FOREIGN KEY (id_person) REFERENCES balance.person (id) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


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
  CONSTRAINT fk_transaction_category FOREIGN KEY (id_category) REFERENCES balance.category (id) ON DELETE NO ACTION ON UPDATE NO ACTION ,
  CONSTRAINT fk_transaction_account FOREIGN KEY (id_account) REFERENCES balance.account (id) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE balance.balance (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  id_account bigint(20) NOT NULL,
  id_transaction bigint(20),
  dt_balance datetime NOT NULL,
  is_positive boolean NOT NULL DEFAULT TRUE,
  vl_value decimal(19,4) DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id),
  KEY fk_balance_account_idx (id_account),
  KEY fk_balance_transaction_idx (id_transaction),
  CONSTRAINT fk_balance_account FOREIGN KEY (id_account) REFERENCES balance.account (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_balance_transaction FOREIGN KEY (id_transaction) REFERENCES balance.transaction (id) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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

/*

select * from transaction where id_category in (81, 82, 83)
select * from transaction where id_category in (27, 28, 29)
select * from transaction where id_category = 2
select * from transaction where vl_value = 2983.45
delete from category where id in (81, 82, 83)

select id from balance where id_transaction is not null

select * from transaction where id in (select id_transaction from balance where id_transaction is not null)  and ds_text like '%Payment%';

delete from transaction where id in (select id_transaction from balance where id_transaction is not null)  and ds_text like '%Payment%';

180,181,182,183,184,185,186,201,202,209,210,212,213,214,216,235,236
1,3,5,7,8,9,10,12,13,14,18,19,23,29,42,43,44

select case balance0_.is_positive when 1 then balance0_.vl_value else balance0_.vl_value*-1 end as col_0_0_ from balance balance0_ left outer join account account1_ on balance0_.id_account=account1_.id where account1_.id=7 
ORDER BY balance0_.dt_balance DESC LIMIT 1;

select * from transaction transactio0_ left outer join category category1_ on transactio0_.id_category=category1_.id left outer join account account2_ on transactio0_.id_account=account2_.id where account2_.id=1

ALTER TABLE balance.balance ADD id_transaction bigint(20) NULL ;
ALTER TABLE balance ADD CONSTRAINT fk_balance_transaction FOREIGN KEY ( id_transaction ) REFERENCES transaction ( id );
CREATE INDEX fk_balance_transaction_idx ON balance ( id_transaction );

CREATE TABLE balance.account_type (
	id bigint(20) NOT NULL AUTO_INCREMENT,
	ds_text varchar(300) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY id_UNIQUE (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO balance.account_type (ds_text) VALUES ('Checking');
INSERT INTO balance.account_type (ds_text) VALUES ('Savings');
INSERT INTO balance.account_type (ds_text) VALUES ('Credit Card');
INSERT INTO balance.account_type (ds_text) VALUES ('Investment');

select case balance0_.is_positive when 1 then balance0_.vl_value else balance0_.vl_value*-1 end as col_0_0_ from balance balance0_ left outer join account account1_ on balance0_.id_account=account1_.id where account1_.id_type<>3 and (balance0_.dt_balance between '2014-09-01 00:00:00' and '2014-09-30 00:00:00') and account1_.id=3
select sum(case balance0_.is_positive when 1 then balance0_.vl_value else balance0_.vl_value*-1 end) as col_0_0_ from balance balance0_ left outer join account account1_ on balance0_.id_account=account1_.id where account1_.id_type<>3 and balance0_.dt_balance>='2014-09-01 00:00:00' and balance0_.dt_balance<='2014-09-30 00:00:00';
select account0_.id as id1_0_0_, balances1_.id as id1_2_1_, account0_.ds_text as ds_text2_0_0_, account0_.id_type as id_type3_0_0_, balances1_.id_account as id_accou5_2_1_, balances1_.dt_balance as dt_balan2_2_1_, balances1_.is_positive as is_posit3_2_1_, balances1_.id_transaction as id_trans6_2_1_, balances1_.vl_value as vl_value4_2_1_, balances1_.id_account as id_accou5_0_0__, balances1_.id as id1_2_0__ from account account0_ inner join balance balances1_ on account0_.id=balances1_.id_account where account0_.id=1 and balances1_.dt_balance>='2014-08-01 00:00:00' and balances1_.dt_balance<='2014-10-01 00:00:00' order by balances1_.dt_balance ASC 


*/
