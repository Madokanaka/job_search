<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="container mt-4">
        <h2 class="mb-4">Редактирование вакансии</h2>

        <form action="/vacancies/${vacancyDto.id}/edit" method="post" class="needs-validation" novalidate>
            <input type="hidden" name="_method" value="PUT" />

            <div class="card mb-4">
                <div class="card-body">
                    <div class="mb-3">
                        <label for="name" class="form-label">Название вакансии</label>
                        <@spring.formInput "vacancyDto.name" 'class="form-control" id="name" required' />
                        <@spring.showErrors "<br>" "error text-danger small" />
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label">Описание вакансии</label>
                        <@spring.formTextarea "vacancyDto.description" 'class="form-control" rows="3"' />
                        <@spring.showErrors "<br>" "error text-danger small" />
                    </div>

                    <div class="row">

                        <div class="col-md-6 mb-3">
                            <label for="categoryId" class="form-label">Категория</label>
                            <select class="form-select" id="categoryId" name="categoryId" required>
                                <#list categories as id, name>
                                    <option value="${id}"
                                            <#if (vacancyDto.categoryId)?? && vacancyDto.categoryId == id>selected</#if>>
                                        ${name}
                                    </option>
                                </#list>
                            </select>
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>

                        <div class="col-md-6 mb-3">
                            <label for="salary" class="form-label">Ожидаемый уровень оклада ($)</label>
                            <@spring.formInput "vacancyDto.salary" 'class="form-control" id="salary" type="number" min="0" required' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>

                    <div class="row">

                        <div class="col-md-6 mb-3">
                            <label for="expFrom" class="form-label">Опыт работы (от лет)</label>
                            <@spring.formInput "vacancyDto.expFrom" 'class="form-control" id="expFrom" type="number" min="0" required' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>

                        <div class="col-md-6 mb-3">
                            <label for="expTo" class="form-label">Опыт работы (до лет)</label>
                            <@spring.formInput "vacancyDto.expTo" 'class="form-control" id="expTo" type="number" min="0" required' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-check-label" for="isActive">Активна ли вакансия?</label>
                        <@spring.formCheckbox "vacancyDto.isActive" 'class="form-check-input" id="isActive"' />
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-end mb-5">
                <button type="submit" class="btn btn-primary btn-lg">Создать вакансию</button>
            </div>
        </form>
    </div>
</@main.layout>
