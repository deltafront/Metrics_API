DROP TABLE IF EXISTS Metric;
CREATE TABLE Metric(
  ID INT AUTO_INCREMENT,
  NAME VARCHAR(100) NOT NULL,
  CONTACT_NAME VARCHAR(100) NOT NULL,
  CONTACT_EMAIL VARCHAR(100) NOT NULL,
  TIMEZONE VARCHAR(50)NOT NULL DEFAULT 'UTC',
  GUID VARCHAR(250)NOT NULL
);
ALTER TABLE Metric ADD CONSTRAINT uniqueName UNIQUE (NAME,CONTACT_EMAIL)
