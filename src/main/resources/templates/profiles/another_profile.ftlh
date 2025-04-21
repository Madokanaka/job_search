<#import "../layout.ftlh" as main>
<@main.layout ; spring>
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
            <div class="position-relative d-inline-block me-4">
                <div class="d-inline-block position-relative" style="cursor: pointer;">
                    <img src="${user.avatar?has_content?then('/api/images/' + user.avatar, 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png')}"
                         alt="Фото профиля"
                         class="border rounded-circle"
                         style="width: 100px; height: 100px; object-fit: cover;"/>
                </div>
            </div>


            <div class="ms-4">
                <h3 class="mb-2">${user.getName()} ${user.getSurname()}</h3>
                <p class="text-muted mb-2">Возраст: ${user.getAge()}</p>
            </div>
        </div>
    </div>

    <#if user.accountType == "employer" || user.accountType == "admin">
        <h5 class="mb-3">Созданные вакансии:</h5>
        <div class="row g-3 mb-5">
            <#if vacancies?? && vacancies?size gt 0>
                <#list vacancies as vacancy>
                    <div class="col-md-4 mb-5">
                        <div class="card h-100">
                            <div class="card-body">
                                <h6 class="vacancy-title">${vacancy.name!'Название вакансии'}</h6>
                                <p class="text-muted">Дата
                                    обновления: ${vacancy.updateTime.toLocalDate()!'дд/мм/гггг'}</p>
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
        <h5 class="mb-3 mt-4">Резюме пользователя:</h5>
        <div class="row g-3">
            <#if resumes?? && resumes?size gt 0>
                <#list resumes as resume>
                    <div class="col-md-4 mb-5">
                        <div class="card vacancy-card h-100">
                            <div class="card-body">
                                <h6 class="vacancy-title">${resume.name!'Название резюме'}</h6>
                                <p class="text-muted">Дата
                                    обновления: ${resume.update_time.toLocalDate()!'дд/мм/гггг'}</p>
                            </div>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="alert alert-info">Нет созданных резюме</div>
            </#if>
        </div>
    </#if>

    <#if (user.accountType == "employer" || user.accountType == "admin") && vacancyTotalPages?? && (vacancyTotalPages > 1)>
        <div class="col-12 d-flex justify-content-center mt-4">
            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <#if vacancyCurrentPage?? && (vacancyCurrentPage > 0)>
                        <li class="page-item">
                            <a class="page-link" href="?page=${vacancyCurrentPage - 1}&size=6">«</a>
                        </li>
                    </#if>

                    <#list 0..(vacancyTotalPages - 1) as i>
                        <li class="page-item <#if i == vacancyCurrentPage>active</#if>">
                            <a class="page-link" href="?page=${i}&size=6">${i + 1}</a>
                        </li>
                    </#list>

                    <#if vacancyCurrentPage?? && (vacancyCurrentPage + 1) < vacancyTotalPages>
                        <li class="page-item">
                            <a class="page-link" href="?page=${vacancyCurrentPage + 1}&size=6">»</a>
                        </li>
                    </#if>
                </ul>
            </nav>
        </div>
    </#if>

    <#if (user.accountType == "applicant" || user.accountType == "admin") && resumeTotalPages?? && (resumeTotalPages > 1)>
        <div class="col-12 d-flex justify-content-center mt-4">
            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <#if resumeCurrentPage?? && (resumeCurrentPage > 0)>
                        <li class="page-item">
                            <a class="page-link" href="?page=${resumeCurrentPage - 1}&size=6">«</a>
                        </li>
                    </#if>

                    <#list 0..(resumeTotalPages - 1) as i>
                        <li class="page-item <#if i == resumeCurrentPage>active</#if>">
                            <a class="page-link" href="?page=${i}&size=6">${i + 1}</a>
                        </li>
                    </#list>

                    <#if resumeCurrentPage?? && (resumeCurrentPage + 1) < resumeTotalPages>
                        <li class="page-item">
                            <a class="page-link" href="?page=${resumeCurrentPage + 1}&size=6">»</a>
                        </li>
                    </#if>
                </ul>
            </nav>
        </div>
    </#if>

</@main.layout>

