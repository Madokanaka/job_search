<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1>Активные вакансии</h1>
        </div>

        <#if vacancies?? && vacancies?size gt 0>
            <div class="row">
                <#list vacancies as vacancy>
                    <#if vacancy.isActive>
                        <div class="col-12 mb-4">
                            <div class="card h-100 shadow-sm">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <div>
                                            <h3 class="card-title mb-2">${vacancy.name}</h3>
                                            <div class="d-flex flex-wrap align-items-center gap-3 mb-3">
                                                <span class="badge bg-success fs-6">
                                                    <#if vacancy.salary??>
                                                        ${vacancy.salary?string["0"]} ₽
                                                    <#else>
                                                        З/П не указана
                                                    </#if>
                                                </span>
                                                <span class="text-muted">
                                                    <i class="bi bi-briefcase"></i> Опыт:
                                                    <#if vacancy.expFrom?? && vacancy.expTo??>
                                                        ${vacancy.expFrom} - ${vacancy.expTo} лет
                                                    <#elseif vacancy.expFrom??>
                                                        от ${vacancy.expFrom} лет
                                                    <#elseif vacancy.expTo??>
                                                        до ${vacancy.expTo} лет
                                                    <#else>
                                                        Без опыта
                                                    </#if>
                                                </span>
                                                <span class="text-muted">
                                                    <i class="bi bi-calendar"></i> Обновлено: ${vacancy.updateTime.toLocalDate()}
                                                </span>
                                            </div>
                                        </div>
                                        <div>
                                            <#if vacancy.authorId??>
                                                <a href="/profile/${vacancy.authorId}"
                                                   class="btn btn-sm btn-outline-secondary">
                                                    Профиль компании
                                                </a>
                                            </#if>
                                        </div>
                                    </div>

                                    <#if vacancy.description??>
                                        <div class="card-text mt-3 mb-3">
                                            ${vacancy.description}
                                        </div>
                                    </#if>

                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <#if vacancy.categoryId??>
                                                <span class="badge bg-info text-dark">
                                                   Категория ${vacancy.categoryId}
                                                </span>
                                            </#if>
                                        </div>
                                        <div>
<#--                                            <a href="/vacancies/${vacancy.id}" class="btn btn-outline-primary me-2">-->
<#--                                                Подробнее-->
<#--                                            </a>-->
                                            <button  class="btn btn-outline-primary me-2">
                                                Подробнее
                                            </button>

                                            <#if accountType?? && accountType == "applicant">
                                                <button class="btn btn-primary">
                                                    Откликнуться
                                                </button>
                                            </#if>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </#if>
                </#list>
            </div>
        <#else>
            <div class="alert alert-info" role="alert">
                На данный момент активных вакансий нет.
            </div>
        </#if>
    </div>
</@main.layout>