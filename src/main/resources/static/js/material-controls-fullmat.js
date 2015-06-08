function deleteMat(matId) {
    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/delete",
        async: false,
        error : function() {
            $.snackbar({
                content: "Не удалось удалить материал",
                style: "toast",
                timeout: 3000
            });
        }
    });
}

function takeTask(matId) {
    $("#taketask_" + matId).addClass("disabled");

    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/taketask",
        success : function() {
            $.snackbar({
                content: "Задание получено",
                style: "toast",
                timeout: 3000
            });
        },
        error : function() {
            $("#taketask_" + matId).removeClass("disabled");

            $.snackbar({
                content: "Не удалось взять задание на модерацию",
                style: "toast",
                timeout: 3000
            });
        }
    });
}

function accept(matId) {
    $("#accept_" + matId).addClass("disabled");
    $("#decline_" + matId).addClass("disabled");
    $("#edit_" + matId).addClass("disabled");

    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/accept",
        success : function() {
            $.snackbar({
                content: "Материал одобрен",
                style: "toast",
                timeout: 3000
            });
        },
        error : function() {
            $("#accept_" + matId).removeClass("disabled");
            $("#decline_" + matId).removeClass("disabled");
            $("#edit_" + matId).removeClass("disabled");

            $.snackbar({
                content: "Не удалось одобрить материал",
                style: "toast",
                timeout: 3000
            });
        }
    });
}

function decline(matId) {
    $("#accept_" + matId).addClass("disabled");
    $("#decline_" + matId).addClass("disabled");
    $("#edit_" + matId).addClass("disabled");

    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/decline",
        success : function() {
            $.snackbar({
                content: "Материал отклонен",
                style: "toast",
                timeout: 3000
            });
        },
        error : function() {
            $("#accept_" + matId).removeClass("disabled");
            $("#decline_" + matId).removeClass("disabled");
            $("#edit_" + matId).removeClass("disabled");

            $.snackbar({
                content: "Не удалось отклонить материал",
                style: "toast",
                timeout: 3000
            });
        }
    });
}

function toMain(matId) {
    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/tomain",
        success : function() {
            $("#taomain_" + matId).addClass("disabled");
        },
        error : function() {
            $.snackbar({
                content: "Не удалось отправить материал на главную",
                style: "toast",
                timeout: 3000
            });
        }
    });
}

function toArchive(matId) {
    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/toarchive",
        success : function() {
            $("#toarchive_" + matId).addClass("disabled");
        },
        error : function() {
            $.snackbar({
                content: "Не удалось отправить материал в архив",
                style: "toast",
                timeout: 3000
            });
        }
    });
}

function fav(matId) {
    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/fav",
        success : function() {
        },
        error : function() {
            $.snackbar({
                content: "Не удалось добавить материал в избранное",
                style: "toast",
                timeout: 3000
            });
        }
    });
}

function unfav(matId) {
    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/unfav",
        success : function() {
        },
        error : function() {
            $.snackbar({
                content: "Не удалось убрать материал из избранного",
                style: "toast",
                timeout: 3000
            });
        }
    });
}

function feature(matId) {
    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/feature",
        success : function() {
        },
        error : function() {
            $.snackbar({
                content: "Не удалось зафичерить материал",
                style: "toast",
                timeout: 3000
            });
        }
    });
}

function unfeature(matId) {
    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/unfeature",
        success : function() {
        },
        error : function() {
            $.snackbar({
                content: "Не удалось расфичерить материал",
                style: "toast",
                timeout: 3000
            });
        }
    });
}
