TRUNCATE TABLE users RESTART IDENTITY CASCADE;

INSERT INTO users (id, username, email, first_name, last_name, role, password, created_at, updated_at)
VALUES ('157e4056-3a6e-4410-bc15-f14ea86887b6', 'user', 'user@example.com', 'First', 'Last', 'USER', '$2a$10$urevrQGtcI80dEZyI9yBKecGMuNmXNH.pCfjLWPfq1HjBMU0awMk.',
        '2024-06-05T21:00:00.00000+07:00', '2024-05-16 00:00:00.000000 +00:00');