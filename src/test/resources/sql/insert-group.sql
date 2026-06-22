INSERT INTO groups (id, name, deleted)
VALUES (1, 'Gruppa A', false);

SELECT setval('groups_id_seq', 1);
