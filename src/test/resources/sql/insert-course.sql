INSERT INTO teachers (id, first_name, last_name, deleted)
VALUES (1, 'Li', 'Dja', false);

INSERT INTO courses (id, name, description, teacher_id, deleted)
VALUES (1, 'Java', 'Kurs po razrabotke Java', 1, false);

SELECT setval('teachers_id_seq', 1);
SELECT setval('courses_id_seq', 1);
