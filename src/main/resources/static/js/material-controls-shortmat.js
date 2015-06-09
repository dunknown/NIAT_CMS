function deleteMat(matId) {
    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/delete",
        success : function() {
            $('#material_' + matId).hide('slow', function(){ $(this).remove(); });
        },
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
            $('#material_' + matId).hide('slow', function(){ $(this).remove(); });

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
            $('#material_' + matId).hide('slow', function(){ $(this).remove(); });

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
    $("#accept_" + matId).removeClass("disabled");
    $("#decline_" + matId).removeClass("disabled");
    $("#edit_" + matId).removeClass("disabled");

    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/decline",
        success : function() {
            $('#material_' + matId).hide('slow', function(){ $(this).remove(); });

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
            $('#material_' + matId).hide('slow', function(){ $(this).remove(); });
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
            $('#material_' + matId).hide('slow', function(){ $(this).remove(); });
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
    $("#fav_" + matId).addClass("disabled");

    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/addtofavs",
        success : function() {
            $("#fav_" + matId).hide('medium');
            $("#unfav_" + matId).show('medium');
            $("#unfav_" + matId).removeClass("disabled");
            $("#unfav_" + matId).show('medium');
        },
        error : function() {
            $("#fav_" + matId).removeClass("disabled");
            $.snackbar({
                content: "Не удалось добавить материал в избранное",
                style: "toast",
                timeout: 3000
            });
        }
    });
}

function unfav(matId) {
    $("#unfav_" + matId).addClass("disabled");

    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/unfav",
        success : function() {
            $("#unfav_" + matId).hide('medium');
            $("#fav_" + matId).show('medium');
            $("#fav_" + matId).removeClass("disabled");
            $("#fav_" + matId).show('medium');
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