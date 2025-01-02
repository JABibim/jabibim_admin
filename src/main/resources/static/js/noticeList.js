function showToast(message) {
    // 토스트 메시지 업데이트
    $('#validationToast .toast-body').text(message);

    // Bootstrap 토스트 객체 생성
    const toastElement = $('#validationToast');
    const toast = new bootstrap.Toast(toastElement);

    // 토스트 표시
    toast.show();
}

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
        url: "/admin/board/notice",
        data: {
            state: "ajax" // state 값을 직접 "ajax"로 설정합니다.
        },
        dataType: "json",
        cache: false,
        success: function (data) {
            console.log(data);
            printnoticeList(data);
        },
        error: function () {
            console.log("공지사항 목록 페이지 출력 에러");
        }
    });
}

function printnoticeList(data) {
    const $table = $('#notice-list-table');
    $table.find('tbody').empty();
    data.noticelist.forEach(function (item) {
        let exposureStatus = item.board_exposure_status === 1
            ? '<span class="badge bg-primary">노출</span>'
            : '<span class="badge bg-secondary">숨김</span>';

        let output = `<tr>
                             <td>${item.rnum}</td>
                             <td style="width: 35%">
                             <a href="/admin/board/notice/detail?id=${item.board_id}&rnum=${item.rnum}">${item.board_title}</a>
                             </td>
                             <td>${item.teacher_name}</td>
                             <td>${item.created_at}</td>
                             <td>${item.board_read_count}</td>
                             <td>${exposureStatus}</td>
                            </tr>`;

        $table.find('tbody').append(output);
    });
}

$(function () {
    $(document).ready(listajax);  // 즉시 실행하지 않고 함수 참조를 전달합니다.

    $(document).on("ajaxComplete", function () {
        $('#notice-list-table').addClass('datatable');
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
                        sortable: false
                    },
                    {
                        select: 2,
                        sortSequence: ["desc", "asc"]
                    },

                    {
                        select: 3,
                        type: "String",
                        format: "YYYY/MM/DD",
                        sortSequence: ["desc", "asc"]
                    },
                    {
                        select: 4,
                        sortSequence: ["desc", "asc"]
                    },
                    {
                        select: 5,
                        sortable: false,
                        searchable: false
                    }
                ]
            });
        });
    });

    $(document).on('click', '.writeBtn', function () {
        // [chan] 아카데미 정보가 없는 어드민 계정으로는 강의홈페이지에 노출될 공지사항을 작성할 수 없습니다.
        if (isAdmin) {
            showToast('어드민 계정으로는 강의홈페이지 공지사항을 등록할 수 없습니다.');
            return;
        }

        location.href = `notice/write`;
    })
});