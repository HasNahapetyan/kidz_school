const reset = document.getElementsByClassName('reset-popup')[0];
const verify = document.getElementsByClassName('verify-popup')[0];
let forgotPath = window.location.href.split('?')[1];
if (forgotPath) {
    let forgotName = forgotPath.split('=')[0]

    if (forgotName === "forgot") {
        verify.style.display = "block"
        let emailName = forgotPath.split('email=')[1]
        document.getElementById('otp-email').value = emailName;
    }
    if (forgotName === "verify") {
        reset.style.display = "block"
        let emailName = forgotPath.split('email=')[1]
        document.getElementById('reset-email').value = emailName;
    }

}