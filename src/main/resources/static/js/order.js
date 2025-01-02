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
        url: "orders/orderlist",
        dataType: "json",
        cache: false,
        success: function(data) {
            console.log(data);
            printOrderList(data);
        }
        ,error: function(){
            console.log("주문 페이지 출력 에러");
        }
    });

}
function printOrderList (data) {
    const $table = $('#order-list-table');
    data.orderList.forEach(function(item) {
        let output = `<tr>
                             <td> ${item.student_name} </td>
                             <td> ${item.course_name} </td>
                             <td> ${item.course_price.toLocaleString()}</td>
                             <td> ${item.payment_amount.toLocaleString()} </td>
                             <td> ${item.payment_method} </td>
                             <td> ${item.payment_status} </td>
                             <td> ${item.order_date} </td>
                             <td> ${item.payment_approved_at} </td>
                             // payment_id 와 enrollment_id 암호화 전달 예정
                             <td><input type="hidden" name="id" value="${item.payment_id},${item.enrollment_id}"><button type="button" class="btn btn-secondary rounded-pill payment-detail">상세 보기</button></td>
                            </tr>`;

        $table.find('tbody').append(output);
    });
}

function printOrderDetail(data) {
    $('#order-list-container > div > div > div').hide();

    $('#order-list-container > div > div').append(data);
}


function orderDetail(id) {
    $.ajax({
        type: "post",
        url: "orders/orderdetail",
        data: {"id": id},
        dataType: "html",
        cache: false,
        success: function(data) {
            console.log(data);
            printOrderDetail(data);
        },
        error: function(){
            console.log("주문 상세 페이지 출력 에러");
        }
    });
}

$(function(){
    $(document).ready(listajax())
    $(document).on( "ajaxComplete", function() {
        $('#order-list-table').addClass('datatable');
        const datatables = select('.datatable', true)
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
                    sortSequence: ["desc", "asc"],
                    className: "text-right"
                },
                {
                    select: 3,
                    sortSequence: ["desc","asc"],
                    className: "text-right"
                    },
                    {
                        select: 4,
                        sortSequence: ["desc","asc"]
                    },
                    {
                        select: 5,
                        sortSequence: ["desc","asc"]
                    },
                    {
                        select: 6,
                        type: "date",
                        format: "YYYY/MM/DD",
                        sortSequence: ["desc","asc"]
                    },
                    {
                        select: 7,
                        type: "date",
                        format: "YYYY/MM/DD",
                        sortSequence: ["desc","asc"]
                    },
                    {
                        select: 8,
                        sortable: false,
                        searchable: false
                    }
                ]
            });
        })
        $('#order-list-container > div > div > div > div > div > div.datatable-container').css('border', '0');
    });
    $(document).on("click", ".payment-detail", function(e){
        const val = $(this).parent().find('input').val();
        console.log(window.location.hash);
        window.location.hash="#orderDetail";
        orderDetail(val);
    });

    $(document).on('click', '#order-list-container > div > div > div.row > div.col-12 > button', function() {
        $('#order-list-container > div > div > div').show();
        $('#order-list-container > div > div > div.row').remove();
    });
});

