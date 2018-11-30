DROP  TABLE IF EXISTS  system;
create table system(
id int not null auto_increment,
name  varchar(100) not null ,
lastaudit date not null,
primary key  (id),
)