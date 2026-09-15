(() => {
    const search = document.querySelector("[data-filter-input]");
    const authorSelect = document.querySelector("[data-filter-select]");
    const cards = document.querySelectorAll("[data-card-grid] [data-book]");
    const empty = document.querySelector("[data-empty-filter]");
    const resultCount = document.querySelector("[data-result-count]");

    const matches = (card) => {
        const query = (search?.value || "").trim().toLowerCase();
        const authorFilter = authorSelect?.value || "all";
        const haystack = [
            card.dataset.title,
            card.dataset.author,
            card.dataset.genre,
            card.dataset.isbn
        ].join(" ").toLowerCase();
        const textOk = query === "" || haystack.includes(query);
        const authorOk = authorFilter === "all" || card.dataset.author === authorFilter;
        return textOk && authorOk;
    };

    const apply = () => {
        let visible = 0;
        cards.forEach((card) => {
            const show = matches(card);
            card.classList.toggle("is-hidden", !show);
            if (show) {
                visible += 1;
            }
        });
        if (empty) {
            empty.hidden = visible !== 0 || cards.length === 0;
        }
        if (resultCount) {
            resultCount.textContent = visible + (visible === 1 ? " book" : " books");
        }
    };

    apply();
    search?.addEventListener("input", apply);
    authorSelect?.addEventListener("change", apply);

    document.querySelectorAll("form[data-confirm]").forEach((form) => {
        form.addEventListener("submit", (event) => {
            if (!window.confirm(form.dataset.confirm)) {
                event.preventDefault();
            }
        });
    });

    const toast = document.querySelector("[data-toast]");
    if (toast) {
        window.setTimeout(() => toast.remove(), 4200);
    }
})();
