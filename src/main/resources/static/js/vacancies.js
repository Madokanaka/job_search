document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('vacancyFilterForm');
    const sortSelect = document.getElementById('sort');

    window.saveFilterAndSubmit = function() {
        const selectedSort = sortSelect.value;
        localStorage.setItem('vacancySortFilter', selectedSort);
        form.submit();
    };

    const savedSort = localStorage.getItem('vacancySortFilter');
    if (savedSort && (savedSort === 'date' || savedSort === 'responses') && sortSelect.value !== savedSort) {
        sortSelect.value = savedSort;
        form.submit();
    }
});