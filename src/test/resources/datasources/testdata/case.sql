TRUNCATE TABLE cases RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

INSERT INTO users (id, username, email, first_name, last_name, password)
VALUES ('1f0c5c6a-adcb-462e-83f4-3eda6f3ac60a', 'ckensington0', 'ckensington0@nps.gov', 'Courtnay', 'Kensington',
        'password');
INSERT INTO users (id, username, email, first_name, last_name, password)
VALUES ('f04169fb-9b2f-449c-81fd-00158cfb2e18', 'ispellar1', 'ispellar1@paginegialle.it', 'Inglis', 'Spellar',
        'password');
INSERT INTO users (id, username, email, first_name, last_name, password)
VALUES ('9585385e-8718-4be4-902e-42c1bd669159', 'ccessford2', 'ccessford2@wikimedia.org', 'Cleve', 'Cessford',
        'password');

INSERT INTO cases (id, user_id, title, description, duration_in_seconds)
VALUES ('03dbe568-28df-4ff3-a084-95f5924e4b43', '1f0c5c6a-adcb-462e-83f4-3eda6f3ac60a', 'tincidunt eu felis fusce',
        'Cras pellentesque volutpat dui.', 1320);
INSERT INTO cases (id, user_id, title, description, duration_in_seconds)
VALUES ('f04c9c81-67a9-42ba-924c-52b46c33f955', '1f0c5c6a-adcb-462e-83f4-3eda6f3ac60a', 'orci luctus et',
        'In tempor, turpis nec euismod scelerisque, quam turpis adipiscing lorem, vitae mattis nibh ligula nec sem. Duis aliquam convallis nunc.',
        859);
INSERT INTO cases (id, user_id, title, description, duration_in_seconds)
VALUES ('7d5f5c72-10d8-41dd-a506-08faf9d3dd4b', '1f0c5c6a-adcb-462e-83f4-3eda6f3ac60a', 'tortor duis',
        'Nunc purus. Phasellus in felis. Donec semper sapien a libero.', 193);
INSERT INTO cases (id, user_id, title, description, duration_in_seconds)
VALUES ('dccca039-ee3e-49d7-8ac7-6d22f7e81cd0', '1f0c5c6a-adcb-462e-83f4-3eda6f3ac60a', 'molestie sed justo',
        'Aenean lectus. Pellentesque eget nunc.', 356);
INSERT INTO cases (id, user_id, title, description, duration_in_seconds)
VALUES ('5e314413-9ccc-4d74-b7fa-2e170ab2a310', '1f0c5c6a-adcb-462e-83f4-3eda6f3ac60a', 'cras mi pede',
        'In tempor, turpis nec euismod scelerisque, quam turpis adipiscing lorem, vitae mattis nibh ligula nec sem.',
        2633);