create database AtmSystem;
use AtmSystem;
create table accounts(
    ID                 int primary key auto_increment,
    cardID             varchar(20),
    userName           varchar(20),
    cardPassWord       varchar(20),
    getMoneyLimitation double,
    lastMoney          double,
    insert_time        timestamp not null default CURRENT_TIMESTAMP
);