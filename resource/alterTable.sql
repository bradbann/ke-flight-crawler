ALTER TABLE `flight_crawler_dev`.`FlightInfo` 
ADD COLUMN `PunctualityRate` FLOAT NULL COMMENT '准点率' AFTER `AheadDay`,
ADD COLUMN `IsFood` BIT NULL AFTER `PunctualityRate`,
ADD COLUMN `AdditionalFee` DECIMAL(10,0) NULL AFTER `IsFood`;