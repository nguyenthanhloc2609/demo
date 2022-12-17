jQuery(document).ready(function () {
    var elems = Array.from(document.getElementsByClassName("money-input"));

    elems.forEach(elem => {
        elem.addEventListener("keydown", function (event) {
            var key = event.which;
            if (key != 8 && key < 48 || (key > 57 && key < 96) || key > 105) event.preventDefault();
        });
        elem.addEventListener("keyup", function (event) {
            var value = this.value.replace(/\./g, "");
            this.dataset.currentValue = parseInt(value);
            var caret = value.length - 1;

            while ((caret - 3) > -1) {
                caret -= 3;
                value = value.split('');
                value.splice(caret + 1, 0, ".");

                value = value.join('');
            }
            this.value = value;
        });
    })
});