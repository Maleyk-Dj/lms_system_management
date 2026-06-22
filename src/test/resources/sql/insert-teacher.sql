INSERT INTO teachers (id, first_name, last_name, deleted)
VALUES (1, 'Malika', 'Djabrailova', false);

SELECT setval('teachers_id_seq', 1);
