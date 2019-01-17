create schema if not exists split_me;
use split_me;

create table split(
split_id varchar(50) NOT NULL,
currency varchar(4),
primary key (split_id)
);

create table transaction(
transaction_id varchar(50) NOT NULL primary key,
public_id varchar(50) NOT NULL,
transaction_date date, 
description varchar(150),
transaction_type varchar(10),
split_id varchar(50),
added_time timestamp DEFAULT CURRENT_TIMESTAMP,
added_by varchar(50),
last_modified_time timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
last_modified_by varchar(50),
foreign key fk_split(split_id)
references split(split_id)
on update cascade
on delete cascade);

create table split_detail(
split_detail_id varchar(50) NOT NULL primary key,
user_id varchar(50) NOT NULL,
split_id varchar(50) NOT NULL,
exchanged_amount decimal(20,10),
user_share decimal(20,10),
foreign key fk_split(split_id)
references split(split_id)
on update cascade
on delete cascade
);

create table due(
due_id varchar(50) NOT NULL primary key,
public_id varchar(50) NOT NULL, 
debtor_id varchar(50) NOT NULL,
creditor_id varchar(50) NOT NULL,
amount decimal(20,10),
currency_code varchar(4),
settled boolean,
transaction_id varchar(50) NOT NULL ,
foreign key fk_transaction(transaction_id)
references transaction(transaction_id)
on update cascade
on delete cascade
);