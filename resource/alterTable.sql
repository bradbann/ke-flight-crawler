ALTER TABLE `flight_crawler_dev`.`FlightInfo` 
ADD COLUMN `PunctualityRate` FLOAT NULL COMMENT '准点率' AFTER `AheadDay`,
ADD COLUMN `IsFood` BIT NULL AFTER `PunctualityRate`,
ADD COLUMN `AdditionalFee` DECIMAL(10,0) NULL AFTER `IsFood`;


ALTER TABLE `flight_crawler_dev`.`FlightSchedule` 
CHANGE COLUMN `CreateDate` `CreateDate` DATETIME NOT NULL ,
CHANGE COLUMN `Flag` `Flag` INT(11) NOT NULL DEFAULT '1' COMMENT '1，有效数据 -1过期修改，-2过期删除' ,
ADD COLUMN `RequestParam` VARCHAR(1000) NULL COMMENT '请求的信息，包括url，formdata，cookie' AFTER `WeekSchedule`,
ADD COLUMN `LastUpdate` TIMESTAMP NOT NULL AFTER `RequestParam`;
