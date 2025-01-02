const select = (el, all = false) => {
    el = el.trim()
    if (all) {
        return [...document.querySelectorAll(el)]
    } else {
        return document.querySelector(el)
    }
}

function refundlistajax() {
    $.ajax({
        type: "post",
        url: "refundlist",
        dataType: "json",
        cache: false,
        success: function(data) {
            console.log(data);
            printRefundList(data);
        }
        ,error: function(){
            console.log("환불 페이지 출력 에러");
        }
    });

}
function printRefundList (data) {
    const $table = $('#refund-list-table');
    data.refundList.forEach(function(item) {
        let output = `<tr>
                             <td>${item.student_name}</a></td>
                             <td> ${item.course_name} </td>
                             <td> ${item.payment_amount.toLocaleString()} </td>
                             <td> ${item.refund_amount.toLocaleString()} </td>
                             <td> ${item.refund_date} </td>
                             <td> ${item.refund_status} </td>
                             <td><input type="hidden" name="id" value="${item.refund_id},${item.payment_id},${item.enrollment_id}"><button type="button" class="btn btn-secondary rounded-pill refund-detail">상세 보기</button></td>
                            </tr>`;

        $table.find('tbody').append(output);
    });
}

function printRefundDetail(data) {
    $('#refund-list-container > div > div > div > div > div').hide();

    $('#refund-list-container > div > div > div > div').append(data);
}

function refundDetail(id) {
    $.ajax({
        type: "post",
        url: "refunddetail",
        data: {"id": id},
        dataType: "html",
        cache: false,
        success: function(data) {
            console.log(data);
            printRefundDetail(data);
        },
        error: function(){
            console.log("주문 상세 페이지 출력 에러");
        }
    });
}



$(function(){
    $(document).ready(refundlistajax());
    $(document).on( "ajaxComplete", function() {
        $('#refund-list-table').addClass('datatable');
        const datatables = select('.datatable', true);
        datatables.forEach(datatable => {
            new simpleDatatables.DataTable(datatable, {
                perPageSelect: [5, 10, 15, ["All", -1]],
                columns: [{
                    select: 0,
                    sortSequence: ["desc", "asc"]
                },{
                    select: 1,
                    sortSequence: ["desc", "asc"]
                },
                  {
                    select: 2,
                    sortSequence: ["desc", "asc"]
                },
                    {
                        select: 3,
                        sortSequence: ["desc","asc"]
                    },
                    {
                        select: 4,
                        type: "date",
                        format: "YYYY/MM/DD",
                        sortSequence: ["desc","asc"]
                    },
                    {
                        select: 5,
                        sortSequence: ["desc","asc"]
                    },
                    {
                        select: 6,
                        sortable: false,
                        searchable: false
                    }
                ]
            });
        })
        $('#refund-list-container > div > div > div > div > div > div.datatable-container').css('border', '0');
    });

    $(document).on('click','.refund-detail', function(e) {
        const val = $(this).parent().find('input').val();
        console.log(window.location.hash);
        window.location.hash="#refundDetail";
        refundDetail(val);
    });

    $(document).on('click','#refund-list-container > div > div > div > div > div.row > div > div > div:nth-child(4) > div > div > div > div:nth-child(17) > button.btn.btn-danger.btn-sm.col-2', function() {
        $('#refund-list-container > div > div > div > div > div.datatable-wrapper.datatable-loading.no-footer.sortable.searchable.fixed-columns').show();
        $('#refund-list-container > div > div > div > div > div.row').remove();

    })


});

