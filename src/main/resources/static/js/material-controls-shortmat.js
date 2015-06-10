function deleteMat(matId) {
    bootbox.dialog({
        title: "Подтвердите действие",
        message: "Вы действительно хотите удалить материал?",
        buttons: {
            success: {
                label: "Да",
                className: "btn-success",
                callback: function() {
                    $("#delete_" + matId).addClass("disabled");
                    $("#edit_" + matId).addClass("disabled");
                    $("#toArchive_" + matId).addClass("disabled");
                    $("#toMain_" + matId).addClass("disabled");
                    $("#feature_" + matId).addClass("disabled");
                    $("#unfeature_" + matId).addClass("disabled");
                    $("#fav_" + matId).addClass("disabled");
                    $("#unfav_" + matId).addClass("disabled");

                    $.ajax({
                        type: "GET",
                        url : "/material/" + matId + "/delete",
                        success : function() {
                            $('#material_' + matId).hide('slow', function(){ $(this).remove(); });
                        },
                        error : function() {
                            $("#delete_" + matId).removeClass("disabled");
                            $("#edit_" + matId).removeClass("disabled");
                            $("#toArchive_" + matId).removeClass("disabled");
                            $("#toMain_" + matId).removeClass("disabled");
                            $("#feature_" + matId).removeClass("disabled");
                            $("#unfeature_" + matId).removeClass("disabled");
                            $("#fav_" + matId).removeClass("disabled");
                            $("#unfav_" + matId).removeClass("disabled");

                            $.snackbar({
                                content: "Не удалось удалить материал",
                                style: "toast",
                                timeout: 3000
                            });
                        }
                    });
                }
            },
            danger: {
                label: "Отмена",
                className: "btn-danger"
            }
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
    $("#toMain_" + matId).addClass("disabled");

    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/tomain",
        success : function() {
            $('#material_' + matId).hide('slow', function(){ $(this).remove(); });
        },
        error : function() {
            $("#toMain_" + matId).removeClass("disabled");

            $.snackbar({
                content: "Не удалось отправить материал на главную",
                style: "toast",
                timeout: 3000
            });
        }
    });
}

function toArchive(matId) {
    $("#toArchive_" + matId).addClass("disabled");

    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/toarchive",
        success : function() {
            $('#material_' + matId).hide('slow', function(){ $(this).remove(); });
        },
        error : function() {
            $("#toArchive_" + matId).removeClass("disabled");

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
    $("#feature_" + matId).addClass("disabled");

    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/feature",
        success : function() {
            $("#mat_title_" + matId).addClass("text-warning");
            $("#mat_title_" + matId).removeClass("text-indigo");

            $("#feature_" + matId).hide('medium');
            $("#unfeature_" + matId).removeClass("disabled");
            $("#unfeature_" + matId).show('medium');
        },
        error : function() {
            $("#feature_" + matId).removeClass("disabled");

            $.snackbar({
                content: "Не удалось зафичерить материал",
                style: "toast",
                timeout: 3000
            });
        }
    });
}

function unfeature(matId) {
    $("#unfeature_" + matId).addClass("disabled");

    $.ajax({
        type: "GET",
        url : "/material/" + matId + "/unfeature",
        success : function() {
            $("#mat_title_" + matId).addClass("text-indigo");
            $("#mat_title_" + matId).removeClass("text-warning");

            $("#unfeature_" + matId).hide('medium');
            $("#feature_" + matId).removeClass("disabled");
            $("#feature_" + matId).show('medium');
        },
        error : function() {
            $("#unfeature_" + matId).removeClass("disabled");

            $.snackbar({
                content: "Не удалось расфичерить материал",
                style: "toast",
                timeout: 3000
            });
        }
    });
}