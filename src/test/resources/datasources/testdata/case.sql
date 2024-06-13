TRUNCATE TABLE cases RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

INSERT INTO users (id, username, email, first_name, last_name, password, created_at, updated_at)
VALUES ('157e4056-3a6e-4410-bc15-f14ea86887b6', 'user', 'user@example.com', 'First', 'Last', 'password',
        '2024-06-05T21:00:00.00000+07:00', '2024-05-16 00:00:00.000000 +00:00');

INSERT INTO cases (id, user_id, title, description, duration_in_seconds, created_at, updated_at)
VALUES ('2eef6095-06af-4c07-b989-795d64c86625', '157e4056-3a6e-4410-bc15-f14ea86887b6', 'Case Title 1',
        'Case 1 Description.', 1800, '2024-05-16 00:00:01.000000 +00:00', '2024-05-16 00:00:01.000000 +00:00'),
       ('b9a5ebc1-b77f-495d-9f28-5a181bf543bf', '157e4056-3a6e-4410-bc15-f14ea86887b6', 'Case Title 2',
        'Case 2 Description.', 1900, '2024-05-16 00:00:02.000000 +00:00', '2024-05-16 00:00:02.000000 +00:00'),
       ('f22a0d17-f8ba-44fb-b003-444124f6d1d3', '157e4056-3a6e-4410-bc15-f14ea86887b6', 'Case Title 3',
        'Case 3 Description.', 2000, '2024-05-16 00:00:03.000000 +00:00', '2024-05-16 00:00:03.000000 +00:00');