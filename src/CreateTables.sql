CREATE TABLE Facilities (
	FacilityID int IDENTITY(1,1) PRIMARY KEY,
	Name varchar(255),
	Type varchar(255),
	Address varchar (255),
	Risk tinyint
);

CREATE TABLE Inspections (
	InspectionID int PRIMARY KEY,
	FacilityID int FOREIGN KEY REFERENCES Facilities(FacilityID),
	Date varchar(255),
	Result varchar(255),
	Type varchar(255),
	Violations varchar(1024)
);