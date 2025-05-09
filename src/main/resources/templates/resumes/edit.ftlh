<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="container mt-4">
        <h2 class="mb-4"><@spring.message "resume.edit.title" /></h2>

        <form action="/resumes/${resumeDto.id}/edit" method="post" class="needs-validation" novalidate>
            <input type="hidden" name="_method" value="PUT" />
            <#if _csrf??>
                <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}" value="${(_csrf.token)!'csrf-token'}"/>
            </#if>
            <div class="card mb-4">
                <div class="card-body">
                    <div class="mb-3">
                        <label for="name" class="form-label"><@spring.message "resume.edit.name.label" /></label>
                        <@spring.formInput "resumeDto.name" 'class="form-control" id="name" required' />
                        <@spring.showErrors "<br>" "error text-danger small" />
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="categoryId" class="form-label"><@spring.message "resume.edit.category.label" /></label>
                            <select class="form-select" id="categoryId" name="categoryId" required>
                                <#list categories as id, name>
                                    <option value="${id}" <#if resumeDto.categoryId == id>selected</#if>>${name}</option>
                                </#list>
                            </select>
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>

                        <div class="col-md-6 mb-3">
                            <label for="salary" class="form-label"><@spring.message "resume.edit.salary.label" /></label>
                            <@spring.formInput "resumeDto.salary" 'class="form-control" id="salary" type="number" min="0" required' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>
                </div>
            </div>

            <div class="card mb-4">
                <div class="card-header bg-light">
                    <h5 class="mb-0"><@spring.message "resume.edit.workExperience.title" /></h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label"><@spring.message "resume.edit.workExperience.companyName.label" /></label>
                            <@spring.formInput "resumeDto.workExperienceInfoList[0].companyName" 'class="form-control"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label"><@spring.message "resume.edit.workExperience.position.label" /></label>
                            <@spring.formInput "resumeDto.workExperienceInfoList[0].position" 'class="form-control"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label"><@spring.message "resume.edit.workExperience.responsibilities.label" /></label>
                        <@spring.formTextarea "resumeDto.workExperienceInfoList[0].responsibilities" 'class="form-control" rows="3"' />
                        <@spring.showErrors "<br>" "error text-danger small" />
                    </div>
                    <div class="mb-3">
                        <label class="form-label"><@spring.message "resume.edit.workExperience.years.label" /></label>
                        <@spring.formTextarea "resumeDto.workExperienceInfoList[0].years" 'class="form-control" rows="3"' />
                        <@spring.showErrors "<br>" "error text-danger small" />
                    </div>
                </div>
            </div>

            <div class="card mb-4">
                <div class="card-header bg-light">
                    <h5 class="mb-0"><@spring.message "resume.edit.education.title" /></h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label"><@spring.message "resume.edit.education.institution.label" /></label>
                            <@spring.formInput "resumeDto.educationInfoList[0].institution" 'class="form-control"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label"><@spring.message "resume.edit.education.program.label" /></label>
                            <@spring.formInput "resumeDto.educationInfoList[0].program" 'class="form-control"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label"><@spring.message "resume.edit.education.degree.label" /></label>
                            <@spring.formInput "resumeDto.educationInfoList[0].degree" 'class="form-control"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label"><@spring.message "resume.edit.education.startDate.label" /></label>
                            <@spring.formInput "resumeDto.educationInfoList[0].startDate" 'class="form-control" type="number"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label"><@spring.message "resume.edit.education.endDate.label" /></label>
                            <@spring.formInput "resumeDto.educationInfoList[0].endDate" 'class="form-control"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>
                </div>
            </div>

            <div class="card mb-4">
                <div class="card-header bg-light">
                    <h5 class="mb-0"><@spring.message "resume.edit.contactInfo.title" /></h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label"><@spring.message "resume.edit.contactInfo.email.label" /></label>
                            <@spring.formInput "resumeDto.contactEmail" 'class="form-control" type="email"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label"><@spring.message "resume.edit.contactInfo.phoneNumber.label" /></label>
                            <@spring.formInput "resumeDto.phoneNumber" 'class="form-control" type="tel" pattern="\\+?[0-9]{10,20}"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label class="form-label"><@spring.message "resume.edit.contactInfo.linkedIn.label" /></label>
                            <@spring.formInput "resumeDto.linkedIn" 'class="form-control" type="url"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label"><@spring.message "resume.edit.contactInfo.telegram.label" /></label>
                            <@spring.formInput "resumeDto.telegram" 'class="form-control"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label"><@spring.message "resume.edit.contactInfo.facebook.label" /></label>
                            <@spring.formInput "resumeDto.facebook" 'class="form-control" type="url"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-end mb-5">
                <button type="submit" class="btn btn-primary btn-lg"><@spring.message "resume.edit.button.save" /></button>
            </div>
        </form>
    </div>
</@main.layout>
