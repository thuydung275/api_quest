USE `quest_spring_boot`;
ALTER TABLE `adresse` auto_increment = 1;
ALTER TABLE `user` auto_increment = 1;

LOCK TABLES `adresse` WRITE;
LOCK TABLES `user` WRITE;
UNLOCK TABLES;
