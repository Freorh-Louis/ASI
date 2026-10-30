// Activer les dropdowns Semantic UI
$('.ui.dropdown').dropdown();

// Récupérer l'utilisateur connecté depuis le localStorage
const user = JSON.parse(localStorage.getItem("user"));
const userId = user?.id;

if (!userId) {
    alert("Utilisateur non connecté");
    window.location.href = "login.html";
}

// Charger les infos utilisateur
function loadUserInfo() {
    fetch(`http://localhost:8088/users/${userId}`)
        .then(res => res.json())
        .then(user => {
            document.querySelector("#userNameId").textContent = user.name;
            document.querySelector("#userNameId").nextElementSibling.querySelector("span").textContent = user.wallet;
        })
        .catch(err => console.error("Erreur lors du chargement de l'utilisateur :", err));
}

// Charger les cartes de l'utilisateur
function loadUserCards() {
    fetch(`http://localhost:8088/users/${userId}/cards`)
        .then(res => res.json())
        .then(cardList => {
            const cardTemplate = document.querySelector("#row");
            const tableContent = document.querySelector("#tableContent");
            tableContent.innerHTML = "";

            for (const card of cardList) {
                const clone = document.importNode(cardTemplate.content, true);
                const row = clone.querySelector("tr");

                row.innerHTML = row.innerHTML
                    .replace(/{{img_src}}/g, card.imageUrl || "")
                    .replace(/{{name}}/g, card.name || card.id || "NoName")
                    .replace(/{{description}}/g, card.description || "No Description")
                    .replace(/{{element}}/g, card.element || "Unknown")
                    .replace(/{{hp}}/g, card.hp ?? "-")
                    .replace(/{{energy}}/g, card.energy ?? "-")
                    .replace(/{{defense}}/g, card.defense ?? "-")
                    .replace(/{{attack}}/g, card.attack ?? "-")
                    .replace(/{{price}}/g, (card.price ?? "-") + "$");

                row.querySelector(".sell-btn")?.addEventListener("click", function (e) {
                    e.stopPropagation();

                    fetch("http://localhost:8088/market/transaction", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded"
                        },
                        body: new URLSearchParams({
                            cardId: card.id,
                            prix: card.price ?? 10,
                            vendeurId: userId,
                            transactionType: "SELL"
                        })
                    })
                        .then(res => {
                            if (!res.ok) throw new Error("Erreur lors de la mise en vente");
                            return res.json();
                        })
                        .then(() => {
                            alert("Carte mise en vente !");
                            loadUserCards(); // mise à jour
                        })
                        .catch(err => {
                            console.error(err);
                            alert("Échec de la mise en vente");
                        });
                });

                row.addEventListener("click", function () {
                    $("#card").load("/part/card-full.html", function () {
                        setTimeout(() => {
                            $("#cardNameId").text(card.name || card.id || "NoName");
                            $("#cardImgId").attr("src", card.imageUrl || "");
                            $("#cardDescriptionId").val(card.description || "...");
                            $("#cardElementId").text(card.element || "Unknown");
                            $("#cardHPId").text("HP " + (card.hp ?? 0));
                            $("#cardEnergyId").text("Energy " + (card.energy ?? 0));
                            $("#cardAttackId").text("Attack " + (card.attack ?? 0));
                            $("#cardDefenceId").text("Defense " + (card.defense ?? 0));
                            $("#cardPriceId").text((card.price ?? 0) + "$");
                        }, 50);
                    });
                });

                tableContent.appendChild(row);
            }
        })
        .catch(err => console.error("Erreur chargement des cartes :", err));
}

// Charger les cartes en vente (pour market.html)
function loadMarketCards() {
    fetch("http://localhost:8088/market/sale")
        .then(res => res.json())
        .then(cardList => {
            const cardTemplate = document.querySelector("#row");
            const tableContent = document.querySelector("#tableContent");
            tableContent.innerHTML = "";

            for (const marketEntry of cardList) {
                const card = marketEntry.card;
                const clone = document.importNode(cardTemplate.content, true);
                const row = clone.querySelector("tr");

                row.innerHTML = row.innerHTML
                    .replace(/{{img_src}}/g, card.imgSrc || "")
                    .replace(/{{name}}/g, card.name || "NoName")
                    .replace(/{{description}}/g, card.description || "No Description")
                    .replace(/{{family_name}}/g, card.familyName || "Unknown")
                    .replace(/{{hp}}/g, card.hp ?? "-")
                    .replace(/{{energy}}/g, card.energy ?? "-")
                    .replace(/{{defense}}/g, card.defense ?? "-")
                    .replace(/{{attack}}/g, card.attack ?? "-")
                    .replace(/{{price}}/g, marketEntry.prix ?? "-");

                row.querySelector(".buy-btn")?.addEventListener("click", function () {
                    fetch("http://localhost:8088/market/transaction", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded"
                        },
                        body: new URLSearchParams({
                            cardId: card.id,
                            prix: marketEntry.prix,
                            vendeurId: userId,
                            transactionType: "BUY"
                        })
                    })
                        .then(res => {
                            if (!res.ok) throw new Error("Erreur lors de l'achat");
                            return res.json();
                        })
                        .then(() => {
                            alert("Achat réussi !");
                            loadMarketCards();
                        })
                        .catch(err => {
                            console.error(err);
                            alert("Erreur lors de l'achat");
                        });
                });

                tableContent.appendChild(row);
            }
        })
        .catch(err => console.error("Erreur chargement du market :", err));
}


// Initialisation
$(function () {
    loadUserInfo();

    // Détection automatique : si #card existe → vue utilisateur, sinon → market
    if ($("#card").length) {
        loadUserCards();
        $("#card").load("/part/card-full.html");
    } else {
        loadMarketCards();
    }
});
