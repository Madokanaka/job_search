<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="mb-0"><@spring.message "resumes.title"/></h1>
        <span class="badge bg-primary fs-6">
            <#if resumes??>${resumes?filter(r -> r.isActive)?size}<#else>0</#if> <@spring.message "resumes.found"/>
        </span>
    </div>

    <#if resumes??>
        <#assign activeResumes = resumes?filter(r -> r.isActive)>

        <#if activeResumes?size gt 0>
            <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
                <#list activeResumes as resume>
                    <div class="col">
                        <div class="card h-100 shadow-sm">
                            <div class="card-body d-flex flex-column">
                                <h5 class="card-title mb-3">${resume.name!'<@spring.message "resumes.name.none"/>'}</h5>

                                <div class="d-flex align-items-center mb-2">
                                    <span class="badge bg-success text-white me-2">
                                        <#if resume.salary??>
                                            ${resume.salary?string["0"]} <@spring.message "resumes.currency"/>
                                        <#else>
                                            <@spring.message "resumes.salary.none"/>
                                        </#if>
                                    </span>
                                    <span class="badge bg-info text-white">
                                        <@spring.message "resumes.category.label"/>: ${resume.categoryName!'<@spring.message "resumes.category.none"/>'}
                                    </span>
                                </div>

                                <div class="mt-auto pt-3">
                                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                        <a href="/resumes/${resume.id}"
                                           class="btn btn-outline-primary btn-sm me-md-2"
                                           aria-label="<@spring.message "resumes.view.aria"/>"
                                           title="<@spring.message "resumes.view.title"/>">
                                            <i class="bi bi-eye"></i> <@spring.message "resumes.view"/>
                                        </a>
                                        <button class="btn btn-primary btn-sm"
                                                aria-label="<@spring.message "resumes.contact.aria"/>"
                                                title="<@spring.message "resumes.contact.title"/>">
                                            <i class="bi bi-telephone"></i> <@spring.message "resumes.contact"/>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </#list>
            </div>
            <div class="d-flex justify-content-center mt-4">
                <nav aria-label="<@spring.message "resumes.pagination.label"/>">
                    <ul class="pagination">
                        <#if (currentPage > 0)>
                            <li class="page-item">
                                <a class="page-link"
                                   href="/resumes?page=${currentPage - 1}&size=9"
                                   aria-label="<@spring.message "resumes.pagination.prev.aria"/>">
                                    <@spring.message "resumes.pagination.prev"/>
                                </a>
                            </li>
                        </#if>

                        <#list 0..(totalPages - 1) as i>
                            <li class="page-item <#if i == currentPage>active</#if>">
                                <a class="page-link"
                                   href="/resumes?page=${i}&size=9"
                                   aria-label="<@spring.message "resumes.pagination.page.aria"/> ${i + 1}">
                                    ${i + 1}
                                </a>
                            </li>
                        </#list>

                        <#if (currentPage?? && (currentPage + 1) < totalPages)>
                            <li class="page-item">
                                <a class="page-link"
                                   href="/resumes?page=${currentPage + 1}&size=9"
                                   aria-label="<@spring.message "resumes.pagination.next.aria"/>">
                                    <@spring.message "resumes.pagination.next"/>
                                </a>
                            </li>
                        </#if>
                    </ul>
                </nav>
            </div>
        <#else>
            <div class="alert alert-info mt-4">
                <i class="bi bi-info-circle"></i> <@spring.message "resumes.empty"/>
            </div>
        </#if>
    <#else>
        <div class="alert alert-warning">
            <i class="bi bi-exclamation-triangle"></i> <@spring.message "resumes.unavailable"/>
        </div>
    </#if>
</@main.layout>