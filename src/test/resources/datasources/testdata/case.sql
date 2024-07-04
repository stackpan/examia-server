TRUNCATE TABLE cases RESTART IDENTITY CASCADE;

INSERT INTO cases (id, title, description, duration_in_seconds, user_id, created_at, updated_at)
VALUES ('2eef6095-06af-4c07-b989-795d64c86625', 'Case Title 1',
        'Case 1 Description.', 1800, '157e4056-3a6e-4410-bc15-f14ea86887b6', '2024-05-16 00:00:01.000000 +00:00',
        '2024-05-16 00:00:01.000000 +00:00'),
       ('b9a5ebc1-b77f-495d-9f28-5a181bf543bf', 'Case Title 2',
        'Case 2 Description.', 1900, '157e4056-3a6e-4410-bc15-f14ea86887b6', '2024-05-16 00:00:02.000000 +00:00',
        '2024-05-16 00:00:02.000000 +00:00'),
       ('f22a0d17-f8ba-44fb-b003-444124f6d1d3', 'Case Title 3',
        'Case 3 Description.', 2000, '157e4056-3a6e-4410-bc15-f14ea86887b6', '2024-05-16 00:00:03.000000 +00:00',
        '2024-05-16 00:00:03.000000 +00:00'),
       ('527fa3f0-449e-46f5-ac2a-ffd39e7e539f', 'Case Title 4',
        'Case 4 Description.', 2100, '08b7b440-e8f6-4397-b4ad-1f9b105f7c89', '2024-05-16 00:00:04.000000 +00:00',
        '2024-05-16 00:00:04.000000 +00:00');