<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="container py-4">
        <div class="card shadow-sm border-0">
            <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                <h3 class="mb-0">${vacancy.name}</h3>
                <span class="badge bg-light text-dark">
                    <#if vacancy.isActive><@spring.message "vacancy.status.active"/><#else><@spring.message "vacancy.status.inactive"/></#if>
                </span>
            </div>
            <div class="card-body">
                <div class="row g-4">
                    <div class="col-LG-8">
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary"><@spring.message "vacancy.description.title"/></h5>
                            <p class="text-muted">${vacancy.description!'<@spring.message "vacancy.description.none"/>'}</p>
                        </section>
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary"><@spring.message "vacancy.category.title"/></h5>
                            <p>${vacancy.categoryName!'<@spring.message "vacancy.category.none"/>'}</p>
                        </section>
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary"><@spring.message "vacancy.salary.title"/></h5>
                            <p class="fw-bold text-success fs-5">
                                ${vacancy.salary?string.currency}
                            </p>
                        </section>
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary"><@spring.message "vacancy.experience.title"/></h5>
                            <p><@spring.messageArgs code="vacancy.experience.range" args=[vacancy.expFrom, vacancy.expTo]/></p>
                        </section>
                        <#assign known = SPRING_SECURITY_CONTEXT??>
                        <#if known>
                            <#assign
                            userPr = SPRING_SECURITY_CONTEXT.authentication.principal
                            name = userPr.getUsername()
                            isApplicant = false
                            >
                            <#list userPr.authorities as auth>
                                <#if auth.authority == "APPLICANT">
                                    <#assign isApplicant = true>
                                </#if>
                            </#list>
                            <#if isApplicant>
                                <div class="mt-4">
                                    <a href="/chat/start/${vacancy.authorId}" class="btn btn-primary">
                                        <@spring.message "vacancy.contact.employer"/>
                                    </a>
                                </div>
                            </#if>
                        </#if>
                    </div>
                    <div class="col-lg-4">
                        <div class="card h-100 border-light shadow-sm">
                            <div class="card-body">
                                <h6 class="card-title fw-bold text-primary"><@spring.message "vacancy.details.title"/></h6>
                                <ul class="list-group list-group-flush">
                                    <li class="list-group-item d-flex justify-content-between">
                                        <span><@spring.message "vacancy.details.created"/></span>
                                        <span>${vacancy.createdDate.toLocalDate()}</span>
                                    </li>
                                    <li class="list-group-item d-flex justify-content-between">
                                        <span><@spring.message "vacancy.details.updated"/></span>
                                        <span>${vacancy.updateTime.toLocalDate()}</span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@main.layout>