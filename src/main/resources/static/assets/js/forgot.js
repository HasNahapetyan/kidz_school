const forgot = document.getElementsByClassName('forgot-password-popup')[0];
const btn = document.getElementById('forgot-button');
const popup = document.getElementsByClassName('forgot-popup-child')[0];
const sendEmail = document.getElementById('send-forgot-email');

popup.addEventListener('click', function (e) {
    e.stopPropagation()
})

btn.addEventListener('click',function (){
    forgot.style.display = "block"
})

forgot.addEventListener('click',function (){
    forgot.style.display = 'none';
})

// sendEmail.addEventListener('click',function (e) {
//     e.preventDefault();
//     let email = document.getElementById('forgot-email').value;
//     let form = new FormData()
//     form.append("email",email)
//     const xhttp = new XMLHttpRequest();
//     xhttp.onload = function() {
//         console.log(this.responseText)
//     }
//     xhttp.open("POST", "http://localhost:8080/sign-up-or-login/forgot");
//     xhttp.send(form);
// })
//
