SELECT
person.id,
CONCAT('\'', person.ds_name,'\'') as ds_name
FROM balance.person
INTO OUTFILE '/tmp/balance_person.csv'
FIELDS TERMINATED BY ';';
-- ENCLOSED BY '"' ESCAPED BY '"'
-- LINES TERMINATED BY '\r\n';

SELECT
category.id,
category.nr_order,
IF(category.is_positive,true,false),
CONCAT('\'', category.ds_text,'\'') as ds_text,
IFNULL(category.id_parent, 'null') AS id_parent,
IFNULL(category.id_person, 'null') AS id_person
FROM balance.category
INTO OUTFILE '/tmp/balance_category.csv'
FIELDS TERMINATED BY ';' ;
-- ENCLOSED BY '"' ESCAPED BY '"'
-- LINES TERMINATED BY '\n';

SELECT
transaction.id,
transaction.dt_transaction,
transaction.vl_value,
transaction.id_category
FROM balance.transaction INTO OUTFILE '/tmp/balance_transaction.csv' FIELDS TERMINATED BY ';';