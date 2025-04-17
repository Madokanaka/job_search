INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type, enabled, role_id)
VALUES ('Иван', 'Петров', 30, 'ivan.petrov@example.com', '$2y$10$KG8TeD.E40KHTUta6RBrbeBHrJCVPThptd99RSFOUt7EsbuimcPle', '+77001112233', NULL, 'applicant', true, (select id from roles where role = 'APPLICANT'));

INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type, enabled, role_id)
VALUES ('Мария', 'Сидорова', 40, 'maria.sidorova@example.com', '$2y$10$KG8TeD.E40KHTUta6RBrbeBHrJCVPThptd99RSFOUt7EsbuimcPle', '+77009998877', NULL, 'employer', true, select id from roles where role = 'EMPLOYER');

INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type, enabled, role_id)
VALUES ('Админ', 'Админ', 40, 'admin@example.com', '$2y$10$KG8TeD.E40KHTUta6RBrbeBHrJCVPThptd99RSFOUt7EsbuimcPle', '+77009998877', NULL, 'admin', true, select id from roles where role = 'ADMIN');


INSERT INTO categories (name, parent_id)
VALUES ('Java Developer', NULL);

INSERT INTO categories (name, parent_id)
VALUES ('Frontend Developer', NULL);

INSERT INTO resumes (applicant_id, name, category_id, salary, is_active)
VALUES ((select id from users where email = 'ivan.petrov@example.com'), 'Java Developer', (SELECT id FROM categories WHERE name = 'Java Developer'), 250000, TRUE);

INSERT INTO resumes (applicant_id, name, category_id, salary, is_active)
VALUES ((select id from users where email = 'ivan.petrov@example.com'), 'Frontend Developer', (SELECT id FROM categories WHERE name = 'Frontend Developer'), 220000, TRUE);

INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id)
VALUES ('Backend Developer', 'Ищем Java-разработчика с опытом 3+ лет', (SELECT id FROM categories WHERE name = 'Java Developer'), 300000, 3, 5, TRUE, (select id from users where email = 'maria.sidorova@example.com'));

INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id)
VALUES ('Frontend Developer', 'Ищем React-разработчика', (SELECT id FROM categories WHERE name = 'Frontend Developer'), 280000, 2, 4, TRUE, (select id from users where email = 'maria.sidorova@example.com'));

INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation)
VALUES ( (select id from resumes where name = 'Java Developer'), (select id from vacancies where name = 'Backend Developer'), TRUE);

INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation)
VALUES ( (select id from resumes where name = 'Frontend Developer'), (select id from vacancies where name = 'Frontend Developer'), FALSE);

INSERT INTO contact_types (type)
VALUES ('Телефон');

INSERT INTO contact_types (type)
VALUES ('Email');

INSERT INTO work_experience (resume_id, years, company_name, position, responsibilities)
VALUES ( (select id from resumes where name = 'Java Developer'), 5, 'Tech Corp', 'Senior Java Developer', 'Разработка и поддержка серверной части приложения');

INSERT INTO education_info (resume_id, institution, program, start_date, end_date, degree)
VALUES ( (select id from resumes where name = 'Java Developer'), 'КТМУ Манас', 'Программная инженерия', '2010-09-01', '2014-06-30', 'Бакалавр');

insert into CONTACT_INFO (TYPE_ID, RESUME_ID, "VALUE")
values ((select id from CONTACT_TYPES where type = 'Телефон'),  (select id from resumes where name = 'Java Developer'), '+77001112233');

insert into CONTACT_INFO (TYPE_ID, RESUME_ID, "VALUE")
values (( select id from CONTACT_TYPES where type = 'Email'), (select id from resumes where name = 'Java Developer'),'ivan.petrov@example.com');