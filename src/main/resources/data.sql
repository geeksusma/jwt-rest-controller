DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(25) NOT NULL,
  `email` varchar(100) NOT NULL,
  `pwd` BINARY NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
);

INSERT INTO `user` (`user_id`, `nickname`, `email`, `pwd`) values (1, 'geeksusma', 'test@geeksusma.es', 'A348803E8F49F5A7C0ACB8C568865A8A');