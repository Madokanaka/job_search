<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1>Активные вакансии</h1>
        </div>

        <#if vacancies?? && vacancies?size gt 0>
            <form method="get" action="/vacancies" class="mb-4 d-flex justify-content-end">
                <input type="hidden" name="page" value="${currentPage}" />
                <input type="hidden" name="size" value="6" />

                <label for="sort" class="me-2">Сортировка:</label>
                <select name="sort" id="sort" class="form-select w-auto" onchange="this.form.submit()">
                    <option value="date" <#if (sort == "date")>selected</#if>>По дате</option>
                    <option value="responses" <#if (sort == "responses")>selected</#if>>По количеству откликов</option>
                </select>
            </form>
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
                                                   Категория ${vacancy.categoryName}
                                                </span>
                                            </#if>
                                        </div>
                                        <div>

                                            <a href="/vacancies/${vacancy.id}" class="btn btn-outline-primary me-2">
                                                Подробнее
                                            </a>

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
                <div class="d-flex justify-content-center mt-4">
                    <nav aria-label="Pagination">
                        <ul class="pagination">
                            <#if (currentPage > 0)>
                                <li class="page-item">
                                    <a class="page-link" href="/vacancies?page=${currentPage - 1}&size=6&sort=${sort}">«</a>
                                </li>
                            </#if>

                            <#list 0..(totalPages - 1) as i>
                                <li class="page-item <#if i == currentPage>active</#if>">
                                    <a class="page-link" href="/vacancies?page=${i}&size=6&sort=${sort}">${i + 1}</a>
                                </li>
                            </#list>

                            <#if (currentPage?? && (currentPage + 1) < totalPages)>
                                <li class="page-item">
                                    <a class="page-link" href="/vacancies?page=${currentPage + 1}&size=6&sort=${sort}">»</a>
                                </li>
                            </#if>
                        </ul>
                    </nav>
                </div>


            </div>
        <#else>
            <div class="alert alert-info" role="alert">
                На данный момент активных вакансий нет.
            </div>
        </#if>
    </div>
</@main.layout>