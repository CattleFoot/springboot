DROP  TABLE IF EXISTS  system;
CREATE TABLE system (
                      id int(11) unsigned NOT NULL AUTO_INCREMENT,
                      name varchar(1024) DEFAULT NULL,
                      lastaudit varchar(1024) DEFAULT NULL,
                      PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;