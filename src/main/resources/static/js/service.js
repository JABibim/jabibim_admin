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
        url: "/admin/policy/service",
        data: {
            state: "ajax" // state 값을 직접 "ajax"로 설정합니다.
        },
        dataType: "json",
        cache: false,
        success: function (data) {
            console.log(data);
            printServiceList(data);
        },
        error: function (err) {
            console.log("----------------------")
            console.log(err)
            console.log("서비스 이용약관 목록 페이지 출력 에러");
        }
    });
}

function printServiceList(data) {
    const $table = $('#service-list-table');
    $table.find('tbody').empty();
    data.servicelist.forEach(function (item) {
        let output = `<tr>
                             <td>${item.rnum}</td>
                             <td>${item.service_term_effective_date}</td>
                             <td style="text-align: right; padding-right: 30px">
                              <button type="button" class="btn btn-outline-primary" 
                              onclick='location.href="/admin/policy/service/detail?rnum=${item.rnum}"'>본문보기</button>
                             </td>
                            </tr>`;
        $table.find('tbody').append(output);
    });
}

$(function () {
    $(document).ready(listajax);  // 즉시 실행하지 않고 함수 참조를 전달합니다.

    $(document).on("ajaxComplete", function () {
        $('#service-list-table').addClass('datatable');
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
                        type: "String",
                        format: "YYYY/MM/DD",
                        sortSequence: ["desc", "asc"]
                    },
                    {
                        select: 2,  // "비고" 열의 인덱스 (여기서는 2번째 열)
                        sortable: false, // 정렬 기능 비활성화
                        searchable: false
                    }

                ]
            });
        });
    });
});