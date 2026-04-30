-- Sample data for classeses
INSERT INTO classes (name) VALUES ('LKG');
INSERT INTO classes (name) VALUES ('UKG');
INSERT INTO classes (name) VALUES ('1st');
INSERT INTO classes (name) VALUES ('2nd');
INSERT INTO classes (name) VALUES ('3rd');
INSERT INTO classes (name) VALUES ('4th');
INSERT INTO classes (name) VALUES ('5th');
INSERT INTO classes (name) VALUES ('6th');
INSERT INTO classes (name) VALUES ('7th');
INSERT INTO classes (name) VALUES ('8th');
INSERT INTO classes (name) VALUES ('9th');
INSERT INTO classes (name) VALUES ('10th');
INSERT INTO classes (name) VALUES ('11th');
INSERT INTO classes (name) VALUES ('12th');

-- Sample data for schools
INSERT INTO school (name, address, state, district, pin,phone,logo_url)
 VALUES ('Army_Public_School', '123 Main Street, City Center',
'Maharashtra', 'Mumbai', '400001','9876543210','/images/Army_Public_School.png');

INSERT INTO school (name, address, state, district, pin,phone,logo_url)
 VALUES ('Kendriya Vidyalaya', '456 Elm Avenue, Suburb Area', 'Karnataka',
 'Bangalore', '560001','9876543211','/images/Kendriya_Vidyalaya.png');

INSERT INTO public.users(name, email, username, password, role, school_id)
VALUES ('admin1', 'admin1@gmail.com', 'admin1', 'admin1', 'admin', 'sch-1');

INSERT INTO public.users(name, email, username, password, role, school_id)
VALUES ('admin2', 'admin2@gmail.com', 'admin2', 'admin2', 'admin', 'sch-2');