<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="container mt-4">
        <h2 class="mb-4"><@spring.message "resume.create.title"/></h2>

        <form action="/resumes/create" method="post" class="needs-validation" novalidate>
            <#if _csrf??>
                <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                       value="${(_csrf.token)!'csrf-token'}"/>
            </#if>
            <div class="card mb-4">
                <div class="card-body">
                    <div class="mb-3">
                        <label for="name" class="form-label"><@spring.message "resume.create.name.label"/></label>
                        <@spring.formInput "resumeDto.name" 'class="form-control" id="name" required' />
                        <@spring.showErrors "<br>" "error text-danger small" />
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="categoryId" class="form-label"><@spring.message "resume.create.category.label"/></label>
                            <select class="form-select" id="categoryId" name="categoryId" required>
                                <#list categories as id, name>
                                    <option value="${id}"
                                            <#if (resumeDto.categoryId)?? && resumeDto.categoryId == id>selected</#if> >
                                        ${name}
                                    </option>
                                </#list>
                            </select>
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>

                        <div class="col-md-6 mb-3">
                            <label for="salary" class="form-label"><@spring.message "resume.create.salary.label"/></label>
                            <@spring.formInput "resumeDto.salary" 'class="form-control" id="salary" type="number" min="0" required' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>
                </div>
            </div>

            <div class="card mb-4">
                <div class="card-header bg-light d-flex justify-content-between align-items-center">
                    <h5 class="mb-0"><@spring.message "resume.create.workExperience.title"/></h5>
                    <button type="button" class="btn btn-sm btn-outline-primary" onclick="window.addWorkExperience()">
                        <@spring.message "resume.create.workExperience.add"/>
                    </button>
                </div>
                <div class="card-body" id="workExperienceContainer">
                    <#if (resumeDto.workExperienceInfoList)??>
                        <#list resumeDto.workExperienceInfoList as workExp>
                            <div class="work-experience-section mb-3" data-index="${workExp?index}">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <h6><@spring.message "resume.create.workExperience.title"/> ${workExp?index + 1}</h6>
                                    <button type="button" class="btn btn-sm btn-outline-danger" onclick="window.removeSection(this, 'work-experience-section')">
                                        <@spring.message "resume.create.remove"/>
                                    </button>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label"><@spring.message "resume.create.workExperience.companyName.label"/></label>
                                        <@spring.formInput "resumeDto.workExperienceInfoList[${workExp?index}].companyName" 'class="form-control" required' />
                                        <@spring.showErrors "<br>" "error text-danger small" />
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label"><@spring.message "resume.create.workExperience.position.label"/></label>
                                        <@spring.formInput "resumeDto.workExperienceInfoList[${workExp?index}].position" 'class="form-control" required' />
                                        <@spring.showErrors "<br>" "error text-danger small" />
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label"><@spring.message "resume.create.workExperience.responsibilities.label"/></label>
                                    <@spring.formTextarea "resumeDto.workExperienceInfoList[${workExp?index}].responsibilities" 'class="form-control" rows="3"' />
                                    <@spring.showErrors "<br>" "error text-danger small" />
                                </div>
                                <div class="mb-3">
                                    <label class="form-label"><@spring.message "resume.create.workExperience.years.label"/></label>
                                    <@spring.formInput "resumeDto.workExperienceInfoList[${workExp?index}].years" 'class="form-control" type="number" min="0" required' />
                                    <@spring.showErrors "<br>" "error text-danger small" />
                                </div>
                            </div>
                        </#list>
                    </#if>
                </div>
            </div>

            <div class="card mb-4">
                <div class="card-header bg-light d-flex justify-content-between align-items-center">
                    <h5 class="mb-0"><@spring.message "resume.create.education.title"/></h5>
                    <button type="button" class="btn btn-sm btn-outline-primary" onclick="window.addEducation()">
                        <@spring.message "resume.create.education.add"/>
                    </button>
                </div>
                <div class="card-body" id="educationContainer">
                    <#if (resumeDto.educationInfoList)??>
                        <#list resumeDto.educationInfoList as edu>
                            <div class="education-section mb-3" data-index="${edu?index}">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <h6><@spring.message "resume.create.education.title"/> ${edu?index + 1}</h6>
                                    <button type="button" class="btn btn-sm btn-outline-danger" onclick="window.removeSection(this, 'education-section')">
                                        <@spring.message "resume.create.remove"/>
                                    </button>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label"><@spring.message "resume.create.education.institution.label"/></label>
                                        <@spring.formInput "resumeDto.educationInfoList[${edu?index}].institution" 'class="form-control" required' />
                                        <@spring.showErrors "<br>" "error text-danger small" />
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label"><@spring.message "resume.create.education.program.label"/></label>
                                        <@spring.formInput "resumeDto.educationInfoList[${edu?index}].program" 'class="form-control" required' />
                                        <@spring.showErrors "<br>" "error text-danger small" />
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label"><@spring.message "resume.create.education.degree.label"/></label>
                                        <@spring.formInput "resumeDto.educationInfoList[${edu?index}].degree" 'class="form-control" required' />
                                        <@spring.showErrors "<br>" "error text-danger small" />
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label"><@spring.message "resume.create.education.startDate.label"/></label>
                                        <@spring.formInput "resumeDto.educationInfoList[${edu?index}].startDate" 'class="form-control" type="date" required' />
                                        <@spring.showErrors "<br>" "error text-danger small" />
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label"><@spring.message "resume.create.education.endDate.label"/></label>
                                        <@spring.formInput "resumeDto.educationInfoList[${edu?index}].endDate" 'class="form-control" type="date" required' />
                                        <@spring.showErrors "<br>" "error text-danger small" />
                                    </div>
                                </div>
                            </div>
                        </#list>
                    </#if>
                </div>
            </div>

            <div class="card mb-4">
                <div class="card-header bg-light">
                    <h5 class="mb-0"><@spring.message "resume.create.contactInfo.title"/></h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label"><@spring.message "resume.create.contactInfo.email.label"/></label>
                            <@spring.formInput "resumeDto.contactEmail" 'class="form-control" type="email" required' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label"><@spring.message "resume.create.contactInfo.phoneNumber.label"/></label>
                            <@spring.formInput "resumeDto.phoneNumber" 'class="form-control" type="tel" pattern="\\+?[0-9]{10,20}"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label class="form-label"><@spring.message "resume.create.contactInfo.linkedIn.label"/></label>
                            <@spring.formInput "resumeDto.linkedIn" 'class="form-control" type="url"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label"><@spring.message "resume.create.contactInfo.telegram.label"/></label>
                            <@spring.formInput "resumeDto.telegram" 'class="form-control"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label"><@spring.message "resume.create.contactInfo.facebook.label"/></label>
                            <@spring.formInput "resumeDto.facebook" 'class="form-control" type="url"' />
                            <@spring.showErrors "<br>" "error text-danger small" />
                        </div>
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-end mb-5">
                <button type="submit" class="btn btn-primary btn-lg"><@spring.message "resume.create.button.save"/></button>
            </div>
        </form>
    </div>

    <script>
        const messages = {
            workExperience: {
                title: "<@spring.message 'resume.create.workExperience.title'?js_string/>",
                companyNameLabel: "<@spring.message 'resume.create.workExperience.companyName.label'?js_string/>",
                positionLabel: "<@spring.message 'resume.create.workExperience.position.label'?js_string/>",
                responsibilitiesLabel: "<@spring.message 'resume.create.workExperience.responsibilities.label'?js_string/>",
                yearsLabel: "<@spring.message 'resume.create.workExperience.years.label'?js_string/>",
                companyNameNotBlank: "<@spring.message 'WorkExperienceInfo.companyName.notBlank'?js_string/>",
                positionNotBlank: "<@spring.message 'WorkExperienceInfo.position.notBlank'?js_string/>",
                yearsNotNull: "<@spring.message 'WorkExperienceInfo.years.notNull'?js_string/>"
            },
            education: {
                title: "<@spring.message 'resume.create.education.title'?js_string/>",
                institutionLabel: "<@spring.message 'resume.create.education.institution.label'?js_string/>",
                programLabel: "<@spring.message 'resume.create.education.program.label'?js_string/>",
                degreeLabel: "<@spring.message 'resume.create.education.degree.label'?js_string/>",
                startDateLabel: "<@spring.message 'resume.create.education.startDate.label'?js_string/>",
                endDateLabel: "<@spring.message 'resume.create.education.endDate.label'?js_string/>",
                institutionNotBlank: "<@spring.message 'education.info.institution.notBlank'?js_string/>",
                programNotBlank: "<@spring.message 'education.info.program.notBlank'?js_string/>",
                degreeNotBlank: "<@spring.message 'education.info.degree.notBlank'?js_string/>",
                startDateNotNull: "<@spring.message 'education.info.startDate.notNull'?js_string/>",
                endDateNotNull: "<@spring.message 'education.info.endDate.notNull'?js_string/>"
            },
            remove: "<@spring.message 'resume.create.remove'?js_string/>"
        };
    </script>
    <script src="../../static/js/resume-form.js"></script>
</@main.layout>