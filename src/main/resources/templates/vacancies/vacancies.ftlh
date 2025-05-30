<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1><@spring.message "vacancies.title"/></h1>
        </div>

        <#if vacancies?? && vacancies?size gt 0>
            <form method="get" action="/vacancies" class="mb-4 d-flex justify-content-end" id="vacancyFilterForm">
                <input type="hidden" name="page" value="${currentPage}" />
                <input type="hidden" name="size" value="6" />

                <label for="sort" class="me-2"><@spring.message "vacancies.sort.label"/></label>
                <select name="sort" id="sort" class="form-select w-auto" onchange="saveFilterAndSubmit()">
                    <option value="date" <#if (sort == "date")>selected</#if>><@spring.message "vacancies.sort.date"/></option>
                    <option value="responses" <#if (sort == "responses")>selected</#if>><@spring.message "vacancies.sort.responses"/></option>
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
                                                        ${vacancy.salary?string["0"]} <@spring.message "vacancies.currency"/>
                                                    <#else>
                                                        <@spring.message "vacancies.salary.none"/>
                                                    </#if>
                                                </span>
                                                <span class="text-muted">
                                                    <i class="bi bi-briefcase"></i> <@spring.message "vacancies.experience.label"/>:
                                                    <#if vacancy.expFrom?? && vacancy.expTo??>
                                                        <@spring.messageArgs code="vacancies.experience.range" args=[vacancy.expFrom, vacancy.expTo]/>
                                                    <#elseif vacancy.expFrom??>
                                                        <@spring.messageArgs code="vacancies.experience.from" args=[vacancy.expFrom]/>
                                                    <#elseif vacancy.expTo??>
                                                        <@spring.messageArgs code="vacancies.experience.to" args=[vacancy.expTo]/>
                                                    <#else>
                                                        <@spring.message "vacancies.experience.none"/>
                                                    </#if>
                                                </span>
                                                <span class="text-muted">
                                                    <i class="bi bi-calendar"></i> <@spring.message "vacancies.updated"/>: ${vacancy.updateTime.toLocalDate()}
                                                </span>
                                            </div>
                                        </div>
                                        <div>
                                            <#if vacancy.authorId??>
                                                <a href="/profile/${vacancy.authorId}"
                                                   class="btn btn-sm btn-outline-secondary">
                                                    <@spring.message "vacancies.company.profile"/>
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
                                                   <@spring.messageArgs code="vacancies.category" args=[vacancy.categoryName]/>
                                                </span>
                                            </#if>
                                        </div>
                                        <div>
                                            <a href="/vacancies/${vacancy.id}" class="btn btn-outline-primary me-2">
                                                <@spring.message "vacancies.details"/>
                                            </a>

                                            <#if accountType?? && accountType == "applicant">
                                                <button class="btn btn-primary apply-btn"
                                                        data-bs-toggle="modal"
                                                        data-bs-target="#applyModal"
                                                        data-vacancy-id="${vacancy.id}"
                                                        data-category-id="${vacancy.categoryId}"
                                                        data-employer-id="${vacancy.authorId}">
                                                    <@spring.message "vacancies.apply"/>
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
                                    <a class="page-link" href="/vacancies?page=${currentPage - 1}&size=6&sort=${sort}"><@spring.message "vacancies.pagination.prev"/></a>
                                </li>
                            </#if>

                            <#list 0..(totalPages - 1) as i>
                                <li class="page-item <#if i == currentPage>active</#if>">
                                    <a class="page-link" href="/vacancies?page=${i}&size=6&sort=${sort}">${i + 1}</a>
                                </li>
                            </#list>

                            <#if (currentPage?? && (currentPage + 1) < totalPages)>
                                <li class="page-item">
                                    <a class="page-link" href="/vacancies?page=${currentPage + 1}&size=6&sort=${sort}"><@spring.message "vacancies.pagination.next"/></a>
                                </li>
                            </#if>
                        </ul>
                    </nav>
                </div>
            </div>
            <div class="modal fade" id="applyModal" tabindex="-1" aria-labelledby="applyModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="applyModalLabel"><@spring.message "vacancies.apply.modal.title"/></h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="<@spring.message 'vacancies.apply.modal.close'/>"></button>
                        </div>
                        <div class="modal-body">
                            <form id="applyForm">
                                <input type="hidden" id="vacancyId" name="vacancyId">
                                <input type="hidden" id="employerId" name="employerId">
                                <div class="mb-3">
                                    <label for="resumeSelect" class="form-label"><@spring.message "vacancies.apply.modal.resume.label"/></label>
                                    <select class="form-select" id="resumeSelect" name="resumeId" required>
                                        <option value="" disabled selected><@spring.message "vacancies.apply.modal.resume.placeholder"/></option>
                                    </select>
                                </div>
                                <button type="submit" class="btn btn-primary"><@spring.message "vacancies.apply.modal.submit"/></button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        <#else>
            <div class="alert alert-info" role="alert">
                <@spring.message "vacancies.empty"/>
            </div>
        </#if>
    </div>
    <script>
        const messages = {
            resumePlaceholder: '<@spring.message "vacancies.apply.modal.resume.placeholder"/>',
            resumeEmpty: '<@spring.message "vacancies.apply.modal.resume.empty"/>',
            error: '<@spring.message "vacancies.apply.modal.error"/>'
        };
    </script>
    <script src="/static/js/vacancies.js"></script>
</@main.layout>