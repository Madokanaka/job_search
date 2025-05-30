document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('vacancyFilterForm');
    const sortSelect = document.getElementById('sort');

    function saveFilterAndSubmit() {
        if (sortSelect && form) {
            const selectedSort = sortSelect.value;
            localStorage.setItem('vacancySortFilter', selectedSort);
            form.submit();
        }
    }

    if (sortSelect && form) {
        const savedSort = localStorage.getItem('vacancySortFilter');
        if (savedSort && (savedSort === 'date' || savedSort === 'responses') && sortSelect.value !== savedSort) {
            sortSelect.value = savedSort;
            form.submit();
        }
    }

    document.querySelectorAll('.apply-btn').forEach(button => {
        button.addEventListener('click', async (event) => {
            const vacancyId = event.target.getAttribute('data-vacancy-id');
            const categoryId = event.target.getAttribute('data-category-id');
            const employerId = event.target.getAttribute('data-employer-id');

            const vacancyIdInput = document.getElementById('vacancyId');
            const employerIdInput = document.getElementById('employerId');
            if (vacancyIdInput && employerIdInput) {
                vacancyIdInput.value = vacancyId;
                employerIdInput.value = employerId;
            }

            try {
                const response = await fetch(`/api/resumes/by-category/${categoryId}`, {
                    headers: {
                        'Accept': 'application/json',
                        'X-CSRF-Token': document.querySelector('meta[name="_csrf"]')?.content || ''
                    }
                });
                if (!response.ok) {
                    throw new Error('Failed to fetch resumes');
                }

                const resumes = await response.json();
                console.log(resumes);
                const resumeSelect = document.getElementById('resumeSelect');
                if (resumeSelect) {
                    resumeSelect.innerHTML = `<option value="" disabled selected>${messages.resumePlaceholder}</option>`;
                    if (resumes.length === 0) {
                        resumeSelect.innerHTML += `<option value="" disabled>${messages.resumeEmpty}</option>`;
                    } else {
                        resumes.forEach(resume => {
                            const option = document.createElement('option');
                            option.value = resume.id;
                            option.textContent = resume.name;
                            resumeSelect.appendChild(option);
                        });
                    }
                }
            } catch (error) {
                console.error('Error fetching resumes:', error);
                alert(`${messages.error}: ${error.message}`);
            }
        });
    });

    const applyForm = document.getElementById('applyForm');
    if (applyForm) {
        applyForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            const formData = new FormData(event.target);

            const resumeId = parseInt(formData.get('resumeId'));
            if (isNaN(resumeId) || resumeId <= 0) {
                console.error('No valid resume selected');
                alert(`${window.messages?.error || 'Error'}: ${window.messages?.noResumeSelected || 'Please select a resume'}`);
                return;
            }

            const data = {
                resumeId: parseInt(formData.get('resumeId')),
                vacancyId: parseInt(formData.get('vacancyId')),
                employerId: parseInt(formData.get('employerId'))
            };

            try {
                const response = await fetch('/api/applications/respond', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-Token': document.querySelector('meta[name="_csrf"]')?.content || ''
                    },
                    body: JSON.stringify(data)
                });
                if (!response.ok) {
                    const errorData = await response.json();
                    throw new Error(errorData.error || 'Failed to submit application');
                }
                const result = await response.json();
                window.location.href = `/chat/${result.chatRoomId}`;
            } catch (error) {
                console.error('Error submitting application:', error);
                alert(`${window.messages?.error || 'Error'}: ${error.message}`);
            }
        });
    }
});

window.saveFilterAndSubmit = window.saveFilterAndSubmit || function() {
    const form = document.getElementById('vacancyFilterForm');
    const sortSelect = document.getElementById('sort');
    if (sortSelect && form) {
        const selectedSort = sortSelect.value;
        localStorage.setItem('vacancySortFilter', selectedSort);
        form.submit();
    }
};