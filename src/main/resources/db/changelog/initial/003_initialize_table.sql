INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type)
VALUES ('Иван', 'Петров', 30, 'ivan.petrov@example.com', '$2a$10$xtbfLFAiI7MuP3W8zuPgvOb7mCN40OeZ0nDvVCe2zQiVYwi3QUWNu', '+77001112233', NULL, 'applicant');

INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type)
VALUES ('Мария', 'Сидорова', 40, 'maria.sidorova@example.com', '$2a$10$xtbfLFAiI7MuP3W8zuPgvOb7mCN40OeZ0nDvVCe2zQiVYwi3QUWNu', '+77009998877', NULL, 'employer');

INSERT INTO categories (name, parent_id)
VALUES ('Java Developer', NULL);

INSERT INTO categories (name, parent_id)
VALUES ('Frontend Developer', NULL);

INSERT INTO resumes (applicant_id, name, category_id, salary, is_active)
VALUES (1, 'Java Developer', (SELECT id FROM categories WHERE name = 'Java Developer'), 250000, TRUE);

INSERT INTO resumes (applicant_id, name, category_id, salary, is_active)
VALUES (1, 'Frontend Developer', (SELECT id FROM categories WHERE name = 'Frontend Developer'), 220000, TRUE);

INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id)
VALUES ('Backend Developer', 'Ищем Java-разработчика с опытом 3+ лет', (SELECT id FROM categories WHERE name = 'Java Developer'), 300000, 3, 5, TRUE, 2);

INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id)
VALUES ('Frontend Developer', 'Ищем React-разработчика', (SELECT id FROM categories WHERE name = 'Frontend Developer'), 280000, 2, 4, TRUE, 2);

INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation)
VALUES (1, 1, TRUE);

INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation)
VALUES (2, 2, FALSE);

INSERT INTO contact_types (type)
VALUES ('Телефон');

INSERT INTO contact_types (type)
VALUES ('Email');

INSERT INTO work_experience (resume_id, years, company_name, position, responsibilities)
VALUES (1, 5, 'Tech Corp', 'Senior Java Developer', 'Разработка и поддержка серверной части приложения');

INSERT INTO education_info (resume_id, institution, program, start_date, end_date, degree)
VALUES (1, 'КТМУ Манас', 'Программная инженерия', '2010-09-01', '2014-06-30', 'Бакалавр');

insert into CONTACT_INFO (TYPE_ID, RESUME_ID, "VALUE")
values (1, 1, '+77001112233');

insert into CONTACT_INFO (TYPE_ID, RESUME_ID, "VALUE")
values (2, 1, 'ivan.petrov@example.com');