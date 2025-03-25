INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type)
SELECT 'Иван', 'Петров', 30, 'ivan.petrov@example.com', 'password123', '+77001112233', NULL, 'applicant'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'ivan.petrov@example.com');

INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type)
SELECT 'Мария', 'Сидорова', 40, 'maria.sidorova@example.com', 'password123', '+77009998877', NULL, 'employer'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'maria.sidorova@example.com');

INSERT INTO categories (name)
SELECT 'Java Developer'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Java Developer');

INSERT INTO categories (name)
SELECT 'Frontend Developer'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Frontend Developer');

INSERT INTO resumes (applicant_id, name, category_id, salary, is_active)
SELECT 1, 'Java Developer', 1, 250000, TRUE
WHERE NOT EXISTS (SELECT 1 FROM resumes WHERE name = 'Java Developer' AND applicant_id = 1);

INSERT INTO resumes (applicant_id, name, category_id, salary, is_active)
SELECT 1, 'Frontend Developer', 2, 220000, TRUE
WHERE NOT EXISTS (SELECT 1 FROM resumes WHERE name = 'Frontend Developer' AND applicant_id = 1);

INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id)
SELECT 'Backend Developer', 'Ищем Java-разработчика с опытом 3+ лет', 1, 300000, 3, 5, TRUE, 2
WHERE NOT EXISTS (SELECT 1 FROM vacancies WHERE name = 'Backend Developer' AND author_id = 2);

INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id)
SELECT 'Frontend Developer', 'Ищем React-разработчика', 2, 280000, 2, 4, TRUE, 2
WHERE NOT EXISTS (SELECT 1 FROM vacancies WHERE name = 'Frontend Developer' AND author_id = 2);

INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation)
SELECT 1, 1, TRUE
WHERE NOT EXISTS (SELECT 1 FROM responded_applicants WHERE resume_id = 1 AND vacancy_id = 1);

INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation)
SELECT 2, 2, FALSE
WHERE NOT EXISTS (SELECT 1 FROM responded_applicants WHERE resume_id = 2 AND vacancy_id = 2);

INSERT INTO contact_types (type)
SELECT 'Телефон'
WHERE NOT EXISTS (SELECT 1 FROM contact_types WHERE type = 'Телефон');

INSERT INTO contact_types (type)
SELECT 'Email'
WHERE NOT EXISTS (SELECT 1 FROM contact_types WHERE type = 'Email');

