<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="profile-info text-center">
        <h1 class="mb-5">
            Личный кабинет
            <#if user.accountType == "employer">
                Работодатель
            <#elseif user.accountType == "applicant">
                Соискатель
            <#else>
                Администратор
            </#if>
        </h1>
    </div>

    <div class="row mb-5">
        <div class="col-md-7 d-flex align-items-center">
            <div class="position-relative d-inline-block me-4">
                <form action="/profile/avatar" method="post" enctype="multipart/form-data" id="avatarForm">
                    <input type="hidden" name="userId" value="${user.id}"/>

                    <input type="file" name="file" id="avatarInput" accept="image/*" style="display: none;" onchange="this.form.submit()"/>

                    <label for="avatarInput" class="d-inline-block position-relative" style="cursor: pointer;">
                        <img src="${user.avatar?has_content?then('/api/images/' + user.avatar, 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png')}"
                             alt="Фото профиля"
                             class="border rounded-circle"
                             style="width: 100px; height: 100px; object-fit: cover;"/>

                        <span class="btn btn-sm btn-primary position-absolute top-0 end-0 rounded-circle"
                              style="transform: translate(50%, -50%);">
                            <i class="bi bi-pencil"></i>
                        </span>
                    </label>
                </form>
            </div>


            <div class="ms-4">
                <h3 class="mb-2">${user.getName()} ${user.getSurname()}</h3>
                <p class="text-muted mb-2">Возраст: ${user.getAge()}</p>
                <button class="btn btn-primary w-100 mb-2" data-bs-toggle="modal" data-bs-target="#editModal">
                    Редактировать
                </button>
            </div>
        </div>

        <div class="col-md-5 row d-flex justify-content-between">
            <#if user.accountType == "applicant">
                <div class="col-md-6">
                    <a href="/resumes/create" class="btn btn-outline-primary h-100 w-100 mb-2">
                        <span class="me-2">+</span> Создать резюме
                    </a>
                </div>
            </#if>

            <#if user.accountType == "employer">
                <div class="col-md-6">
                    <a href="/vacancies/create" class="btn btn-outline-primary h-100 w-100 mb-2">
                        <span class="me-2">+</span> Создать вакансию
                    </a>
                </div>
                <div class="col-md-6">
                    <button class="btn btn-outline-primary h-100 w-100 mb-2">
                        <span class="me-2"></span> Отклики
                    </button>
                </div>
            </#if>
        </div>
    </div>

    <#if user.accountType == "employer" || user.accountType == "admin">
        <h5 class="mb-3">Созданные вакансии:</h5>
        <div class="row g-3 mb-5">
            <#if vacancies?? && vacancies?size gt 0>
                <#list vacancies as vacancy>
                    <div class="col-md-4 mb-5">
                        <div class="card h-100">
                            <div class="card-body">
                                <h6 class="vacancy-title">${vacancy.name!'Название вакансии'}</h6>
                                <p class="text-muted">Дата обновления: ${vacancy.updatedDate!'дд/мм/гггг'}</p>
                            </div>
                        </div>
                        <div class="d-flex justify-content-between">
                            <button class="btn btn-outline-primary w-50">Обновить</button>
                            <a href="/vacancies/${vacancy.id}/edit" class="btn btn-primary w-50">Ред.</a>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="alert alert-info">Нет созданных вакансий</div>
            </#if>
        </div>
        <a class="btn btn-success m-2" href="/resumes">Список резюме</a>
    </#if>

    <#if user.accountType == "applicant" || user.accountType == "admin">
        <h5 class="mb-3 mt-4">Мои резюме:</h5>
        <div class="row g-3">
            <#if resumes?? && resumes?size gt 0>
                <#list resumes as resume>
                    <div class="col-md-4 mb-5">
                        <div class="card vacancy-card h-100">
                            <div class="card-body">
                                <h6 class="vacancy-title">${resume.name!'Название резюме'}</h6>
                                <p class="text-muted">Дата обновления: ${resume.update_time!'дд/мм/гггг'}</p>
                            </div>
                        </div>
                        <div class="d-flex justify-content-between">
                            <button class="btn btn-outline-primary w-50">Обновить</button>
                            <a href="/resumes/${resume.id}/edit" class="btn btn-primary w-50">Ред.</a>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="alert alert-info">Нет созданных резюме</div>
            </#if>
        </div>
    </#if>

    <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content">
                <form action="/profile" method="post">
                    <input type="hidden" name="_method" value="put">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editModalLabel">Редактировать профиль</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Закрыть"></button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group mb-3">
                            <label class="form-label" for="form3Example1cg">Имя</label>
                            <@spring.formInput "userEdit.name" 'id="form3Example1cg" class="form-control" value="${userEdit.name}"' />
                            <@spring.showErrors "<br>" "error text-danger" />
                        </div>
                        <div class="form-group mb-3">
                            <label class="form-label" for="form3Example2cg">Фамилия</label>
                            <@spring.formInput "userEdit.surname" 'id="form3Example2cg" class="form-control" value="${userEdit.surname}"' />
                            <@spring.showErrors "<br>" "error text-danger" />
                        </div>
                        <div class="form-group mb-3">
                            <label class="form-label" for="form3Example3cg">Возраст</label>
                            <@spring.formInput "userEdit.age" 'id="form3Example3cg" class="form-control" value="${userEdit.age}"' />
                            <@spring.showErrors "<br>" "error text-danger" />
                        </div>
                        <div class="form-group mb-3">
                            <label class="form-label" for="form3Example5cg">Телефон</label>
                            <@spring.formInput "userEdit.phoneNumber" 'id="form3Example5cg" class="form-control" value="${userEdit.phoneNumber}"' />
                            <@spring.showErrors "<br>" "error text-danger" />
                        </div>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
                        <button type="submit" class="btn btn-primary">Сохранить изменения</button>
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
</@main.layout>

