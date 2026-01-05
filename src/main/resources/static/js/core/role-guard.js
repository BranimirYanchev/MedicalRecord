// js/core/role-guard.js

// четем ролята веднъж
const role = localStorage.getItem("role") || "GUEST";

function applyRoleGuard() {
    // елементи с конкретна роля
    document.querySelectorAll("[data-role]").forEach(el => {
        const allowedRole = el.getAttribute("data-role");
        if (allowedRole !== role) {
            el.style.display = "none";
        } else {
            el.style.display = ""; // restore if преди е бил скрит
        }
    });

    // елементи скрити за някои роли
    document.querySelectorAll("[data-hide]").forEach(el => {
        const hiddenFor = el.getAttribute("data-hide").split(",");
        if (hiddenFor.includes(role)) {
            el.style.display = "none";
        } else {
            el.style.display = "";
        }
    });

    // елементи видими само за определени роли
    document.querySelectorAll("[data-show]").forEach(el => {
        const allowed = el.getAttribute("data-show").split(",");
        if (!allowed.includes(role)) {
            el.style.display = "none";
        } else {
            el.style.display = "";
        }
    });

    console.log("Role guard applied for:", role);
}

// пускаме го веднъж при зареждане на страницата
document.addEventListener("DOMContentLoaded", () => {
    applyRoleGuard();

    // logout бутон – да не гърми ако го няма
    const logoutBtn = document.querySelector("#logoutBtn, .logout-btn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", () => {
            localStorage.removeItem("role");
            localStorage.removeItem("token");
            localStorage.removeItem("egn");
            localStorage.removeItem("patientId");
            localStorage.removeItem("doctorId");
            window.location.href = "login.html";
        });
    }
});

// изнасяме функцията глобално, за да можеш да я викаш от други js файлове
window.applyRoleGuard = applyRoleGuard;
