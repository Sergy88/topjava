var ctx;
var form;

function successNoty(text) {
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + text,
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

var ctx;

// $(document).ready(function () {
$(function () {
    form = $('#mealForm');
    // https://stackoverflow.com/a/5064235/548473
    ctx = {
        ajaxUrl: "user/meals/",
        ajaxUrlFilter: "user/meals/filter",
        datatableApi: $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    };
    makeEditable();
});

function getFiltered() {
    $.get(ctx.ajaxUrlFilter, {
            "startDate": $('input[name="startDate"]').val(),
            "endDate": $('input[name="endDate"]').val(),
            "startTime": $('input[name="startTime"]').val(),
            "endTime": $('input[name="endTime"]').val()
        }
        , function (data) {
            ctx.datatableApi.clear().rows.add(data).draw();
        });
}

function resetFilter() {
    $('input[name="startDate"]').val(""),
        $('input[name="endDate"]').val(""),
        $('input[name="startTime"]').val(""),
        $('input[name="endTime"]').val("")
    getFiltered()
}
