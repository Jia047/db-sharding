# master_db 写入数据源
create database master_db;
use master_db;
create table user(id int(11) primary key auto_increment, name char(20) not null);

# slave_db 读取数据源
create database slave_db;
use slave_db;
create table user(id int(11) primary key auto_increment, name char(20) not null);
