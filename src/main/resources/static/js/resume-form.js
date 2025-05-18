let workExperienceIndex = document.querySelectorAll('.work-experience-section').length;
let educationIndex = document.querySelectorAll('.education-section').length;

function addWorkExperience() {
    const container = document.getElementById('workExperienceContainer');
    const div = document.createElement('div');
    div.className = 'work-experience-section mb-3';
    div.innerHTML = `
        <div class="row">
            <div class="col-md-6 mb-3">
                <label class="form-label">${messages.workExperience.companyNameLabel}</label>
                <input type="text" class="form-control" name="workExperienceInfoList[${workExperienceIndex}].companyName">
                <div class="invalid-feedback">
                    ${messages.workExperience.companyNameNotBlank}
                </div>
            </div>
            <div class="col-md-6 mb-3">
                <label class="form-label">${messages.workExperience.positionLabel}</label>
                <input type="text" class="form-control" name="workExperienceInfoList[${workExperienceIndex}].position">
                <div class="invalid-feedback">
                    ${messages.workExperience.positionNotBlank}
                </div>
            </div>
        </div>
        <div class="mb-3">
            <label class="form-label">${messages.workExperience.responsibilitiesLabel}</label>
            <textarea class="form-control" name="workExperienceInfoList[${workExperienceIndex}].responsibilities" rows="3"></textarea>
        </div>
        <div class="mb-3">
            <label class="form-label">${messages.workExperience.yearsLabel}</label>
            <input type="number" class="form-control" name="workExperienceInfoList[${workExperienceIndex}].years" min="0">
            <div class="invalid-feedback">
                ${messages.workExperience.yearsNotNull}
            </div>
        </div>
        <button type="button" class="btn btn-danger btn-sm" onclick="removeSection(this, 'work-experience-section')">
            ${messages.remove}
        </button>
    `;
    container.appendChild(div);
    workExperienceIndex++;
    enableBootstrapValidation();
}

function addEducation() {
    const container = document.getElementById('educationContainer');
    const div = document.createElement('div');
    div.className = 'education-section mb-3';
    div.innerHTML = `
        <div class="row">
            <div class="col-md-6 mb-3">
                <label class="form-label">${messages.education.institutionLabel}</label>
                <input type="text" class="form-control" name="educationInfoList[${educationIndex}].institution">
                <div class="invalid-feedback">
                    ${messages.education.institutionNotBlank}
                </div>
            </div>
            <div class="col-md-6 mb-3">
                <label class="form-label">${messages.education.programLabel}</label>
                <input type="text" class="form-control" name="educationInfoList[${educationIndex}].program">
                <div class="invalid-feedback">
                    ${messages.education.programNotBlank}
                </div>
            </div>
            <div class="col-md-6 mb-3">
                <label class="form-label">${messages.education.degreeLabel}</label>
                <input type="text" class="form-control" name="educationInfoList[${educationIndex}].degree">
                <div class="invalid-feedback">
                    ${messages.education.degreeNotBlank}
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6 mb-3">
                <label class="form-label">${messages.education.startDateLabel}</label>
                <input type="date" class="form-control" name="educationInfoList[${educationIndex}].startDate">
                <div class="invalid-feedback">
                    ${messages.education.startDateNotNull}
                </div>
            </div>
            <div class="col-md-6 mb-3">
                <label class="form-label">${messages.education.endDateLabel}</label>
                <input type="date" class="form-control" name="educationInfoList[${educationIndex}].endDate">
                <div class="invalid-feedback">
                    ${messages.education.endDateNotNull}
                </div>
            </div>
        </div>
        <button type="button" class="btn btn-danger btn-sm" onclick="removeSection(this, 'education-section')">
            ${messages.remove}
        </button>
    `;
    container.appendChild(div);
    educationIndex++;
    enableBootstrapValidation();
}

function removeSection(button, sectionClass) {
    button.closest(`.${sectionClass}`).remove();
}

function enableBootstrapValidation() {
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
}