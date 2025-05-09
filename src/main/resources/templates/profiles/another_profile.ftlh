<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="profile-info text-center">
        <h1 class="mb-5">
            <@spring.message "another_profile.title"/>
            <#if user.accountType == "employer">
                <@spring.message "another_profile.type.employer"/>
            <#elseif user.accountType == "applicant">
                <@spring.message "another_profile.type.applicant"/>
            <#else>
                <@spring.message "another_profile.type.admin"/>
            </#if>
        </h1>
    </div>

    <div class="row mb-5">
        <div class="col-md-7 d-flex align-items-center">
            <div class="position-relative d-inline-block me-4">
                <div class="d-inline-block position-relative" style="cursor: pointer;">
                    <img src="${user.avatar?has_content?then('/api/images/' + user.avatar, 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png')}"
                         alt="<@spring.message "another_profile.avatar.alt"/>"
                         class="border rounded-circle"
                         style="width: 100px; height: 100px; object-fit: cover;"/>
                </div>
            </div>

            <div class="ms-4">
                <h3 class="mb-2">${user.getName()} ${user.getSurname()}</h3>
                <p class="text-muted mb-2"><@spring.message "another_profile.age.label"/>: ${user.getAge()}</p>
            </div>
        </div>
    </div>

    <#if user.accountType == "employer" || user.accountType == "admin">
        <h5 class="mb-3"><@spring.message "another_profile.vacancies.title"/></h5>
        <div class="row g-3 mb-5">
            <#if vacancies?? && vacancies?size gt 0>
                <#list vacancies as vacancy>
                    <div class="col-md-4 mb-5">
                        <a href="/vacancies/${vacancy.id}"
                           class="text-decoration-none"
                           aria-label="<@spring.message "another_profile.vacancies.view.aria"/> ${vacancy.name!'<@spring.message "another_profile.vacancies.name.none"/>'}">
                            <div class="card h-100">
                                <div class="card-body">
                                    <h6 class="vacancy-title">${vacancy.name!'<@spring.message "another_profile.vacancies.name.none"/>'}</h6>
                                    <p class="text-muted"><@spring.message "another_profile.vacancies.updated"/>: ${vacancy.updateTime.toLocalDate()!'<@spring.message "another_profile.date.none"/>'}</p>
                                </div>
                            </div>
                        </a>
                    </div>
                </#list>
            <#else>
                <div class="alert alert-info"><@spring.message "another_profile.vacancies.empty"/></div>
            </#if>
        </div>
    </#if>

    <#if user.accountType == "applicant" || user.accountType == "admin">
        <h5 class="mb-3 mt-4"><@spring.message "another_profile.resumes.title"/></h5>
        <div class="row g-3">
            <#if resumes?? && resumes?size gt 0>
                <#list resumes as resume>
                    <div class="col-md-4 mb-5">
                        <a href="/resumes/${resume.id}"
                           class="text-decoration-none"
                           aria-label="<@spring.message "another_profile.resumes.view.aria"/> ${resume.name!'<@spring.message "another_profile.resumes.name.none"/>'}">
                            <div class="card vacancy-card h-100">
                                <div class="card-body">
                                    <h6 class="vacancy-title">${resume.name!'<@spring.message "another_profile.resumes.name.none"/>'}</h6>
                                    <p class="text-muted"><@spring.message "another_profile.resumes.updated"/>: ${resume.update_time.toLocalDate()!'<@spring.message "another_profile.date.none"/>'}</p>
                                </div>
                            </div>
                        </a>
                    </div>
                </#list>
            <#else>
                <div class="alert alert-info"><@spring.message "another_profile.resumes.empty"/></div>
            </#if>
        </div>
    </#if>

    <#if (user.accountType == "employer" || user.accountType == "admin") && vacancyTotalPages?? && (vacancyTotalPages > 1)>
        <div class="col-12 d-flex justify-content-center mt-4">
            <nav aria-label="<@spring.message "another_profile.vacancies.pagination.label"/>">
                <ul class="pagination">
                    <#if vacancyCurrentPage?? && (vacancyCurrentPage > 0)>
                        <li class="page-item">
                            <a class="page-link"
                               href="?page=${vacancyCurrentPage - 1}&size=6"
                               aria-label="<@spring.message "another_profile.vacancies.pagination.prev.aria"/>">
                                <@spring.message "another_profile.vacancies.pagination.prev"/>
                            </a>
                        </li>
                    </#if>

                    <#list 0..(vacancyTotalPages - 1) as i>
                        <li class="page-item <#if i == vacancyCurrentPage>active</#if>">
                            <a class="page-link"
                               href="?page=${i}&size=6"
                               aria-label="<@spring.message "another_profile.vacancies.pagination.page.aria"/> ${i + 1}">
                                ${i + 1}
                            </a>
                        </li>
                    </#list>

                    <#if vacancyCurrentPage?? && (vacancyCurrentPage + 1) < vacancyTotalPages>
                        <li class="page-item">
                            <a class="page-link"
                               href="?page=${vacancyCurrentPage + 1}&size=6"
                               aria-label="<@spring.message "another_profile.vacancies.pagination.next.aria"/>">
                                <@spring.message "another_profile.vacancies.pagination.next"/>
                            </a>
                        </li>
                    </#if>
                </ul>
            </nav>
        </div>
    </#if>

    <#if (user.accountType == "applicant" || user.accountType == "admin") && resumeTotalPages?? && (resumeTotalPages > 1)>
        <div class="col-12 d-flex justify-content-center mt-4">
            <nav aria-label="<@spring.message "another_profile.resumes.pagination.label"/>">
                <ul class="pagination">
                    <#if resumeCurrentPage?? && (resumeCurrentPage > 0)>
                        <li class="page-item">
                            <a class="page-link"
                               href="?page=${resumeCurrentPage - 1}&size=6"
                               aria-label="<@spring.message "another_profile.resumes.pagination.prev.aria"/>">
                                <@spring.message "another_profile.resumes.pagination.prev"/>
                            </a>
                        </li>
                    </#if>

                    <#list 0..(resumeTotalPages - 1) as i>
                        <li class="page-item <#if i == resumeCurrentPage>active</#if>">
                            <a class="page-link"
                               href="?page=${i}&size=6"
                               aria-label="<@spring.message "another_profile.resumes.pagination.page.aria"/> ${i + 1}">
                                ${i + 1}
                            </a>
                        </li>
                    </#list>

                    <#if resumeCurrentPage?? && (resumeCurrentPage + 1) < resumeTotalPages>
                        <li class="page-item">
                            <a class="page-link"
                               href="?page=${resumeCurrentPage + 1}&size=6"
                               aria-label="<@spring.message "another_profile.resumes.pagination.next.aria"/>">
                                <@spring.message "another_profile.resumes.pagination.next"/>
                            </a>
                        </li>
                    </#if>
                </ul>
            </nav>
        </div>
    </#if>
</@main.layout>