<#import "layout.ftlh" as main>
<@main.layout ; spring>
    <div class="container mt-5">
        <h1 class="mb-4"><@spring.message "applications.title"/></h1>

        <#if applications?? && applications?size gt 0>
            <div class="row g-3">
                <#list applications as app>
                    <div class="col-md-6">
                        <div class="card h-100">
                            <div class="card-body">
                                <h5 class="card-title">
                                    <@spring.message "applications.application"/> #${app.id}
                                </h5>
                                <p class="card-text">
                                    <strong><@spring.message "applications.resume"/>:</strong>
                                    <a href="/resumes/${app.resumeId}" class="text-decoration-none">
                                        ${app.resumeName!'<@spring.message "applications.name.none"/>'}
                                    </a>
                                </p>
                                <p class="card-text">
                                    <strong><@spring.message "applications.vacancy"/>:</strong>
                                    <a href="/vacancies/${app.vacancyId}" class="text-decoration-none">
                                        ${app.vacancyName!'<@spring.message "applications.name.none"/>'}
                                    </a>
                                </p>
                                <p class="card-text">
                                    <strong><@spring.message "applications.applicant"/>:</strong>
                                    ${app.applicantName!'Unknown'}
                                </p>
                                <p class="card-text">
                                    <strong><@spring.message "applications.employer"/>:</strong>
                                    ${app.employerName!'Unknown'}
                                </p>
                                <#if userRole == "applicant">
                                    <a href="/chat/start/${app.employerId}"
                                       class="btn btn-primary btn-sm"
                                       aria-label="<@spring.message "applications.chat.employer.aria"/> ${app.employerName!'Unknown'}">
                                        <@spring.message "applications.chat.employer"/>
                                    </a>
                                <#elseif userRole == "employer">
                                    <a href="/chat/start/${app.applicantId}"
                                       class="btn btn-primary btn-sm"
                                       aria-label="<@spring.message "applications.chat.applicant.aria"/> ${app.applicantName!'Unknown'}">
                                        <@spring.message "applications.chat.applicant"/>
                                    </a>
                                </#if>
                            </div>
                        </div>
                    </div>
                </#list>
            </div>

            <#if totalPages?? && totalPages gt 1>
                <div class="col-12 d-flex justify-content-center mt-4">
                    <nav aria-label="<@spring.message "applications.pagination.label"/>">
                        <ul class="pagination">
                            <#if currentPage?? && currentPage gt 0>
                                <li class="page-item">
                                    <a class="page-link"
                                       href="/applications?page=${currentPage - 1}&size=${pageSize}"
                                       aria-label="<@spring.message "applications.pagination.prev.aria"/>">
                                        <@spring.message "applications.pagination.prev"/>
                                    </a>
                                </li>
                            </#if>

                            <#list 0..(totalPages - 1) as i>
                                <li class="page-item <#if i == currentPage>active</#if>">
                                    <a class="page-link"
                                       href="/applications?page=${i}&size=${pageSize}"
                                       aria-label="<@spring.message "applications.pagination.page.aria"/> ${i + 1}">
                                        ${i + 1}
                                    </a>
                                </li>
                            </#list>

                            <#if currentPage?? && (currentPage + 1) < totalPages>
                                <li class="page-item">
                                    <a class="page-link"
                                       href="/applications?page=${currentPage + 1}&size=${pageSize}"
                                       aria-label="<@spring.message "applications.pagination.next.aria"/>">
                                        <@spring.message "applications.pagination.next"/>
                                    </a>
                                </li>
                            </#if>
                        </ul>
                    </nav>
                </div>
            </#if>
        <#else>
            <div class="alert alert-info"><@spring.message "applications.empty"/></div>
        </#if>
    </div>
</@main.layout>