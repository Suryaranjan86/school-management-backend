-- Sample data for classeses
INSERT INTO classes (name) VALUES ('LKG');
INSERT INTO classes (name) VALUES ('UKG');
INSERT INTO classes (name) VALUES ('1ST');
INSERT INTO classes (name) VALUES ('2ND');
INSERT INTO classes (name) VALUES ('3RD');
INSERT INTO classes (name) VALUES ('4TH');
INSERT INTO classes (name) VALUES ('5TH');
INSERT INTO classes (name) VALUES ('6TH');
INSERT INTO classes (name) VALUES ('7TH');
INSERT INTO classes (name) VALUES ('8TH');
INSERT INTO classes (name) VALUES ('9TH');
INSERT INTO classes (name) VALUES ('10TH');
INSERT INTO classes (name) VALUES ('11TH');
INSERT INTO classes (name) VALUES ('12TH');

-- Sample data for schools
INSERT INTO school (name, address, state, district, pin, phone, payment_mode, session_start_month,session_end_month, logo_url)
 VALUES ('Army_Public_School', '123 Main Street, City Center',
 'Maharashtra', 'Mumbai', '400001','9876543210', 'MONTHLY', 'APR','MAR', '/images/Army_Public_School.png');

INSERT INTO school (name, address, state, district, pin, phone, payment_mode, session_start_month,session_end_month, logo_url)
 VALUES ('Kendriya Vidyalaya', '456 Elm Avenue, Suburb Area', 'Karnataka',
 'Bangalore', '560001','9876543211', 'YEARLY', 'APR','MAR' ,'/images/Kendriya_Vidyalaya.png');

INSERT INTO public.users(name, email, username, password, role, school_id)
VALUES ('admin1', 'admin1@gmail.com', 'admin', 'password123', 'admin', 'sch-1');

INSERT INTO public.users(name, email, username, password, role, school_id)
VALUES ('admin2', 'admin2@gmail.com', 'admin2', 'admin2', 'admin', 'sch-2');