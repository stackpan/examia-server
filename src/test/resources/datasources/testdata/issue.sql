TRUNCATE TABLE issues RESTART IDENTITY CASCADE;

INSERT INTO issues (id, case_id, type, body, created_at, updated_at)
VALUES ('e7576fb6-c4f3-4b73-b9d9-0e1e67ac91d5', '2eef6095-06af-4c07-b989-795d64c86625', 'SINGLE_CHOICE',
        'porttitor lorem id ligula suspendisse ornare', '2024-07-05 00:00:01.000000 +00:00',
        '2024-07-05 00:00:01.000000 +00:00'),
       ('8a12c2ed-747e-489c-ad01-32ca95932bc7', '2eef6095-06af-4c07-b989-795d64c86625', 'MULTIPLE_CHOICE',
        'massa id nisl', '2024-07-05 00:00:02.000000 +00:00', '2024-07-05 00:00:02.000000 +00:00'),
       ('f1fbc220-585b-422a-91bd-141378f07e38', '2eef6095-06af-4c07-b989-795d64c86625', 'SINGLE_CHOICE',
        'molestie lorem quisque ut erat', '2024-07-05 00:00:03.000000 +00:00', '2024-07-05 00:00:03.000000 +00:00'),
       ('d6c39843-ebd7-4eec-9858-18257975e859', 'b9a5ebc1-b77f-495d-9f28-5a181bf543bf', 'SINGLE_CHOICE',
        'auctor gravida sem praesent id', '2024-07-05 00:00:04.000000 +00:00', '2024-07-05 00:00:04.000000 +00:00'),
       ('f752cabb-ca90-400b-8ed6-5a903c92abf6', 'f22a0d17-f8ba-44fb-b003-444124f6d1d3', 'SINGLE_CHOICE',
        'justo morbi ut odio', '2024-07-05 00:00:05.000000 +00:00', '2024-07-05 00:00:05.000000 +00:00'),
       ('c83e7a4d-d040-4f73-9300-6269b425a098', '527fa3f0-449e-46f5-ac2a-ffd39e7e539f', 'MULTIPLE_CHOICE',
        'lacus curabitur at ipsum', '2024-07-05 00:00:06.000000 +00:00', '2024-07-05 00:00:06.000000 +00:00');
