export const API = {
    get: async (url) => {
        const res = await fetch(url, {
            credentials: "include"
        });

        const text = await res.text(); // <- винаги четем raw първо

        try {
            if (!res.ok) throw new Error(text);
            return JSON.parse(text);   // <- парсваме ръчно
        } catch (e) {
            console.error("API GET JSON PARSE FAIL:", text);
            throw new Error("API GET failed: " + url);
        }
    },

    post: async (url, body) => {
        const res = await fetch(url, {
            method: "POST",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });

        const text = await res.text();

        try {
            if (!res.ok) throw new Error(text);
            return JSON.parse(text);
        } catch (e) {
            console.error("API POST JSON PARSE FAIL:", text);
            throw new Error("API POST failed: " + url);
        }
    },

    put: async (url, body) => {
        const res = await fetch(url, {
            method: "PUT",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        });

        const text = await res.text();
        try {
            if (!res.ok) throw new Error(text);
            return JSON.parse(text);
        } catch (e) {
            console.error("API PUT JSON PARSE FAIL:", text);
            throw new Error("API PUT failed: " + url);
        }
    },

    delete: async (url) => {
        const res = await fetch(url, {
            method: "DELETE",
            credentials: "include"
        });

        const text = await res.text();

        if (!res.ok) {
            console.error("API DELETE FAIL:", text);
            // 🔥 ТУК е ключът
            throw new Error(text || "Неуспешно изтриване");
        }

        // по желание – success message
        return text;
    }
};

window.deleteEntity = async function(type, id) {
    if (!confirm("Наистина ли искате да изтриете този запис?")) return;

    try {
        await API.delete(`/${type}/delete/${id}`);
        location.reload(); // рефреш за да презареди таблицата
    } catch (e) {
        console.error(e);
        alert("Грешка при изтриването.");
    }
};

