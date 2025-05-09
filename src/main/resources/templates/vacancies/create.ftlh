<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="container mt-4">
        <h2 class="mb-4"><@spring.message "vacancy.create.title"/></h2>

        <form action="/vacancies/create" method="post" class="needs-validation" novalidate>
            <#if _csrf??>
                <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                       value="${(_csrf.token)!'csrf-token'}"/>
            </#if>
            <div class="card mb-4">
                <div class="card-body">
                    <div class="mb-3">
                        <label for="name" class="form-label"><@spring.message "vacancy.create.name.label"/></label>
                        <@spring.formInput "vacancyDto.name" 'class="form-control" id="name" required' />
                        <@spring.showErrors "<br>" "error text-danger small" />
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label"><@spring.message "vacancy.create.description.label"/></label>
                        <@spring.formTextarea "vacancyDto.description" 'class="form-control" rows="3"' />
                        <@spring.showErrors "<br>" "error text-danger small" />
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="categoryId" class="form-label"><@spring.message "vacancy.create.category.label"/></label>
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
                            <label for="salary" class="form-label"><@spring.message "vacancy.create.salary.label"/></label>
                            <@spring.formInput "vacancyDto.salary" 'class="form-control" id="salary" type="number" min="0" required' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="expFrom" class="form-label"><@spring.message "vacancy.create.expFrom.label"/></label>
                            <@spring.formInput "vacancyDto.expFrom" 'class="form-control" id="expFrom" type="number" min="0" required' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>

                        <div class="col-md-6 mb-3">
                            <label for="expTo" class="form-label"><@spring.message "vacancy.create.expTo.label"/></label>
                            <@spring.formInput "vacancyDto.expTo" 'class="form-control" id="expTo" type="number" min="0" required' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-check-label" for="isActive"><@spring.message "vacancy.create.isActive.label"/></label>
                        <@spring.formCheckbox "vacancyDto.isActive" 'class="form-check-input" id="isActive"' />
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-end mb-5">
                <button type="submit" class="btn btn-primary btn-lg"><@spring.message "vacancy.create.button"/></button>
            </div>
        </form>
    </div>
</@main.layout>
