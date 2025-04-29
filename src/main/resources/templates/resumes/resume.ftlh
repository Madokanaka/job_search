<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="container py-4">
        <div class="card shadow-sm border-0">
            <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                <h3 class="mb-0">${resume.name!'Без названия'}</h3>
                <span class="badge bg-light text-dark">
                    <#if resume.isActive>Активно<#else>Неактивно</#if>
                </span>
            </div>
            <div class="card-body">
                <div class="row g-4">
                    <div class="col-lg-8">
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary">Категория</h5>
                            <p>${resume.categoryName!'Не указана'}</p>
                        </section>
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary">Ожидаемая зарплата</h5>
                            <p class="fw-bold text-success fs-5">
                                ${resume.salary?string.currency!'Не указана'}
                            </p>
                        </section>
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary">Контактная информация</h5>
                            <ul class="list-unstyled">
                                <li><strong>Email:</strong> ${resume.contactEmail!'Не указан'}</li>
                                <li><strong>Телефон:</strong> ${resume.phoneNumber!'Не указан'}</li>
                                <li><strong>LinkedIn:</strong>
                                    <#if resume.linkedIn??>
                                        <a href="${resume.linkedIn}" target="_blank">${resume.linkedIn}</a>
                                    <#else>
                                        Не указан
                                    </#if>
                                </li>
                                <li><strong>Telegram:</strong>
                                    <#if resume.telegram??>
                                        <a href="${resume.telegram}" target="_blank">${resume.telegram}</a>
                                    <#else>
                                        Не указан
                                    </#if>
                                </li>
                                <li><strong>Facebook:</strong>
                                    <#if resume.facebook??>
                                        <a href="${resume.facebook}" target="_blank">${resume.facebook}</a>
                                    <#else>
                                        Не указан
                                    </#if>
                                </li>
                            </ul>
                        </section>
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary">Образование</h5>
                            <#if resume.educationInfoList?? && resume.educationInfoList?size gt 0>
                                <div class="list-group">
                                    <#list resume.educationInfoList as education>
                                        <div class="list-group-item">
                                            <h6 class="mb-1">${education.institution!'Не указано'}</h6>
                                            <p class="mb-1"><strong>Программа:</strong> ${education.program!'Не указано'}</p>
                                            <p class="mb-1"><strong>Степень:</strong> ${education.degree!'Не указано'}</p>
                                            <p class="text-muted">
                                                ${education.startDate!'?'} -
                                                ${education.endDate!'?'}
                                            </p>
                                        </div>
                                    </#list>
                                </div>
                            <#else>
                                <p class="text-muted">Информация об образовании отсутствует</p>
                            </#if>
                        </section>
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary">Опыт работы</h5>
                            <#if resume.workExperienceInfoList?? && resume.workExperienceInfoList?size gt 0>
                                <div class="list-group">
                                    <#list resume.workExperienceInfoList as experience>
                                        <div class="list-group-item">
                                            <h6 class="mb-1">${experience.companyName!'Не указано'}</h6>
                                            <p class="mb-1"><strong>Должность:</strong> ${experience.position!'Не указано'}</p>
                                            <p class="mb-1"><strong>Стаж:</strong> ${experience.years!'?'} лет</p>
                                            <p class="mb-1"><strong>Обязанности:</strong> ${experience.responsibilities!'Не указано'}</p>
                                        </div>
                                    </#list>
                                </div>
                            <#else>
                                <p class="text-muted">Информация об опыте работы отсутствует</p>
                            </#if>
                        </section>
                    </div>
                    <div class="col-lg-4">
                        <div class="card h-100 border-light shadow-sm">
                            <div class="card-body">
                                <h6 class="card-title fw-bold text-primary">Детали</h6>
                                <ul class="list-group list-group-flush">
                                    <li class="list-group-item d-flex justify-content-between">
                                        <span>Создано:</span>
                                        <span>${resume.created_date.toLocalDate()!'Не указано'}</span>
                                    </li>
                                    <li class="list-group-item d-flex justify-content-between">
                                        <span>Обновлено:</span>
                                        <span>${resume.update_time.toLocalDate()!'Не указано'}</span>
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