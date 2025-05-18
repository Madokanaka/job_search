<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="profile-info text-center">
        <h1 class="mb-5">
            <@spring.message "profile.title"/>

            <#if user.accountType == "employer">
                <@spring.message "profile.type.employer"/>
            <#elseif user.accountType == "applicant">
                <@spring.message "profile.type.applicant"/>
            <#else>
                <@spring.message "profile.type.admin"/>
            </#if>
        </h1>
    </div>

    <div class="row mb-5">
        <div class="col-md-7 d-flex align-items-center">
            <div class="position-relative d-inline-block me-4">
                <form action="/profile/avatar" method="post" enctype="multipart/form-data" id="avatarForm">
                    <input type="hidden" name="userId" value="${user.id}"/>
                    <#if _csrf??>
                        <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                               value="${(_csrf.token)!'csrf-token'}"/>
                    </#if>

                    <input type="file" name="file" id="avatarInput" accept="image/*" style="display: none;"
                           onchange="this.form.submit()"/>

                    <label for="avatarInput" class="d-inline-block position-relative" style="cursor: pointer;">
                        <img src="${user.avatar?has_content?then('/api/images/' + user.avatar, 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png')}"
                             alt="<@spring.message "profile.avatar.alt"/>"
                             class="border rounded-circle"
                             style="width: 100px; height: 100px; object-fit: cover;"/>

                        <span class="btn btn-sm btn-primary position-absolute top-0 end-0 rounded-circle"
                              style="transform: translate(50%, -50%);"
                              aria-label="<@spring.message "profile.avatar.edit.aria"/>"
                              title="<@spring.message "profile.avatar.edit.title"/>">
                            <i class="bi bi-pencil"></i>
                        </span>
                    </label>
                </form>
            </div>

            <div class="ms-4">
                <h3 class="mb-2">${user.getName()} ${user.getSurname()}</h3>
                <p class="text-muted mb-2"><@spring.message "profile.age.label"/>: ${user.getAge()}</p>
                <button class="btn btn-primary w-100 mb-2"
                        data-bs-toggle="modal"
                        data-bs-target="#editModal"
                        aria-label="<@spring.message "profile.edit.button.aria"/>">
                    <@spring.message "profile.edit.button"/>
                </button>
            </div>
            <div class="language-selector ms-auto" style="max-width: 200px;">
                <form action="/profile/language" method="post" class="d-flex align-items-center">
                    <#if _csrf??>
                        <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                               value="${(_csrf.token)!'csrf-token'}"/>
                    </#if>

                    <label for="languageSelect" class="form-label me-2 mb-0">
                        <i class="bi bi-translate"></i>
                    </label>

                    <select id="languageSelect" name="language" class="form-select form-select-sm">
                        <option value="en"
                                <#if user.languagePreference?? && user.languagePreference == "en">selected</#if>>
                            <@spring.message "profile.language.en"/>
                        </option>
                        <option value="ru"
                                <#if user.languagePreference?? && user.languagePreference == "ru">selected</#if>>
                            <@spring.message "profile.language.ru"/>
                        </option>
                    </select>

                    <button type="submit" class="btn btn-sm btn-outline-primary ms-2">
                        <@spring.message "profile.language.submit"/>
                    </button>
                </form>
            </div>
        </div>

        <div class="col-md-5 row d-flex justify-content-between">
            <#if user.accountType == "applicant">
                <div class="col-md-6">
                    <a href="/resumes/create"
                       class="btn btn-outline-primary h-100 w-100 mb-2 d-flex align-items-center justify-content-center"
                       aria-label="<@spring.message "profile.create.resume.aria"/>">
                        <span class="me-2">+</span> <@spring.message "profile.create.resume"/>
                    </a>
                </div>
                <div class="col-md-6">
                    <button class="btn btn-outline-primary h-100 w-100 mb-2"
                            aria-label="<@spring.message "profile.responses.aria"/>">
                        <span class="me-2"></span> <@spring.message "profile.responses"/>
                    </button>
                </div>
            </#if>

            <#if user.accountType == "employer">
                <div class="col-md-6">
                    <a href="/vacancies/create"
                       class="btn btn-outline-primary h-100 w-100 mb-2 d-flex align-items-center justify-content-center"
                       aria-label="<@spring.message "profile.create.vacancy.aria"/>">
                        <span class="me-2">+</span> <@spring.message "profile.create.vacancy"/>
                    </a>
                </div>
                <div class="col-md-6">
                    <a href="/resumes"
                       class="btn btn-outline-primary h-100 w-100 mb-2 d-flex align-items-center justify-content-center"
                       aria-label="<@spring.message "profile.resume.list.aria"/>">
                        <span class="me-2">+</span> <@spring.message "profile.resume.list"/>
                    </a>
                </div>
            </#if>
        </div>
    </div>

    <#if user.accountType == "employer" || user.accountType == "admin">
        <h5 class="mb-3"><@spring.message "profile.vacancies.title"/></h5>
        <div class="row g-3 mb-5">
            <#if vacancies?? && vacancies?size gt 0>
                <#list vacancies as vacancy>
                    <div class="col-md-4 mb-5">
                        <div class="card h-100">
                            <a href="/vacancies/${vacancy.id}"
                               class="text-decoration-none"
                               aria-label="<@spring.message "profile.vacancies.view.aria"/> ${vacancy.name!'<@spring.message "profile.vacancies.name.none"/>'}">
                                <div class="card-body">
                                    <h6 class="vacancy-title">${vacancy.name!'<@spring.message "profile.vacancies.name.none"/>'}</h6>
                                    <p class="text-muted"><@spring.message "profile.vacancies.updated"/>
                                        : ${vacancy.updateTime.toLocalDate()!'<@spring.message "profile.date.none"/>'}</p>
                                </div>
                            </a>
                        </div>
                        <div class="d-flex justify-content-between">
                            <a href="/profile"
                               class="btn btn-outline-primary w-50"
                               aria-label="<@spring.message "profile.vacancies.refresh.aria"/> ${vacancy.name!'<@spring.message "profile.vacancies.name.none"/>'}">
                                <@spring.message "profile.vacancies.refresh"/>
                            </a>
                            <a href="/vacancies/${vacancy.id}/edit"
                               class="btn btn-primary w-50"
                               aria-label="<@spring.message "profile.vacancies.edit.aria"/> ${vacancy.name!'<@spring.message "profile.vacancies.name.none"/>'}">
                                <@spring.message "profile.vacancies.edit"/>
                            </a>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="alert alert-info"><@spring.message "profile.vacancies.empty"/></div>
            </#if>
        </div>
    </#if>

    <#if user.accountType == "applicant" || user.accountType == "admin">
        <h5 class="mb-3 mt-4"><@spring.message "profile.resumes.title"/></h5>
        <div class="row g-3">
            <#if resumes?? && resumes?size gt 0>
                <#list resumes as resume>
                    <div class="col-md-4 mb-5">
                        <div class="card vacancy-card h-100">
                            <a href="/resumes/${resume.id}"
                               class="text-decoration-none"
                               aria-label="<@spring.message "profile.resumes.view.aria"/> ${resume.name!'<@spring.message "profile.resumes.name.none"/>'}">
                                <div class="card-body">
                                    <h6 class="vacancy-title">${resume.name!'<@spring.message "profile.resumes.name.none"/>'}</h6>
                                    <p class="text-muted"><@spring.message "profile.resumes.updated"/>
                                        : ${resume.update_time.toLocalDate()!'<@spring.message "profile.date.none"/>'}</p>
                                </div>
                            </a>
                        </div>
                        <div class="d-flex justify-content-between">
                            <a href="/profile"
                               class="btn btn-outline-primary w-50"
                               aria-label="<@spring.message "profile.resumes.refresh.aria"/> ${resume.name!'<@spring.message "profile.resumes.name.none"/>'}">
                                <@spring.message "profile.resumes.refresh"/>
                            </a>
                            <a href="/resumes/${resume.id}/edit"
                               class="btn btn-primary w-50"
                               aria-label="<@spring.message "profile.resumes.edit.aria"/> ${resume.name!'<@spring.message "profile.resumes.name.none"/>'}">
                                <@spring.message "profile.resumes.edit"/>
                            </a>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="alert alert-info"><@spring.message "profile.resumes.empty"/></div>
            </#if>
        </div>
    </#if>

    <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content">
                <form action="/profile" method="post">
                    <#if _csrf??>
                        <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                               value="${(_csrf.token)!'csrf-token'}"/>
                    </#if>
                    <input type="hidden" name="_method" value="put">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editModalLabel"><@spring.message "profile.edit.modal.title"/></h5>
                        <button type="button"
                                class="btn-close"
                                data-bs-dismiss="modal"
                                aria-label="<@spring.message "profile.edit.modal.close.aria"/>"></button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group mb-3">
                            <label class="form-label"
                                   for="form3Example1cg"><@spring.message "profile.edit.name.label"/></label>
                            <@spring.formInput "userEdit.name" 'id="form3Example1cg" class="form-control" value="${userEdit.name}"' />
                            <@spring.showErrors "<br>" "error text-danger" />
                        </div>
                        <div class="form-group mb-3">
                            <label class="form-label"
                                   for="form3Example2cg"><@spring.message "profile.edit.surname.label"/></label>
                            <@spring.formInput "userEdit.surname" 'id="form3Example2cg" class="form-control" value="${userEdit.surname}"' />
                            <@spring.showErrors "<br>" "error text-danger" />
                        </div>
                        <div class="form-group mb-3">
                            <label class="form-label"
                                   for="form3Example3cg"><@spring.message "profile.edit.age.label"/></label>
                            <@spring.formInput "userEdit.age" 'id="form3Example3cg" class="form-control" value="${userEdit.age}"' />
                            <@spring.showErrors "<br>" "error text-danger" />
                        </div>
                        <div class="form-group mb-3">
                            <label class="form-label"
                                   for="form3Example5cg"><@spring.message "profile.edit.phone.label"/></label>
                            <@spring.formInput "userEdit.phoneNumber" 'id="form3Example5cg" class="form-control" value="${userEdit.phoneNumber}"' />
                            <@spring.showErrors "<br>" "error text-danger" />
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button"
                                class="btn btn-secondary"
                                data-bs-dismiss="modal">
                            <@spring.message "profile.edit.modal.cancel"/>
                        </button>
                        <button type="submit"
                                class="btn btn-primary">
                            <@spring.message "profile.edit.modal.save"/>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <#if showEditModal?? && showEditModal>
        <script>
            const editModal = new bootstrap.Modal(document.getElementById('editModal'));
            editModal.show();
        </script>
    </#if>

    <#if (user.accountType == "employer" || user.accountType == "admin") && vacancyTotalPages?? && (vacancyTotalPages > 1)>
        <div class="col-12 d-flex justify-content-center mt-4">
            <nav aria-label="<@spring.message "profile.vacancies.pagination.label"/>">
                <ul class="pagination">
                    <#if vacancyCurrentPage?? && (vacancyCurrentPage > 0)>
                        <li class="page-item">
                            <a class="page-link"
                               href="?page=${vacancyCurrentPage - 1}&size=6"
                               aria-label="<@spring.message "profile.vacancies.pagination.prev.aria"/>">
                                <@spring.message "profile.vacancies.pagination.prev"/>
                            </a>
                        </li>
                    </#if>

                    <#list 0..(vacancyTotalPages - 1) as i>
                        <li class="page-item <#if i == vacancyCurrentPage>active</#if>">
                            <a class="page-link"
                               href="?page=${i}&size=6"
                               aria-label="<@spring.message "profile.vacancies.pagination.page.aria"/> ${i + 1}">
                                ${i + 1}
                            </a>
                        </li>
                    </#list>

                    <#if vacancyCurrentPage?? && (vacancyCurrentPage + 1) < vacancyTotalPages>
                        <li class="page-item">
                            <a class="page-link"
                               href="?page=${vacancyCurrentPage + 1}&size=6"
                               aria-label="<@spring.message "profile.vacancies.pagination.next.aria"/>">
                                <@spring.message "profile.vacancies.pagination.next"/>
                            </a>
                        </li>
                    </#if>
                </ul>
            </nav>
        </div>
    </#if>

    <#if (user.accountType == "applicant" || user.accountType == "admin") && resumeTotalPages?? && (resumeTotalPages > 1)>
        <div class="col-12 d-flex justify-content-center mt-4">
            <nav aria-label="<@spring.message "profile.resumes.pagination.label"/>">
                <ul class="pagination">
                    <#if resumeCurrentPage?? && (resumeCurrentPage > 0)>
                        <li class="page-item">
                            <a class="page-link"
                               href="?page=${resumeCurrentPage - 1}&size=6"
                               aria-label="<@spring.message "profile.resumes.pagination.prev.aria"/>">
                                <@spring.message "profile.resumes.pagination.prev"/>
                            </a>
                        </li>
                    </#if>

                    <#list 0..(resumeTotalPages - 1) as i>
                        <li class="page-item <#if i == resumeCurrentPage>active</#if>">
                            <a class="page-link"
                               href="?page=${i}&size=6"
                               aria-label="<@spring.message "profile.resumes.pagination.page.aria"/> ${i + 1}">
                                ${i + 1}
                            </a>
                        </li>
                    </#list>

                    <#if resumeCurrentPage?? && (resumeCurrentPage + 1) < resumeTotalPages>
                        <li class="page-item">
                            <a class="page-link"
                               href="?page=${resumeCurrentPage + 1}&size=6"
                               aria-label="<@spring.message "profile.resumes.pagination.next.aria"/>">
                                <@spring.message "profile.resumes.pagination.next"/>
                            </a>
                        </li>
                    </#if>
                </ul>
            </nav>
        </div>
    </#if>
</@main.layout>