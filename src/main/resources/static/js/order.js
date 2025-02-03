const select = (el, all = false) => {
    el = el.trim()
    if (all) {
        return [...document.querySelectorAll(el)]
    } else {
        return document.querySelector(el)
    }
}

function listajax() {
    const limit = $('#itemsPerPage').val();
    
    $.ajax({
        type: "post",
        url: "orders/orderlist",
        data: { limit: limit },
        dataType: "json",
        cache: false,
        success: function(data) {
            printOrderList(data);
        },
        error: function(){
            console.log("주문 페이지 출력 에러");
        }
    });
}

function printOrderList(data) {
    const $tbody = $('#order-list-table tbody');
    $tbody.empty();
    
    if (!data.orderList || data.orderList.length === 0) {
        $tbody.append('<tr><td colspan="9" class="text-center">주문 내역이 없습니다.</td></tr>');
        return;
    }
    
    data.orderList.forEach(function(item) {
        const statusClass = item.payment_status === 'SUCCESS' ? 'bg-success' : 
                          item.payment_status === 'FAILED' ? 'bg-danger' : 'bg-secondary';
                          
        let output = `<tr>
            <td>${item.student_name}</td>
            <td>${item.course_name}</td>
            <td>${item.course_price.toLocaleString()}원</td>
            <td>${item.payment_amount.toLocaleString()}원</td>
            <td>${item.payment_method}</td>
            <td><span class="badge ${statusClass}">${item.payment_status}</span></td>
            <td>${item.order_date}</td>
            <td>${item.payment_approved_at}</td>
            <td>
                <button type="button" class="btn btn-secondary btn-sm rounded-pill payment-detail"
                        data-payment-id="${item.payment_id}"
                        data-enrollment-id="${item.enrollment_id}">
                    상세 보기
                </button>
            </td>
        </tr>`;
        
        $tbody.append(output);
    });
}

function orderDetail(id) {
    $.ajax({
        type: "post",
        url: "orders/orderdetail",
        data: {"id": id},
        dataType: "html",
        cache: false,
        success: function(data) {
            $('#orderDetailModal .modal-body').html(data);
        },
        error: function(){
            console.log("주문 상세 페이지 출력 에러");
        }
    });
}

$(function(){
    // 페이지 로드 시 주문 목록 조회
    listajax();
    
    // 페이지당 항목 수 변경 이벤트
    $('#itemsPerPage').change(function() {
        listajax();
    });
    
    // 상세보기 버튼 클릭 이벤트
    $(document).on('click', '.payment-detail', function() {
        const paymentId = $(this).data('payment-id');
        const enrollmentId = $(this).data('enrollment-id');
        orderDetail(paymentId + ',' + enrollmentId);
        $('#orderDetailModal').modal('show');
    });
});

