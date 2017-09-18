window.addEventListener('load', function () {
    var urlCheckButton = document.getElementById('url-checker');
    urlCheckButton.addEventListener('click', function () {
        alert(location.href)
    })
});