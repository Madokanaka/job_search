<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Личный кабинет</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .profile-header {
            display: flex;
            align-items: center;
            margin-bottom: 1.5rem;
        }
        .profile-photo {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            object-fit: cover;
            margin-right: 1rem;
            border: 2px solid #dee2e6;
        }
        .profile-info {
            flex-grow: 1;
        }
        .account-badge {
            font-size: 0.9rem;
        }
        .company-info {
            background-color: #f8f9fa;
            padding: 1rem;
            border-radius: 0.25rem;
            margin-bottom: 1.5rem;
        }
        .company-name {
            font-size: 1.2rem;
            font-weight: 500;
            margin-bottom: 1rem;
        }
        .vacancy-card {
            border-radius: 0.25rem;
            margin-bottom: 1rem;
            box-shadow: none;
            border: 1px solid #dee2e6;
        }
        .vacancy-title {
            font-size: 1.1rem;
            margin-bottom: 0.5rem;
        }
        .update-date {
            color: #6c757d;
            font-size: 0.9rem;
            margin-bottom: 0.5rem;
        }
        .action-btn {
            margin-right: 0.5rem;
            padding: 0.25rem 0.75rem;
            font-size: 0.9rem;
        }
    </style>
</head>
<body>
<div class="container py-4">
    <div class="profile-header">
        <img src="${user.avatar?has_content?then('  /api/images/' + user.avatar, 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png')}"
             alt="Фото профиля"
             class="profile-photo">

        <div class="profile-info">
            <h1 class="mb-1">Личный кабинет</h1>
            <span class="badge bg-primary account-badge">
                <#if user.accountType == "employer">
                    Работодатель
                <#elseif user.accountType == "applicant">
                    Соискатель
                <#else>
                    Администратор
                </#if>
            </span>
        </div>
    </div>

    <!-- Кнопки действий -->
    <div class="mb-4">
        <a href="/profile/edit" class="btn btn-success">Редактировать</a>
        <#if user.accountType == "applicant" || user.accountType == "admin">
            <button class="btn btn-danger">Создать резюме</button>
        </#if>
    </div>

    <#if user.accountType == "employer" || user.accountType == "admin">
        <!-- Блок вакансий -->
        <h5 class="mb-3">Созданные вакансии:</h5>
        <div class="row g-3">
            <#if vacancies?? && vacancies?size gt 0>
                <#list vacancies as vacancy>
                    <div class="col-md-4">
                        <div class="card h-100 d-flex flex-column">
                            <div class="card-body flex-grow-1">
                                <h6 class="vacancy-title">${vacancy.name!'Название вакансии'}</h6>
                                <p class="update-date">Дата обновления: ${vacancy.updatedDate!'дд/мм/гггг'}</p>
                            </div>
                            <div class="card-footer bg-transparent border-0 d-flex justify-content-start">
                                <button class="btn btn-outline-primary action-btn">Обновить</button>
                                <button class="btn btn-primary action-btn">Ред.</button>
                            </div>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="alert alert-info">Нет созданных вакансий</div>
            </#if>
        </div>
    </#if>

    <#if user.accountType == "applicant" || user.accountType == "admin">
        <!-- Блок резюме -->
        <h5 class="mb-3 mt-4">Мои резюме:</h5>
        <div class="row g-3">  <!-- Контейнер для ряда -->
            <#if resumes?? && resumes?size gt 0>
                <#list resumes as resume>
                    <div class="col-md-4"> <!-- Каждая карточка будет занимать 1/3 ширины на экранах среднего размера и выше -->
                        <div class="card vacancy-card h-100 d-flex flex-column">
                            <div class="card-body flex-grow-1">
                                <h6 class="vacancy-title">${resume.name!'Название резюме'}</h6>
                                <p class="update-date">Дата обновления: ${resume.updatedDate!'дд/мм/гггг'}</p>
                            </div>
                            <div class="card-footer bg-transparent border-0 d-flex justify-content-start">
                                <button class="btn btn-outline-primary action-btn">Обновить</button>
                                <button class="btn btn-primary action-btn">Ред.</button>
                            </div>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="alert alert-info">Нет созданных резюме</div>
            </#if>
        </div>
    </#if>

</div>

<!-- Bootstrap JS Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>