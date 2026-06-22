INSERT INTO teachers (id, first_name, last_name, deleted)
VALUES (1, 'Malika', 'Djabrailova', false);

INSERT INTO groups (id, name, deleted)
VALUES (1, 'Gruppa A', false);

INSERT INTO courses (id, name, description, teacher_id, deleted)
VALUES (1, 'Java', 'Kurs po Java', 1, false);

SELECT setval('teachers_id_seq', 1);
SELECT setval('groups_id_seq', 1);
SELECT setval('courses_id_seq', 1);
