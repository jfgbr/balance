delimiter $$

DROP SCHEMA IF EXISTS  `balance`;

CREATE DATABASE `balance` /*!40100 DEFAULT CHARACTER SET latin1 */$$

delimiter $$

CREATE TABLE `balance`.`person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ds_name` varchar(250) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
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

CREATE TABLE `balance`.`category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_parent` bigint(20) DEFAULT NULL,
  `id_person` bigint(20) DEFAULT NULL,
  `ds_text` varchar(300) NOT NULL,
  `nr_order` int(11) NOT NULL DEFAULT 1,
  `is_positive` boolean NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_category_parent_idx` (`id_parent`),
  KEY `fk_transaction_person_idx` (`id_person`),
  CONSTRAINT `fk_category_parent` FOREIGN KEY (`id_parent`) REFERENCES `balance`.`category` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_transaction_person` FOREIGN KEY (`id_person`) REFERENCES `balance`.`person` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$


delimiter $$

CREATE TABLE `balance`.`transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_category` bigint(20) NOT NULL,
  `dt_transaction` datetime NOT NULL,
  `vl_value` decimal(19,4) DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_transaction_category_idx` (`id_category`),
  CONSTRAINT `fk_transaction_category` FOREIGN KEY (`id_category`) REFERENCES `balance`.`category` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$



-- person
INSERT INTO `balance`.`person` (id,ds_name) VALUES (1,'Jonas');
INSERT INTO `balance`.`person` (id,ds_name) VALUES (2,'Marina');
-- category
INSERT INTO `balance`.`category` (id,ds_text,id_parent,id_person,is_positive) VALUES (1,'Income',null,1,true);
INSERT INTO `balance`.`category` (id,ds_text,id_parent,id_person,is_positive) VALUES (2,'Income',null,2,true);
-- INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (2,'Expense',null);
-- Income
INSERT INTO `balance`.`category` (id,ds_text,id_parent,is_positive) VALUES (3,'Salário',1,true);
INSERT INTO `balance`.`category` (id,ds_text,id_parent,is_positive) VALUES (4,'Adicional de Qualificação',1,true);
INSERT INTO `balance`.`category` (id,ds_text,id_parent,is_positive) VALUES (5,'Anuênios / Triênios',1,true);
INSERT INTO `balance`.`category` (id,ds_text,id_parent,is_positive) VALUES (6,'FCT',1,true);
INSERT INTO `balance`.`category` (id,ds_text,id_parent,is_positive) VALUES (7,'Férias',1,true);
INSERT INTO `balance`.`category` (id,ds_text,id_parent,is_positive) VALUES (8,'13º Salário',1,true);
INSERT INTO `balance`.`category` (id,ds_text,id_parent,is_positive) VALUES (9,'Receita extra',1,true);
INSERT INTO `balance`.`category` (id,ds_text,id_parent,is_positive) VALUES (10,'Outros',1,true);


INSERT INTO `balance`.`category` (ds_text,id_parent,is_positive) VALUES ('Salário',2,true);

-- Expense
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (11,'Alimentação',null);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (12,'Mercado',11);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (13,'Ticket Alimentação',11);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (14,'Outros',11);
-- HABITACAO
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (15,'Habitação',null);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (16,'Prestação Caixa',15);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (17,'Prestação Pai',15);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (18,'Condomínio',15);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (19,'NET',15);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (20,'Energia elétrica',15);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (21,'Gás',15);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (22,'Celular',15);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (23,'Diarista',15);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (24,'IPTU',15);
-- SAUDE
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (25,'Saúde',null);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (26,'CASSI / SERPRO',25);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (27,'Sulamerica Seguros',25);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (28,'Plano Odontológico',25);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (29,'Academia',25);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (30,'Outros',25);
-- TRANSPORTE
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (31,'Transporte',null);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (32,'Público',31);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (33,'Combustível',31);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (34,'Seguro',31);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (35,'IPVA',31);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (36,'Manutenção',31);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (37,'Licenciamento',31);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (38,'Rav4 EUA',31);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (39,'Vale transporte',31);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (40,'Outros',31);
-- PESSOAIS
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (41,'Pessoais',null);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (42,'Higiene Pessoal (cabelo, ...)',41);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (43,'Vestuário',41);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (44,'Presentes',41);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (45,'Doações',41);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (46,'Outros',41);
-- LAZER
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (47,'Lazer',null);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (48,'Cinema / Teatro / Shows',47);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (49,'Passagem aérea ',47);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (50,'Custos Extras de Viagem',47);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (51,'Hotel',47);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (52,'Aluguel de carro ',47);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (53,'Restaurantes / Festas',47);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (54,'Futebol',47);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (55,'Clube do Cocotá',47);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (56,'Outros',47);
-- CASAMENTO
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (57,'Casamento',null);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (58,'Casa de festas',57);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (59,'Foto / Filmagem',57);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (60,'Vestido de Noiva',57);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (61,'Bem casado',57);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (62,'Buquê',57);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (63,'Hotel',57);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (64,'Padre',57);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (65,'Certidão de Habilitação',57);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (66,'Caio Lima',57);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (67,'Outros',57);
-- ENCARGOS
INSERT INTO `balance`.`category` (id,ds_text,id_person,id_parent) VALUES (68,'Encargos Financeiros',1,null);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (69,'Previdência Social',68);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (70,'Previdência Privada',68);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (71,'Imposto de Renda',68);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (72,'Adiantamento a Pessoal',68);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (73,'Adiantamento do 13º',68);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (74,'Contribuição Sindical',68);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (75,'Transferências para EUA',68);
INSERT INTO `balance`.`category` (id,ds_text,id_parent) VALUES (76,'Tarifas Bancárias',68);

-- Cont. Income
INSERT INTO `balance`.`category` (ds_text,id_parent,is_positive) VALUES ('Abono Pecuniário',1,true);
INSERT INTO `balance`.`category` (ds_text,id_parent,is_positive) VALUES ('Adiantamento a Pessoal',1,true);

-- transaction
INSERT INTO `balance`.`transaction` (id_category,vl_value,dt_transaction) VALUES (3,5934.59,'2014-05-01');
INSERT INTO `balance`.`transaction` (id_category,vl_value,dt_transaction) VALUES (4,890.18,'2014-05-01');
INSERT INTO `balance`.`transaction` (id_category,vl_value,dt_transaction) VALUES (5,474.76,'2014-05-01');
INSERT INTO `balance`.`transaction` (id_category,vl_value,dt_transaction) VALUES (6,1553.91,'2014-05-01');
INSERT INTO `balance`.`transaction` (id_category,vl_value,dt_transaction) VALUES (69,482.92,'2014-05-01');
INSERT INTO `balance`.`transaction` (id_category,vl_value,dt_transaction) VALUES (71,1475.74,'2014-05-01');
INSERT INTO `balance`.`transaction` (id_category,vl_value,dt_transaction) VALUES (3,4737.60,'2014-05-01');
INSERT INTO `balance`.`transaction` (id_category,vl_value,dt_transaction) VALUES (9,1140.71,'2014-05-01');
INSERT INTO `balance`.`transaction` (id_category,vl_value,dt_transaction) VALUES (69,482.93,'2014-05-01');
INSERT INTO `balance`.`transaction` (id_category,vl_value,dt_transaction) VALUES (71,657.57,'2014-05-01');
INSERT INTO `balance`.`transaction` (id_category,vl_value,dt_transaction) VALUES (3,5934.59,'2014-06-01');

UPDATE `balance`.`category` SET `nr_order`='1' WHERE `id`='1';
UPDATE `balance`.`category` SET `nr_order`='2' WHERE `id`='2';
UPDATE `balance`.`category` SET `nr_order`='1' WHERE `id`='3';
UPDATE `balance`.`category` SET `nr_order`='2' WHERE `id`='4';
UPDATE `balance`.`category` SET `nr_order`='3' WHERE `id`='5';
UPDATE `balance`.`category` SET `nr_order`='4' WHERE `id`='6';
UPDATE `balance`.`category` SET `nr_order`='5' WHERE `id`='7';
UPDATE `balance`.`category` SET `nr_order`='6' WHERE `id`='8';
UPDATE `balance`.`category` SET `nr_order`='7' WHERE `id`='9';
UPDATE `balance`.`category` SET `nr_order`='8' WHERE `id`='10';
UPDATE `balance`.`category` SET `nr_order`='2' WHERE `id`='11';
UPDATE `balance`.`category` SET `nr_order`='1' WHERE `id`='12';
UPDATE `balance`.`category` SET `nr_order`='2' WHERE `id`='13';
UPDATE `balance`.`category` SET `nr_order`='3' WHERE `id`='14';
UPDATE `balance`.`category` SET `nr_order`='3' WHERE `id`='15';
UPDATE `balance`.`category` SET `nr_order`='1' WHERE `id`='16';
UPDATE `balance`.`category` SET `nr_order`='2' WHERE `id`='17';
UPDATE `balance`.`category` SET `nr_order`='3' WHERE `id`='18';
UPDATE `balance`.`category` SET `nr_order`='4' WHERE `id`='19';
UPDATE `balance`.`category` SET `nr_order`='5' WHERE `id`='20';
UPDATE `balance`.`category` SET `nr_order`='6' WHERE `id`='21';
UPDATE `balance`.`category` SET `nr_order`='7' WHERE `id`='22';
UPDATE `balance`.`category` SET `nr_order`='8' WHERE `id`='23';
UPDATE `balance`.`category` SET `nr_order`='9' WHERE `id`='24';
UPDATE `balance`.`category` SET `nr_order`='4' WHERE `id`='25';
UPDATE `balance`.`category` SET `nr_order`='1' WHERE `id`='26';
UPDATE `balance`.`category` SET `nr_order`='2' WHERE `id`='27';
UPDATE `balance`.`category` SET `nr_order`='3' WHERE `id`='28';
UPDATE `balance`.`category` SET `nr_order`='4' WHERE `id`='29';
UPDATE `balance`.`category` SET `nr_order`='5' WHERE `id`='30';
UPDATE `balance`.`category` SET `nr_order`='5' WHERE `id`='31';
UPDATE `balance`.`category` SET `nr_order`='1' WHERE `id`='32';
UPDATE `balance`.`category` SET `nr_order`='2' WHERE `id`='33';
UPDATE `balance`.`category` SET `nr_order`='3' WHERE `id`='34';
UPDATE `balance`.`category` SET `nr_order`='4' WHERE `id`='35';
UPDATE `balance`.`category` SET `nr_order`='5' WHERE `id`='36';
UPDATE `balance`.`category` SET `nr_order`='6' WHERE `id`='37';
UPDATE `balance`.`category` SET `nr_order`='7' WHERE `id`='38';
UPDATE `balance`.`category` SET `nr_order`='8' WHERE `id`='39';
UPDATE `balance`.`category` SET `nr_order`='9' WHERE `id`='40';
UPDATE `balance`.`category` SET `nr_order`='6' WHERE `id`='41';
UPDATE `balance`.`category` SET `nr_order`='1' WHERE `id`='42';
UPDATE `balance`.`category` SET `nr_order`='2' WHERE `id`='43';
UPDATE `balance`.`category` SET `nr_order`='3' WHERE `id`='44';
UPDATE `balance`.`category` SET `nr_order`='4' WHERE `id`='45';
UPDATE `balance`.`category` SET `nr_order`='5' WHERE `id`='46';
UPDATE `balance`.`category` SET `nr_order`='7' WHERE `id`='47';
UPDATE `balance`.`category` SET `nr_order`='1' WHERE `id`='48';
UPDATE `balance`.`category` SET `nr_order`='2' WHERE `id`='49';
UPDATE `balance`.`category` SET `nr_order`='3' WHERE `id`='50';
UPDATE `balance`.`category` SET `nr_order`='4' WHERE `id`='51';
UPDATE `balance`.`category` SET `nr_order`='5' WHERE `id`='52';
UPDATE `balance`.`category` SET `nr_order`='6' WHERE `id`='53';
UPDATE `balance`.`category` SET `nr_order`='7' WHERE `id`='54';
UPDATE `balance`.`category` SET `nr_order`='8' WHERE `id`='55';
UPDATE `balance`.`category` SET `nr_order`='9' WHERE `id`='56';
UPDATE `balance`.`category` SET `nr_order`='8' WHERE `id`='57';
UPDATE `balance`.`category` SET `nr_order`='1' WHERE `id`='58';
UPDATE `balance`.`category` SET `nr_order`='2' WHERE `id`='59';
UPDATE `balance`.`category` SET `nr_order`='3' WHERE `id`='60';
UPDATE `balance`.`category` SET `nr_order`='4' WHERE `id`='61';
UPDATE `balance`.`category` SET `nr_order`='5' WHERE `id`='62';
UPDATE `balance`.`category` SET `nr_order`='6' WHERE `id`='63';
UPDATE `balance`.`category` SET `nr_order`='7' WHERE `id`='64';
UPDATE `balance`.`category` SET `nr_order`='8' WHERE `id`='65';
UPDATE `balance`.`category` SET `nr_order`='9' WHERE `id`='66';
UPDATE `balance`.`category` SET `nr_order`='10' WHERE `id`='67';
UPDATE `balance`.`category` SET `nr_order`='9' WHERE `id`='68';
UPDATE `balance`.`category` SET `nr_order`='1' WHERE `id`='69';
UPDATE `balance`.`category` SET `nr_order`='2' WHERE `id`='70';
UPDATE `balance`.`category` SET `nr_order`='3' WHERE `id`='71';
UPDATE `balance`.`category` SET `nr_order`='4' WHERE `id`='72';
UPDATE `balance`.`category` SET `nr_order`='5' WHERE `id`='73';
UPDATE `balance`.`category` SET `nr_order`='6' WHERE `id`='74';
UPDATE `balance`.`category` SET `nr_order`='7' WHERE `id`='75';
UPDATE `balance`.`category` SET `nr_order`='8' WHERE `id`='76';

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

