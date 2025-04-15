<#import "layout.ftlh" as main>
<@main.layout>
    <div class="profile-info text-center">
        <h1 class="mb-5">
            Личный кабинет
            <#if user.accountType == "employer">
                Работодатель
            <#elseif user.accountType == "applicant">
                Соискатель
            <#else>
                Администратор
            </#if>
        </h1>
    </div>

    <div class="row mb-5">
        <div class="col-md-7 d-flex align-items-center">
            <img src="${user.avatar?has_content?then('/api/images/' + user.avatar, 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png')}"
                 alt="Фото профиля" class="me-5  border" style="width: 100px; height: 100px; object-fit: cover;">

            <div class="ms-4">
                <h3 class="mb-2">${user.getName()} ${user.getSurname()}</h3>
                <p class="text-muted mb-2">Возраст: ${user.getAge()}</p>
                <button class="btn btn-primary w-100 mb-2">Редактировать</button>
            </div>
        </div>

        <div class="col-md-5 row d-flex justify-content-between">
            <div class="col-md-6">
                <button class="btn btn-outline-primary h-100 w-100 mb-2">
                    <span class="me-2">+</span> Создать резюме
                </button>
            </div>

            <#if user.accountType == "employer">
                <div class="col-md-6">
                    <button class="btn btn-outline-primary h-100 w-100 mb-2">
                        <span class="me-2"></span> Отклики
                    </button>
                </div>
            </#if>
        </div>
    </div>

    <#if user.accountType == "employer" || user.accountType == "admin">
        <h5 class="mb-3">Созданные вакансии:</h5>
        <div class="row g-3">
            <#if vacancies?? && vacancies?size gt 0>
                <#list vacancies as vacancy>
                    <div class="col-md-4">
                        <div class="card h-100">
                            <div class="card-body">
                                <h6 class="vacancy-title">${vacancy.name!'Название вакансии'}</h6>
                                <p class="text-muted">Дата обновления: ${vacancy.updatedDate!'дд/мм/гггг'}</p>
                            </div>
                        </div>
                        <div class="d-flex justify-content-between">
                            <button class="btn btn-outline-primary w-50">Обновить</button>
                            <button class="btn btn-primary w-50">Ред.</button>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="alert alert-info">Нет созданных вакансий</div>
            </#if>
        </div>
    </#if>

    <#if user.accountType == "applicant" || user.accountType == "admin">
        <h5 class="mb-3 mt-4">Мои резюме:</h5>
        <div class="row g-3">
            <#if resumes?? && resumes?size gt 0>
                <#list resumes as resume>
                    <div class="col-md-4 mb-5">
                        <div class="card vacancy-card h-100">
                            <div class="card-body">
                                <h6 class="vacancy-title">${resume.name!'Название резюме'}</h6>
                                <p class="text-muted">Дата обновления: ${resume.updatedDate!'дд/мм/гггг'}</p>
                            </div>
                        </div>
                        <div class="d-flex justify-content-between">
                            <button class="btn btn-outline-primary w-50">Обновить</button>
                            <button class="btn btn-primary w-50">Ред.</button>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="alert alert-info">Нет созданных резюме</div>
            </#if>
        </div>
    </#if>
</@main.layout>

