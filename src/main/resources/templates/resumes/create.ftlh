<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="container mt-4">
        <h2 class="mb-4">Создание резюме</h2>

        <form action="/resumes/create" method="post" class="needs-validation" novalidate>
            <div class="card mb-4">
                <div class="card-body">
                    <div class="mb-3">
                        <label for="name" class="form-label">Название резюме</label>
                        <@spring.formInput "resumeDto.name" 'class="form-control" id="name" required' />
                        <@spring.showErrors "<br>" "error text-danger small" />
                    </div>

                    <div class="row">

                        <div class="col-md-6 mb-3">
                            <label for="categoryId" class="form-label">Категория</label>
                            <select class="form-select" id="categoryId" name="categoryId" required>
                                <#list categories as id, name>
                                    <option value="${id}"
                                            <#if (resumeDto.categoryId)?? && resumeDto.categoryId == id>selected</#if>>
                                        ${name}
                                    </option>
                                </#list>
                            </select>
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>


                        <div class="col-md-6 mb-3">
                            <label for="salary" class="form-label">Ожидаемый уровень оклада ($)</label>
                            <@spring.formInput "resumeDto.salary" 'class="form-control" id="salary" type="number" min="0"" required' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>
                </div>
            </div>

            <div class="card mb-4">
                <div class="card-header bg-light">
                    <h5 class="mb-0">Опыт работы</h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Название компании</label>
                            <@spring.formInput "resumeDto.workExperienceInfoList[0].companyName" 'class="form-control"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Должность</label>
                            <@spring.formInput "resumeDto.workExperienceInfoList[0].position" 'class="form-control"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Обязанности</label>
                        <@spring.formTextarea "resumeDto.workExperienceInfoList[0].responsibilities" 'class="form-control" rows="3"' />
                        <@spring.showErrors "<br>" "error text-danger small" />
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Лет работы</label>
                        <@spring.formTextarea "resumeDto.workExperienceInfoList[0].years" 'class="form-control" rows="3"' />
                        <@spring.showErrors "<br>" "error text-danger small" />
                    </div>
                </div>
            </div>

            <div class="card mb-4">
                <div class="card-header bg-light">
                    <h5 class="mb-0">Образование</h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Учебное заведение</label>
                            <@spring.formInput "resumeDto.educationInfoList[0].institution" 'class="form-control"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Специальность</label>
                            <@spring.formInput "resumeDto.educationInfoList[0].program" 'class="form-control"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Уровень</label>
                            <@spring.formInput "resumeDto.educationInfoList[0].degree" 'class="form-control"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Год начала</label>
                            <@spring.formInput "resumeDto.educationInfoList[0].startDate" 'class="form-control" type="number"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Год окончания</label>
                            <@spring.formInput "resumeDto.educationInfoList[0].endDate" 'class="form-control"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>
                </div>
            </div>

            <div class="card mb-4">
                <div class="card-header bg-light">
                    <h5 class="mb-0">Контактная информация</h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Email</label>
                            <@spring.formInput "resumeDto.contactEmail" 'class="form-control" type="email"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Телефон</label>
                            <@spring.formInput "resumeDto.phoneNumber" 'class="form-control" type="tel" pattern="\\+?[0-9]{10,20}"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label class="form-label">LinkedIn</label>
                            <@spring.formInput "resumeDto.linkedIn" 'class="form-control" type="url"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Telegram</label>
                            <@spring.formInput "resumeDto.telegram" 'class="form-control"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Facebook</label>
                            <@spring.formInput "resumeDto.facebook" 'class="form-control" type="url"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-end mb-5">
                <button type="submit" class="btn btn-primary btn-lg">Сохранить резюме</button>
            </div>
        </form>
    </div>
</@main.layout>