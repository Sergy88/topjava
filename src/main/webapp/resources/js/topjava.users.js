var ctx;
var form;

// $(document).ready(function () {
$(function () {
    form = $('#detailsForm');
    // https://stackoverflow.com/a/5064235/548473
    ctx = {
        ajaxUrl: "admin/users/",
        ajaxChangeEnabled: "admin/users/enabled/",
        datatableApi: $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
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
    setChangeEnabled()
});

function setChangeEnabled() {
    $("input:checkbox").change(function () {
            $.get(ctx.ajaxChangeEnabled + $(this).parent().parent().attr("id"),
                {enabled: $(this).is(":checked")},
                function (bool) {
                    $(this).attr("checked", bool);
                }
            )
        }
    )
}
