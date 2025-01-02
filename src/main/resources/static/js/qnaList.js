const select = (el, all = false) => {
    el = el.trim()
    if (all) {
        return [...document.querySelectorAll(el)]
    } else {
        return document.querySelector(el)
    }
}

function listajax() {
    $.ajax({
        type: "post",
        url: "/admin/board/qna",
        data: {
            state: "ajax" // state 값을 직접 "ajax"로 설정합니다.
        },
        dataType: "json",
        cache: false,
        success: function(data) {
            console.log(data);
            printqnaList(data);
        },
        error: function() {
            console.log("QNA 목록 페이지 출력 에러");
        }
    });
}

function printqnaList(data) {
    const $table = $('#qna-list-table');
    $table.find('tbody').empty();
    data.qnalist.forEach(function(item) {
        let exposureStatus = item.board_exposure_status === 1
            ? '<span class="badge bg-primary">노출</span>'
            : '<span class="badge bg-secondary">숨김</span>';

        let output = `<tr>
                                        <td>${item.rnum}</td>
                                        <td>
                                        <a href="/admin/board/qna/detail?id=${item.board_id}&rnum=${item.rnum}">${item.board_title}[${item.cnt}]</a>
                                        </td>
                                        <td>${item.student_name}</td>
                                        <td>${item.class_name}</td>
                                        <td>${item.course_name}</td>
                                        <td>${item.student_email}</td>
                                        <td>${item.created_at}</td>
                                        <td>${item.teacher_name}</td>
                                        <td>${item.cnt >= 1 ? '<span class="badge rounded-pill bg-primary">답변 완료</span>' :
                                                                     '<span class="badge rounded-pill bg-warning">답변 대기</span>'}</td>
                                        <td>${exposureStatus}</td>
                                        <td>${item.board_read_count}</td>
                                    </tr>`;
        $table.find('tbody').append(output);
    });
}

$(function(){
    $(document).ready(listajax);  // 즉시 실행하지 않고 함수 참조를 전달합니다.

    $(document).on("ajaxComplete", function() {
        $('#qna-list-table').addClass('datatable');
        const datatables = select('.datatable', true)
        datatables.forEach(datatable => {
            new simpleDatatables.DataTable(datatable, {
                perPageSelect: [5, 10, 15, ["All", -1]],
                columns: [{
                    select: 0,
                    sortSequence: ["desc", "asc"]
                },
                    {
                        select: 1,
                        sortable: false,
                    },
                    {
                        select: 2,
                        sortable: false,
                    },
                    {
                        select: 3,
                        sortable: false,
                    },
                    {
                        select: 4,
                        sortable: false,
                    },
                    {
                        select: 5,
                        sortable: false,
                    },

                    {
                        select: 6,
                        type: "String",
                        format: "YYYY/MM/DD",
                        sortSequence: ["desc", "asc"]
                    },
                    {
                        select: 7,
                        sortSequence: ["desc", "asc"]
                    },
                    {
                        select: 8,
                        sortSequence: ["desc", "asc"]
                    },
                    {
                        select: 9,
                        sortable: false,
                    },
                    {
                        select: 10,
                        sortSequence: ["desc", "asc"]
                    }
                ]
            });
        });
    });
});
