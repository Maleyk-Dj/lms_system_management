INSERT INTO groups (id, name, deleted)
VALUES (1, 'Gruppa A', false);
INSERT INTO groups(id, name, deleted)
VALUES (2, 'Gruppa B', false);
INSERT INTO students(id, first_name, last_name, group_id, deleted)
VALUES (1, 'Valya', 'Ivanova', 1, false);

SELECT setval('groups_id_seq', 2);
SELECT setval('students_id_seq', 1);
