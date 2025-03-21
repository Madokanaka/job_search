CREATE TABLE IF NOT EXISTS users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(100) NOT NULL,
                                     surname VARCHAR(100) NOT NULL,
                                     age INT,
                                     email VARCHAR(255) UNIQUE NOT NULL,
                                     password VARCHAR(255) NOT NULL,
                                     phone_number VARCHAR(20),
                                     avatar VARCHAR(255),
                                     account_type VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
                                          id INT AUTO_INCREMENT PRIMARY KEY,
                                          name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS resumes (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       applicant_id INT NOT NULL,
                                       name VARCHAR(255) NOT NULL,
                                       category_id INT,
                                       salary DOUBLE,
                                       is_active BOOLEAN DEFAULT TRUE,
                                       created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       FOREIGN KEY (applicant_id) REFERENCES users(id),
                                       FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS vacancies (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL,
                                         description TEXT,
                                         category_id INT,
                                         salary DOUBLE,
                                         exp_from INT,
                                         exp_to INT,
                                         is_active BOOLEAN DEFAULT TRUE,
                                         author_id INT NOT NULL,
                                         created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         FOREIGN KEY (author_id) REFERENCES users(id),
                                         FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS responded_applicants (
                                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                                    resume_id INT NOT NULL,
                                                    vacancy_id INT NOT NULL,
                                                    confirmation BOOLEAN DEFAULT FALSE,
                                                    FOREIGN KEY (resume_id) REFERENCES resumes(id),
                                                    FOREIGN KEY (vacancy_id) REFERENCES vacancies(id)
);


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

SELECT * FROM users;
SELECT * FROM categories;
SELECT * FROM resumes;
SELECT * FROM vacancies;
SELECT * FROM responded_applicants;
