document.addEventListener("DOMContentLoaded", () => {

    const loginBtn = document.getElementById("loginBtn");
    const errorBox = document.getElementById("loginError");
    const passwordGroup = document.getElementById("passwordGroup");
    const patientCheckbox = document.getElementById("loginAsPatient");

    // 🔄 Toggle password visibility depending on login type
    patientCheckbox.addEventListener("change", () => {
        if (patientCheckbox.checked) {
            passwordGroup.style.display = "none";   // patient login does not use passwords
        } else {
            passwordGroup.style.display = "block";
        }
    });

    loginBtn.addEventListener("click", async () => {

        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value.trim();
        const loginAsPatient = patientCheckbox.checked;

        if (!username) {
            return showError("Моля, въведете потребителско име или ЕГН.");
        }

        // ============================
        // 🧑‍⚕️ PATIENT LOGIN (via EGN)
        // ============================
        if (loginAsPatient) {
            try {
                const res = await fetch(`/my/info?egn=${username}`);

                if (res.status !== 200) {
                    return showError("Невалидно ЕГН или пациентът не е намерен.");
                }

                const data = await res.json();

                // Save role + patient ID
                localStorage.setItem("role", "PATIENT");
                localStorage.setItem("patientId", data.id);
                localStorage.setItem("egn", username);

                window.location.href = "/pages/index.html";
                return;

            } catch (e) {
                return showError("Грешка при връзката със сървъра.");
            }
        }

        // ============================
        // 🛂 ADMIN / DOCTOR LOGIN
        // ============================
        if (!password) {
            return showError("Моля, въведете парола.");
        }

        const body = new URLSearchParams();
        body.append("username", username);
        body.append("password", password);

        try {
            const res = await fetch("/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: body.toString(),
                credentials: "include"
            });

            if (res.status === 200) {

                const data = await res.json();
                const role = data.role.replace("ROLE_", "");

                localStorage.setItem("role", role);
                localStorage.setItem("username", data.username);

                if (data.doctorId) localStorage.setItem("doctorId", data.doctorId);

                window.location.href = "/pages/index.html";

            } else {
                showError("Грешни потребителски данни.");
            }

        } catch (error) {
            showError("Грешка при връзката със сървъра.");
        }
    });


    function showError(msg) {
        errorBox.textContent = msg;
        errorBox.classList.remove("hidden");
        errorBox.style.opacity = 1;
        setTimeout(() => errorBox.style.opacity = 0, 2000);
    }
});
