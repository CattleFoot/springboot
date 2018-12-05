DROP TABLE IF EXISTS system_bean;
create table system_bean
(
  id        int          not null auto_increment,
  name      varchar(100) not null,
  lastaudit date         not null,
  primary key (id),
)