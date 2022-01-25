CREATE TABLE STUDENT (
  stud_Id          INTEGER PRIMARY KEY,
  first_Name       VARCHAR(50) NOT NULL,
  last_Name        VARCHAR(50) NOT NULL,
  year            INTEGER NOT NULL,
  dob             VARCHAR(10) NOT NULL,
  doj             VARCHAR(10) NOT NULL
);

create sequence hibernate_sequence;

create sequence stud_seq start with 10 increment by 50;