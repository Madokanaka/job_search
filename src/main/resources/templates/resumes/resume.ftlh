<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="container py-4">
        <div class="card shadow-sm border-0">
            <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                <h3 class="mb-0">${resume.name!'<@spring.message "resume.name.none"/>'}</h3>
                <span class="badge bg-light text-dark">
                    <#if resume.isActive><@spring.message "resume.status.active"/><#else><@spring.message "resume.status.inactive"/></#if>
                </span>
            </div>
            <div class="card-body">
                <div class="row g-4">
                    <div class="col-lg-8">
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary"><@spring.message "resume.category.title"/></h5>
                            <p>${resume.categoryName!'<@spring.message "resume.category.none"/>'}</p>
                        </section>
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary"><@spring.message "resume.salary.title"/></h5>
                            <p class="fw-bold text-success fs-5">
                                ${resume.salary?string.currency!'<@spring.message "resume.salary.none"/>'}
                            </p>
                        </section>
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary"><@spring.message "resume.contact.title"/></h5>
                            <ul class="list-unstyled">
                                <li>
                                    <strong><@spring.message "resume.contact.email"/>:</strong>
                                    <#if resume.contactEmail?? && (resume.contactEmail?has_content)>
                                        ${resume.contactEmail}
                                    <#else>
                                        <@spring.message "resume.contact.none"/>
                                    </#if>
                                </li>

                                <li>
                                    <strong><@spring.message "resume.contact.phone"/>:</strong>
                                    <#if resume.phoneNumber?? && (resume.phoneNumber?has_content)>
                                        ${resume.phoneNumber}
                                    <#else>
                                        <@spring.message "resume.contact.none"/>
                                    </#if>
                                </li>

                                <li><strong><@spring.message "resume.contact.linkedin"/>:</strong>
                                    <#if resume.linkedIn??>
                                        <a href="${resume.linkedIn}" target="_blank">${resume.linkedIn}</a>
                                    <#else>
                                        <@spring.message "resume.contact.none"/>
                                    </#if>
                                </li>
                                <li><strong><@spring.message "resume.contact.telegram"/>:</strong>
                                    <#if resume.telegram??>
                                        <a href="${resume.telegram}" target="_blank">${resume.telegram}</a>
                                    <#else>
                                        <@spring.message "resume.contact.none"/>
                                    </#if>
                                </li>
                                <li><strong><@spring.message "resume.contact.facebook"/>:</strong>
                                    <#if resume.facebook??>
                                        <a href="${resume.facebook}" target="_blank">${resume.facebook}</a>
                                    <#else>
                                        <@spring.message "resume.contact.none"/>
                                    </#if>
                                </li>
                            </ul>
                        </section>
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary"><@spring.message "resume.education.title"/></h5>
                            <#if resume.educationInfoList?? && resume.educationInfoList?size gt 0>
                                <div class="list-group">
                                    <#list resume.educationInfoList as education>
                                        <div class="list-group-item">
                                            <h6 class="mb-1">${education.institution!'<@spring.message "resume.education.none"/>'}</h6>
                                            <p class="mb-1"><strong><@spring.message "resume.education.program"/>:</strong> ${education.program!'<@spring.message "resume.education.none"/>'}</p>
                                            <p class="mb-1"><strong><@spring.message "resume.education.degree"/>:</strong> ${education.degree!'<@spring.message "resume.education.none"/>'}</p>
                                            <p class="text-muted">
                                                ${education.startDate!'?'} - ${education.endDate!'?'}
                                            </p>
                                        </div>
                                    </#list>
                                </div>
                            <#else>
                                <p class="text-muted"><@spring.message "resume.education.empty"/></p>
                            </#if>
                        </section>
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary"><@spring.message "resume.experience.title"/></h5>
                            <#if resume.workExperienceInfoList?? && resume.workExperienceInfoList?size gt 0>
                                <div class="list-group">
                                    <#list resume.workExperienceInfoList as experience>
                                        <div class="list-group-item">
                                            <h6 class="mb-1">${experience.companyName!'<@spring.message "resume.experience.none"/>'}</h6>
                                            <p class="mb-1"><strong><@spring.message "resume.experience.position"/>:</strong> ${experience.position!'<@spring.message "resume.experience.none"/>'}</p>
                                            <p class="mb-1"><strong><@spring.message "resume.experience.years"/>:</strong> ${experience.years!'?'} <@spring.message "resume.experience.years.unit"/></p>
                                            <p class="mb-1"><strong><@spring.message "resume.experience.responsibilities"/>:</strong> ${experience.responsibilities!'<@spring.message "resume.experience.none"/>'}</p>
                                        </div>
                                    </#list>
                                </div>
                            <#else>
                                <p class="text-muted"><@spring.message "resume.experience.empty"/></p>
                            </#if>
                        </section>
                    </div>
                    <div class="col-lg-4">
                        <div class="card h-100 border-light shadow-sm">
                            <div class="card-body">
                                <h6 class="card-title fw-bold text-primary"><@spring.message "resume.details.title"/></h6>
                                <ul class="list-group list-group-flush">
                                    <li class="list-group-item d-flex justify-content-between">
                                        <span><@spring.message "resume.details.created"/></span>
                                        <span>${resume.created_date.toLocalDate()!'<@spring.message "resume.details.none"/>'}</span>
                                    </li>
                                    <li class="list-group-item d-flex justify-content-between">
                                        <span><@spring.message "resume.details.updated"/></span>
                                        <span>${resume.update_time.toLocalDate()!'<@spring.message "resume.details.none"/>'}</span>
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