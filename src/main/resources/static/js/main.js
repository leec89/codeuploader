"use strict";
(function() {

    const toggleButton = document.getElementsByClassName('toggle-button')[0];
    const navbarLinks = document.getElementsByClassName('navbar-links')[0];

    toggleButton.addEventListener('click', () => {
        navbarLinks.classList.toggle('active');
    })

    // code to make the logout form look like a link
    document.querySelector('#logout-link').addEventListener('click', e => {
        e.preventDefault();
        document.querySelector('#logout-form').submit();
        return false;
    });

    // code to create confirm step for delete
    if (document.querySelector('#delete-post-btn')) {
        document.querySelector('#delete-post-btn').addEventListener('click', e => {
            e.preventDefault();
            const willDelete = confirm('Are you sure you want to delete this post?');
            if (willDelete) {
                e.currentTarget.parentNode.submit();
            }
        });
    }

})();
