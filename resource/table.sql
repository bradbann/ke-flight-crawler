CREATE DATABASE `flight_crawler` /*!CREATE TABLE `Flight` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `AirLineName` varchar(200) NOT NULL,
  `AirLineID` int(11) DEFAULT NULL,
  `DeptAirportName` varchar(200) NOT NULL,
  `DeptAirportID` int(11) DEFAULT NULL,
  `DeptTime` time NOT NULL,
  `DeptCityName` varchar(200) DEFAULT NULL,
  `DeptCityID` int(11) DEFAULT NULL,
  `ArrAirportName` varchar(200) NOT NULL,
  `ArrAirportID` int(11) DEFAULT NULL,
  `ArrTime` time NOT NULL,
  `ArrCityID` int(11) DEFAULT NULL,
  `ArrCityName` varchar(200) NOT NULL,
  `FlightInterval` time DEFAULT NULL,
  `FlightNo` varchar(200) NOT NULL,
  `PlanModel` varchar(200) DEFAULT NULL,
  `CreateDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ExpiredDate` date NOT NULL,
  `Flag` int(11) NOT NULL DEFAULT '1' COMMENT '1，有效数据 -1无效数据',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
40100 DEFAULT CHARACTER SET utf8 */;
 
