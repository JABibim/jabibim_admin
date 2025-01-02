const select = (el, all = false) => {
    el = el.trim()
    if (all) {
        return [...document.querySelectorAll(el)]
    } else {
        return document.querySelector(el)
    }
}

function dateFormat(date) {
    date = new Date(date.replaceAll("월", ","));


    let dateFormat = date.getFullYear() +
        '-' + ((date.getMonth() + 1) <= 9 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1)) +
        '-' + ((date.getDate()) <= 9 ? "0" + (date.getDate()) : (date.getDate()));
    return dateFormat;
}

function getResignHistAjax() {

    $.ajax({
        url: "resignlist",
        type: "post",
        dataType: "json",
        success: function (data) {
            $('#resignHistory-table > tbody').empty();
            console.log(data);
            data.list.forEach((item, index) => {
                let output = `<tr>
                                        <td>${index + 1}</td>
                                        <td>${item.student_name}</td>
                                        <td>${item.student_email}</td>
                                        <td>${dateFormat(item.created_at)}</td>
                                        <td>${dateFormat(item.deleted_at)}</td>
                                     </tr>`;

                $('#resignHistory-table > tbody').append(output);
            });
        }
    });
}


$(function () {
    $(document).ready(getResignHistAjax());

    $(document).on('ajaxComplete', function () {
        $('#resignHistory-table').addClass('datatable');
        const datatables = select('.datatable', true);
        datatables.forEach(datatable => {
            new simpleDatatables.DataTable(datatable, {
                perPageSelect: [5, 10, 15, ["All", -1]],
                columns: [{
                    select: 0,
                    sortSequence: ["desc", "asc"]
                }, {
                    select: 1,
                    sortSequence: ["desc", "asc"]
                },
                    {
                        select: 2,
                        sortSequence: ["desc", "asc"]
                    },
                    {
                        select: 3,
                        type: "date",
                        format: "YYYY-MM-DD",
                        sortSequence: ["desc", "asc"]
                    },
                    {
                        select: 4,
                        type: "date",
                        format: "YYYY-MM-DD",
                        sortSequence: ["desc", "asc"]
                    }
                ]
            });
        });
    });
});