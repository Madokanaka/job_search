INSERT INTO categories (name, parent_id) VALUES
                                             ('Fullstack Developer', NULL),
                                             ('DevOps Engineer', NULL),
                                             ('QA Engineer', NULL),
                                             ('Data Scientist', NULL),
                                             ('Mobile Developer', NULL),
                                             ('Project Manager', NULL),
                                             ('Game Developer', NULL),
                                             ('Business Analyst', NULL),
                                             ('UI/UX Designer', NULL),
                                             ('System Administrator', NULL),
                                             ('Digital Marketing Specialist', NULL),
                                             ('Content Writer', NULL),
                                             ('Graphic Designer', NULL),
                                             ('HR Specialist', NULL),
                                             ('Customer Support Specialist', NULL),
                                             ('Financial Analyst', NULL),
                                             ('Product Manager', NULL);



INSERT INTO resumes (applicant_id, name, category_id, salary, is_active)
VALUES
    ((SELECT id FROM users WHERE email = 'ivan.petrov@example.com'), 'Java Developer #1', (SELECT id FROM categories WHERE name = 'Java Developer'), 250000, TRUE),
    ((SELECT id FROM users WHERE email = 'ivan.petrov@example.com'), 'Frontend Developer #2', (SELECT id FROM categories WHERE name = 'Frontend Developer'), 220000, TRUE),
    ((SELECT id FROM users WHERE email = 'ivan.petrov@example.com'), 'Fullstack Developer #3', (SELECT id FROM categories WHERE name = 'Fullstack Developer'), 270000, TRUE),
    ((SELECT id FROM users WHERE email = 'ivan.petrov@example.com'), 'DevOps Engineer #4', (SELECT id FROM categories WHERE name = 'DevOps Engineer'), 300000, TRUE),
    ((SELECT id FROM users WHERE email = 'ivan.petrov@example.com'), 'QA Engineer #5', (SELECT id FROM categories WHERE name = 'QA Engineer'), 210000, TRUE),
    ((SELECT id FROM users WHERE email = 'ivan.petrov@example.com'), 'Data Scientist #6', (SELECT id FROM categories WHERE name = 'Data Scientist'), 320000, TRUE),
    ((SELECT id FROM users WHERE email = 'ivan.petrov@example.com'), 'Mobile Developer #7', (SELECT id FROM categories WHERE name = 'Mobile Developer'), 230000, TRUE),
    ((SELECT id FROM users WHERE email = 'ivan.petrov@example.com'), 'Project Manager #8', (SELECT id FROM categories WHERE name = 'Project Manager'), 350000, TRUE),
    ((SELECT id FROM users WHERE email = 'ivan.petrov@example.com'), 'Game Developer #9', (SELECT id FROM categories WHERE name = 'Game Developer'), 240000, TRUE),
    ((SELECT id FROM users WHERE email = 'ivan.petrov@example.com'), 'Business Analyst #10', (SELECT id FROM categories WHERE name = 'Business Analyst'), 260000, TRUE),
    ((SELECT id FROM users WHERE email = 'ivan.petrov@example.com'), 'Java Developer #11', (SELECT id FROM categories WHERE name = 'Java Developer'), 270000, TRUE),
    ((SELECT id FROM users WHERE email = 'ivan.petrov@example.com'), 'Frontend Developer #12', (SELECT id FROM categories WHERE name = 'Frontend Developer'), 225000, TRUE),
    ((SELECT id FROM users WHERE email = 'ivan.petrov@example.com'), 'QA Engineer #13', (SELECT id FROM categories WHERE name = 'QA Engineer'), 215000, TRUE),
    ((SELECT id FROM users WHERE email = 'ivan.petrov@example.com'), 'Mobile Developer #14', (SELECT id FROM categories WHERE name = 'Mobile Developer'), 235000, TRUE),
    ((SELECT id FROM users WHERE email = 'ivan.petrov@example.com'), 'DevOps Engineer #15', (SELECT id FROM categories WHERE name = 'DevOps Engineer'), 310000, TRUE);


INSERT INTO education_info (resume_id, institution, program, start_date, end_date, degree)
VALUES
    ((SELECT id FROM resumes WHERE name = 'Java Developer #1'), 'КТМУ Манас', 'Программная инженерия', '2010-09-01', '2014-06-30', 'Бакалавр'),
    ((SELECT id FROM resumes WHERE name = 'Frontend Developer #2'), 'АУЦА', 'Дизайн интерфейсов', '2011-09-01', '2015-06-30', 'Бакалавр'),
    ((SELECT id FROM resumes WHERE name = 'Fullstack Developer #3'), 'KNU', 'Информатика', '2009-09-01', '2013-06-30', 'Бакалавр'),
    ((SELECT id FROM resumes WHERE name = 'DevOps Engineer #4'), 'KSTU', 'Системное администрирование', '2012-09-01', '2016-06-30', 'Бакалавр'),
    ((SELECT id FROM resumes WHERE name = 'QA Engineer #5'), 'БГУ', 'Тестирование ПО', '2013-09-01', '2017-06-30', 'Бакалавр'),
    ((SELECT id FROM resumes WHERE name = 'Data Scientist #6'), 'Moscow State', 'Data Science', '2015-09-01', '2019-06-30', 'Магистр'),
    ((SELECT id FROM resumes WHERE name = 'Mobile Developer #7'), 'TUIT', 'Мобильная разработка', '2014-09-01', '2018-06-30', 'Бакалавр'),
    ((SELECT id FROM resumes WHERE name = 'Project Manager #8'), 'Harvard', 'Project Management', '2010-09-01', '2012-06-30', 'MBA'),
    ((SELECT id FROM resumes WHERE name = 'Game Developer #9'), 'Unity School', 'Game Dev', '2016-09-01', '2020-06-30', 'Бакалавр'),
    ((SELECT id FROM resumes WHERE name = 'Business Analyst #10'), 'AUCA', 'Бизнес-аналитика', '2011-09-01', '2015-06-30', 'Бакалавр'),
    ((SELECT id FROM resumes WHERE name = 'Java Developer #11'), 'КТМУ Манас', 'Программная инженерия', '2012-09-01', '2016-06-30', 'Бакалавр'),
    ((SELECT id FROM resumes WHERE name = 'Frontend Developer #12'), 'Codify Academy', 'Frontend-разработка', '2017-01-01', '2018-12-01', 'Курс'),
    ((SELECT id FROM resumes WHERE name = 'QA Engineer #13'), 'Skillbox', 'QA Automation', '2020-03-01', '2021-03-01', 'Сертификат'),
    ((SELECT id FROM resumes WHERE name = 'Mobile Developer #14'), 'GeekBrains', 'Android Dev', '2018-01-01', '2019-01-01', 'Курс'),
    ((SELECT id FROM resumes WHERE name = 'DevOps Engineer #15'), 'Udemy', 'DevOps with AWS', '2019-01-01', '2019-12-01', 'Курс');

INSERT INTO work_experience (resume_id, years, company_name, position, responsibilities)
VALUES
    ((SELECT id FROM resumes WHERE name = 'Java Developer #1'), 5, 'Tech Corp', 'Java Developer', 'Разработка бэкенда на Spring'),
    ((SELECT id FROM resumes WHERE name = 'Frontend Developer #2'), 3, 'Web Studio', 'React Developer', 'Разработка SPA-приложений'),
    ((SELECT id FROM resumes WHERE name = 'Fullstack Developer #3'), 6, 'SoftGroup', 'Fullstack Dev', 'Backend + Frontend'),
    ((SELECT id FROM resumes WHERE name = 'DevOps Engineer #4'), 4, 'CloudX', 'DevOps', 'CI/CD, Docker, Kubernetes'),
    ((SELECT id FROM resumes WHERE name = 'QA Engineer #5'), 2, 'TestIt', 'QA Engineer', 'Ручное и авто-тестирование'),
    ((SELECT id FROM resumes WHERE name = 'Data Scientist #6'), 3, 'DataFlow', 'Data Scientist', 'Моделирование и аналитика'),
    ((SELECT id FROM resumes WHERE name = 'Mobile Developer #7'), 5, 'MobileSoft', 'Android Developer', 'Создание Android-приложений'),
    ((SELECT id FROM resumes WHERE name = 'Project Manager #8'), 7, 'BizTech', 'Project Manager', 'Управление проектами в IT'),
    ((SELECT id FROM resumes WHERE name = 'Game Developer #9'), 4, 'GameHouse', 'Unity Developer', 'Разработка 2D/3D игр'),
    ((SELECT id FROM resumes WHERE name = 'Business Analyst #10'), 6, 'FinTech', 'Business Analyst', 'Сбор требований, анализ'),
    ((SELECT id FROM resumes WHERE name = 'Java Developer #11'), 4, 'CodeLine', 'Java Backend Dev', 'Микросервисы и REST'),
    ((SELECT id FROM resumes WHERE name = 'Frontend Developer #12'), 3, 'MediaSoft', 'Vue.js Dev', 'Интерфейсы и адаптивность'),
    ((SELECT id FROM resumes WHERE name = 'QA Engineer #13'), 2, 'QA Pro', 'QA Auto', 'Тестирование API и UI'),
    ((SELECT id FROM resumes WHERE name = 'Mobile Developer #14'), 3, 'AppLab', 'iOS Developer', 'Swift и iOS-приложения'),
    ((SELECT id FROM resumes WHERE name = 'DevOps Engineer #15'), 5, 'InfraTeam', 'DevOps Engineer', 'CI/CD и облачные инфраструктуры');

INSERT INTO contact_info (type_id, resume_id, infovalue)
SELECT (SELECT id FROM contact_types WHERE type = 'Телефон'), id, CONCAT('+7700123', LPAD(FLOOR(RAND() * 10000), 4, '0'))
FROM resumes;

INSERT INTO contact_info (type_id, resume_id, infovalue)
SELECT (SELECT id FROM contact_types WHERE type = 'Email'), id, CONCAT('resume', id, '@example.com')
FROM resumes;



INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id)
VALUES
    ('Data Analyst', 'Требуется Data Analyst с опытом работы в Power BI и SQL.', (SELECT id FROM categories WHERE name = 'Data Scientist'), 200000, 2, 4, TRUE, (SELECT id FROM users WHERE email = 'maria.sidorova@example.com')),
    ('Project Manager', 'Управление проектами в сфере IT. Agile/Scrum обязателен.', (SELECT id FROM categories WHERE name = 'Project Manager'), 250000, 3, 6, TRUE, (SELECT id FROM users WHERE email = 'maria.sidorova@example.com')),
    ('UI/UX Designer', 'Разработка пользовательских интерфейсов для веб и моб. приложений.', (SELECT id FROM categories WHERE name = 'UI/UX Designer'), 180000, 1, 3, TRUE, (SELECT id FROM users WHERE email = 'maria.sidorova@example.com')),
    ('Android Developer', 'Ищем разработчика Android-приложений с опытом в Kotlin.', (SELECT id FROM categories WHERE name = 'Mobile Developer'), 270000, 2, 5, TRUE, (SELECT id FROM users WHERE email = 'maria.sidorova@example.com')),
    ('QA Engineer', 'Автоматизация и ручное тестирование веб-продуктов.', (SELECT id FROM categories WHERE name = 'QA Engineer'), 200000, 2, 4, TRUE, (SELECT id FROM users WHERE email = 'maria.sidorova@example.com')),
    ('DevOps Engineer', 'Нужен DevOps с опытом в Docker, Jenkins и Kubernetes.', (SELECT id FROM categories WHERE name = 'DevOps Engineer'), 320000, 3, 6, TRUE, (SELECT id FROM users WHERE email = 'maria.sidorova@example.com')),
    ('System Administrator', 'Обслуживание серверов Linux/Windows.', (SELECT id FROM categories WHERE name = 'System Administrator'), 180000, 2, 5, TRUE, (SELECT id FROM users WHERE email = 'maria.sidorova@example.com')),
    ('Business Analyst', 'Анализ требований заказчиков, составление ТЗ.', (SELECT id FROM categories WHERE name = 'Business Analyst'), 230000, 2, 4, TRUE, (SELECT id FROM users WHERE email = 'maria.sidorova@example.com')),
    ('Digital Marketing Specialist', 'Настройка рекламы в Google Ads, SMM.', (SELECT id FROM categories WHERE name = 'Digital Marketing Specialist'), 160000, 1, 3, TRUE, (SELECT id FROM users WHERE email = 'maria.sidorova@example.com')),
    ('Content Writer', 'Написание текстов для IT-продуктов и маркетинга.', (SELECT id FROM categories WHERE name = 'Content Writer'), 140000, 1, 2, TRUE, (SELECT id FROM users WHERE email = 'maria.sidorova@example.com')),
    ('Graphic Designer', 'Дизайн баннеров, логотипов, маркетинговых материалов.', (SELECT id FROM categories WHERE name = 'Graphic Designer'), 150000, 1, 3, TRUE, (SELECT id FROM users WHERE email = 'maria.sidorova@example.com')),
    ('HR Specialist', 'Подбор IT-персонала, проведение интервью.', (SELECT id FROM categories WHERE name = 'HR Specialist'), 170000, 2, 4, TRUE, (SELECT id FROM users WHERE email = 'maria.sidorova@example.com')),
    ('Customer Support Specialist', 'Поддержка пользователей на русском и английском языках.', (SELECT id FROM categories WHERE name = 'Customer Support Specialist'), 130000, 1, 2, TRUE, (SELECT id FROM users WHERE email = 'maria.sidorova@example.com')),
    ('Financial Analyst', 'Финансовое планирование и отчётность в IT-компании.', (SELECT id FROM categories WHERE name = 'Financial Analyst'), 240000, 2, 4, TRUE, (SELECT id FROM users WHERE email = 'maria.sidorova@example.com')),
    ('Product Manager', 'Управление разработкой IT-продуктов, взаимодействие с командой.', (SELECT id FROM categories WHERE name = 'Product Manager'), 300000, 3, 6, TRUE, (SELECT id FROM users WHERE email = 'maria.sidorova@example.com'));
