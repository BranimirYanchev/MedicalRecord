    import { API } from "./core/api.js";

    document.addEventListener("DOMContentLoaded", loadDoctors);

    async function loadDoctors() {
        const doctors = await API.get("/doctor/all");
        const table = document.getElementById("doctorsTable");
        table.innerHTML = "";

        doctors.forEach(d => {
            table.innerHTML += `
                <tr>
                    <td>${d.id}</td>
                    <td>${d.name}</td>
                    <td>${d.specialty}</td>
                    <td>${d.personalDoctor ? "Да" : "Не"}</td>
                    <td>
                        <button class="btn-small btn-primary" data-show="ADMIN" onclick="editDoctor(${d.id})">Редактирай</button>
                        <button class="btn-small btn-danger" data-show="ADMIN" onclick="deleteEntity('doctor', ${d.id})">Изтрий</button>
                    </td>
                </tr>
            `;
        });
    }

    // EDIT → попълва полетата вдясно
    window.editDoctor = async function(id) {
        const d = await API.get(`/doctor/${id}`);

        document.getElementById("editId").value = id;
        document.getElementById("dName").value = d.name;
        document.getElementById("dSpec").value = d.specialty;
        document.getElementById("dPD").checked = !!d.personalDoctor;

        // password: НЕ можем да я извадим от бекенда → оставяме празно
        const passEl = document.getElementById("dPassword");
        if (passEl) {
            passEl.value = "";
            passEl.placeholder = "Остави празно (без промяна)";
            passEl.required = false; // при update не е задължително
        }

        document.querySelector("#addDoctorForm button[type='submit']").textContent = "Запази";
    };

    // SUBMIT = ADD or UPDATE
    document.getElementById("addDoctorForm").addEventListener("submit", async (e) => {
        e.preventDefault();

        const id = document.getElementById("editId").value;

        const body = {
            name: document.getElementById("dName").value.trim(),
            specialty: document.getElementById("dSpec").value.trim(),
            personalDoctor: document.getElementById("dPD").checked
        };

        const passEl = document.getElementById("dPassword");
        if (passEl) {
            const pwd = passEl.value.trim();

            // при ADD е задължителна
            if (!id && !pwd) return alert("Моля въведи парола за доктора!");
            // при UPDATE е optional
            if (pwd) body.password = pwd;
        }

        if (id) {
            await API.put(`/doctor/update/${id}`, body);
        } else {
            await API.post(`/doctor/add`, body);
        }

        resetDoctorForm();
        loadDoctors();
    });

    window.deleteEntity = async function(type, id) {
        if (!confirm("Наистина ли искате да изтриете този запис?")) return;

        try {
            await API.delete(`/${type}/delete/${id}`);
            loadDoctors();
        } catch (e) {
            // тук ще хванем текста от backend-а
            alert(e.message || "Неуспешно изтриване.");
        }
    };


    // RESET
    function resetDoctorForm() {
        document.getElementById("addDoctorForm").reset();
        document.getElementById("editId").value = "";

        const passEl = document.getElementById("dPassword");
        if (passEl) {
            passEl.placeholder = "Парола за доктора";
            passEl.required = true; // пак става задължително за ADD
        }

        document.querySelector("#addDoctorForm button[type='submit']").textContent = "Добави";
    }
